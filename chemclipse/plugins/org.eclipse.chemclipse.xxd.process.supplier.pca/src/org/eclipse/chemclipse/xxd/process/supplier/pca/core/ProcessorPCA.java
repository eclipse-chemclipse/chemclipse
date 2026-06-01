/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Lorenz Gerber - PCA adapter, algorithm, opls target group, CV
 * Philip Wenig - get rid of JavaFX, feature selection
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.algorithms.CalculatorOPLS;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.algorithms.CalculatorOPLSRegression;
import org.eclipse.chemclipse.xxd.process.supplier.pca.exception.MathIllegalArgumentException;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Algorithm;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IMultivariateCalculator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IProcessor;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ResultsPCA;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class ProcessorPCA extends AbstractProcessorMultivariateAanalysis implements IProcessor {

	@Override
	public <V extends IVariable, S extends ISample> void cleanUnusedVariables(IEvaluation<IVariable, ISample, IResult> evaluationPCA, IProgressMonitor monitor) {

		if(evaluationPCA != null) {
			ISamplesPCA<IVariable, ISample> samples = evaluationPCA.getSamples();
			if(samples != null) {
				/*
				 * Collect
				 */
				List<IVariable> removeVariables = new ArrayList<>();
				List<IVariable> variables = samples.getVariables();
				List<ISample> sampleList = samples.getSamples();
				List<Integer> indices = new ArrayList<>();

				for(int i = 0; i < variables.size(); i++) {
					IVariable variable = variables.get(i);
					if(!variable.isSelected() || isInvalidVariable(samples, i)) {
						removeVariables.add(variable);
						indices.add(i);
					}
				}
				/*
				 * Remove
				 */
				samples.getVariables().removeAll(removeVariables);
				for(ISample sample : sampleList) {
					List<? extends ISampleData<?>> sampleData = sample.getSampleData();
					List<ISampleData<?>> sampleDataRemove = new ArrayList<>();
					for(int index : indices) {
						sampleDataRemove.add(sampleData.get(index));
					}
					sampleData.removeAll(sampleDataRemove);
				}
				/*
				 * Data Matrix
				 */
				calculateFeatureDataMatrix(evaluationPCA);
			}
		}
	}

	@Override
	public IEvaluation<IVariable, ISample, IResult> process(ISamplesPCA<IVariable, ISample> samples, IEvaluation<IVariable, ISample, IResult> masterEvaluation, IProgressMonitor monitor) throws MathIllegalArgumentException {

		EvaluationPCA evaluationPCA = null;
		if(samples != null) {
			SubMonitor subMonitor = SubMonitor.convert(monitor, "Calculate " + samples.getAnalysisSettings().getAlgorithm(), 160);
			try {
				/*
				 * Settings
				 */
				IAnalysisSettings analysisSettings = samples.getAnalysisSettings();
				ResultsPCA results = new ResultsPCA(analysisSettings);
				/*
				 * Template Map
				 */
				Map<String, Boolean> variablesSelectionMap = getVariablesSelectionMap(masterEvaluation != null ? masterEvaluation.getSamples().getVariables() : Collections.emptyList());
				subMonitor.worked(20);
				/*
				 * Preprocess
				 */
				IPreprocessingSettings preprocessingSettings = analysisSettings.getPreprocessingSettings();
				preprocessingSettings.process(samples, monitor);
				/*
				 * Variable Extraction
				 */
				int numberOfPrincipalComponents = analysisSettings.getNumberOfPrincipalComponents();
				Algorithm algorithm = analysisSettings.getAlgorithm();
				boolean[] selectedVariables = getSelectedVariables(samples, analysisSettings, variablesSelectionMap);
				Map<ISample, double[]> extractData = extractData(samples, algorithm, analysisSettings, selectedVariables);
				int numberPredictionSamples = numberPredictionSamples(extractData);
				assignVariables(results, samples, selectedVariables, variablesSelectionMap);
				int numberVariables = getNumSampleVars(extractData);
				subMonitor.worked(20);
				/*
				 * Prepare PCA Calculation
				 */
				IMultivariateCalculator principalComponentAnalysis = setupPCA(extractData, numberPredictionSamples, numberVariables, numberOfPrincipalComponents, algorithm, analysisSettings.getOplsTargetGroupName());
				subMonitor.worked(20);
				/*
				 * Compute PCA
				 */
				principalComponentAnalysis.compute();
				subMonitor.worked(20);
				/*
				 * Predict samples
				 */
				principalComponentAnalysis.predict();
				/*
				 * Check compute Status
				 */
				if(!principalComponentAnalysis.getComputeStatus()) {
					return null;
				}
				subMonitor.worked(20);
				/*
				 * Collect PCA results
				 */
				List<double[]> loadingVectors = getLoadingVectors(principalComponentAnalysis, numberOfPrincipalComponents);
				double[] explainedVariances = this.getExplainedVariances(principalComponentAnalysis, numberOfPrincipalComponents);
				double[] cumulativeExplainedVariances = this.getCumulativeExplainedVariances(explainedVariances);
				results.setLoadingVectors(loadingVectors);
				results.setExplainedVariances(explainedVariances);
				results.setCumulativeExplainedVariances(cumulativeExplainedVariances);
				if(Algorithm.OPLS_DA == algorithm && principalComponentAnalysis instanceof CalculatorOPLS calculatorOPLS) {
					if(calculatorOPLS.getPCovarianceMatrix() != null) {
						results.setPCovarianceValues(calculatorOPLS.getPCovarianceMatrix().getData().clone());
					}
					if(calculatorOPLS.getPCorrMatrix() != null) {
						results.setPCorrValues(calculatorOPLS.getPCorrMatrix().getData().clone());
					}
				} else if(Algorithm.OPLS == algorithm && principalComponentAnalysis instanceof CalculatorOPLSRegression calculatorOPLS) {
					if(calculatorOPLS.getPCovarianceMatrix() != null) {
						results.setPCovarianceValues(calculatorOPLS.getPCovarianceMatrix().getData().clone());
					}
					if(calculatorOPLS.getPCorrMatrix() != null) {
						results.setPCorrValues(calculatorOPLS.getPCorrMatrix().getData().clone());
					}
				} else {
					results.setPCovarianceValues(null);
					results.setPCorrValues(null);
				}

				setEigenSpaceAndErrorValues(principalComponentAnalysis, extractData, results);
				subMonitor.worked(20);
				/*
				 * Feature Data Matrix
				 */
				evaluationPCA = new EvaluationPCA(samples, results);
				calculateFeatureDataMatrix(evaluationPCA);
				subMonitor.worked(20);
			} finally {
				SubMonitor.done(subMonitor);
			}
		}
		return evaluationPCA;
	}
}