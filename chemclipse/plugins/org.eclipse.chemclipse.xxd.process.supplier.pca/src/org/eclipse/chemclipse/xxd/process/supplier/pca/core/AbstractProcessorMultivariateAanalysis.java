/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.ISamples;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.algorithms.CalculatorNIPALS;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.algorithms.CalculatorOPLS;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.algorithms.CalculatorSVD;
import org.eclipse.chemclipse.xxd.process.supplier.pca.exception.MathIllegalArgumentException;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Algorithm;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.Feature;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.FeatureDataMatrix;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IEvaluation;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IMultivariateCalculator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResult;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ResultMVA;

public abstract class AbstractProcessorMultivariateAanalysis {

	protected int numberPredictionSamples(Map<ISample, double[]> samples) {

		int numberPredictedSamples = (int)samples.keySet().stream().filter(x -> x.isPredicted()).count();
		return numberPredictedSamples;
	}

	protected void calculateFeatureDataMatrix(IEvaluation<IVariable, ISample, IResult> evaluationPCA) {

		ISamplesPCA<IVariable, ISample> samplesPCA = evaluationPCA.getSamples();
		/*
		 * Feature Data Matrix
		 */
		List<IVariable> variables = samplesPCA.getVariables();
		List<ISample> samples = samplesPCA.getSamples();
		/*
		 * Samples
		 */
		List<String> sampleNames = new ArrayList<>();
		List<String> groupNames = new ArrayList<>();
		for(ISample sample : samples) {
			if(sample.isSelected()) {
				sampleNames.add(sample.getSampleName());
				groupNames.add(sample.getGroupName());
			}
		}
		List<Feature> features = new ArrayList<>();
		for(IVariable variable : variables) {
			features.add(new Feature(variable));
		}
		//
		for(int i = 0; i < samples.size(); i++) {
			if(samples.get(i).isSelected()) {
				ISample sample = samples.get(i);
				List<? extends ISampleData<?>> sampleDataList = sample.getSampleData();
				for(int j = 0; j < sampleDataList.size(); j++) {
					ISampleData<?> sampleData = sampleDataList.get(j);
					features.get(j).getSampleData().add(sampleData);
				}
			}
		}
		//
		evaluationPCA.setFeatureDataMatrix(new FeatureDataMatrix(sampleNames, groupNames, features));
	}

	protected <V extends IVariable, S extends ISample> Map<S, double[]> extractData(ISamples<V, S> samples, Algorithm algorithm, IAnalysisSettings settings, boolean[] isSelectedVariable) {

		Map<S, double[]> selectedSamples = new HashMap<>();
		List<V> variables = samples.getVariables();
		/*
		 * Variables
		 */
		for(int i = 0; i < isSelectedVariable.length; i++) {
			IVariable variable = variables.get(i);
			isSelectedVariable[i] = isSelectedVariable[i] & variable.isSelected();
			if(settings.isRemoveUselessVariables()) {
				if(isInvalidVariable(samples, i)) {
					isSelectedVariable[i] = false;
					variable.setSelected(false);
				}
			}
		}
		/*
		 * Collect
		 */
		int numSelected = 0;
		for(boolean b : isSelectedVariable) {
			if(b) {
				numSelected++;
			}
		}
		//
		final Set<String> classifications = samples.getSamples().stream().map(s -> s.getClassification()).distinct().collect(Collectors.toList()).stream().limit(2).collect(Collectors.toSet());
		for(S sample : samples.getSamples()) {
			double[] selectedSampleData = null;
			if(sample.isSelected()) {
				List<? extends ISampleData<?>> data = sample.getSampleData();
				selectedSampleData = new double[numSelected];
				int j = 0;
				for(int i = 0; i < data.size(); i++) {
					if(isSelectedVariable[i]) {
						selectedSampleData[j] = data.get(i).getModifiedData();
						j++;
					}
				}
				selectedSamples.put(sample, selectedSampleData);
			}
		}
		//
		if(algorithm.equals(Algorithm.OPLS)) {
			Map<S, double[]> classificationSelected = selectedSamples.entrySet().stream().filter(e -> classifications.contains(e.getKey().getClassification())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
			return classificationSelected;
		}
		//
		return selectedSamples;
	}

	protected <V extends IVariable, S extends ISample> boolean[] getSelectedVariables(ISamples<V, S> samples, IAnalysisSettings settings, Map<String, Boolean> variablesSelectionMap) {

		List<V> variables = samples.getVariables();
		boolean[] selectedVariables = new boolean[variables.size()];
		Arrays.fill(selectedVariables, true);
		//
		for(int i = 0; i < selectedVariables.length; i++) {
			/*
			 * Variable
			 */
			IVariable variable = variables.get(i);
			variable.setSelected(true);
			//
			if(isVariableSelected(variable, variablesSelectionMap)) {
				if(settings.isRemoveUselessVariables()) {
					if(isInvalidVariable(samples, i)) {
						selectedVariables[i] = false;
						variable.setSelected(false);
					}
				}
			} else {
				selectedVariables[i] = false;
				variable.setSelected(false);
			}
		}
		//
		return selectedVariables;
	}

	protected <V extends IVariable, S extends ISample> boolean isInvalidVariable(ISamples<V, S> samples, int row) {

		return !samples.containsValidData(row);
	}

	protected List<double[]> getLoadingVectors(IMultivariateCalculator principalComponentAnalysis, int numberOfPrincipalComponents) {

		/*
		 * Print the basis vectors.
		 */
		List<double[]> loadingVectors = new ArrayList<double[]>();
		for(int principalComponent = 1; principalComponent <= numberOfPrincipalComponents; principalComponent++) {
			double[] loadingVector = principalComponentAnalysis.getLoadingVector(principalComponent);
			loadingVectors.add(loadingVector);
		}
		//
		return loadingVectors;
	}

	protected double[] getExplainedVariances(IMultivariateCalculator principalComponentAnalysis, int numberOfPrincipalComponents) {

		double summedVariance = principalComponentAnalysis.getSummedVariance();
		double[] explainedVariances = new double[numberOfPrincipalComponents];
		for(int i = 0; i < numberOfPrincipalComponents; i++) {
			explainedVariances[i] = 100.0 / summedVariance * principalComponentAnalysis.getExplainedVariance(i);
		}
		//
		return explainedVariances;
	}

	protected double[] getCumulativeExplainedVariances(double[] explainedVariances) {

		double[] cumulativeExplainedVariances = new double[explainedVariances.length];
		double cumVarTemp = 0.0;
		for(int i = 0; i < explainedVariances.length; i++) {
			cumulativeExplainedVariances[i] = cumVarTemp + explainedVariances[i];
			cumVarTemp = cumulativeExplainedVariances[i];
		}
		//
		return cumulativeExplainedVariances;
	}

	protected int getNumSampleVars(Map<ISample, double[]> extractData) {

		Iterator<Map.Entry<ISample, double[]>> it = extractData.entrySet().iterator();
		if(it.hasNext()) {
			return it.next().getValue().length;
		}
		//
		return -1;
	}

	/**
	 * Initializes the PCA analysis.
	 *
	 * @param pcaPeakMap
	 * @param numSamples
	 * @param sampleSize
	 * @param numberOfPrincipalComponents
	 * @return PrincipalComponentAnalysis
	 * @throws Exception
	 */
	protected IMultivariateCalculator setupPCA(Map<ISample, double[]> pcaPeakMap, int numberPredictionSamples, int sampleSize, int numberOfPrincipalComponents, Algorithm algorithm, String oplsTargetGroup) throws MathIllegalArgumentException {

		/*
		 * Initialize the PCA analysis.
		 */
		int numSamples = pcaPeakMap.size() - numberPredictionSamples;
		IMultivariateCalculator principalComponentAnalysis = null;
		if(algorithm.equals(Algorithm.NIPALS)) {
			principalComponentAnalysis = new CalculatorNIPALS(numSamples, sampleSize, numberOfPrincipalComponents, numberPredictionSamples);
		} else if(algorithm.equals(Algorithm.SVD)) {
			principalComponentAnalysis = new CalculatorSVD(numSamples, sampleSize, numberOfPrincipalComponents, numberPredictionSamples);
		} else if(algorithm.equals(Algorithm.OPLS)) {
			principalComponentAnalysis = new CalculatorOPLS(numSamples, sampleSize, numberOfPrincipalComponents, oplsTargetGroup, numberPredictionSamples);
		}
		/*
		 * Add the samples.
		 */
		for(Map.Entry<ISample, double[]> entry : pcaPeakMap.entrySet()) {
			if(entry.getKey().isPredicted()) {
				principalComponentAnalysis.addPrediction(entry.getValue(), entry.getKey(), entry.getKey().getGroupName(), entry.getKey().getClassification());
			} else {
				principalComponentAnalysis.addObservation(entry.getValue(), entry.getKey(), entry.getKey().getGroupName(), entry.getKey().getClassification());
			}
		}
		//
		return principalComponentAnalysis;
	}

	protected void setEigenSpaceAndErrorValues(IMultivariateCalculator principalComponentAnalysis, Map<ISample, double[]> pcaPeakMap, IResultsMVA pcaResults) {

		/*
		 * Set the eigen space and error membership values.
		 */
		List<IResultMVA> resultsList = new ArrayList<>();
		for(Entry<ISample, double[]> entry : pcaPeakMap.entrySet()) {
			double[] sampleData = entry.getValue();
			ISample sample = entry.getKey();
			IResultMVA pcaResult = new ResultMVA(sample);
			pcaResult.setScoreVector(principalComponentAnalysis.getScoreVector(sample));
			pcaResult.setErrorMetric(principalComponentAnalysis.getErrorMetric(sampleData));
			pcaResult.setSampleData(sampleData);
			resultsList.add(pcaResult);
		}
		//
		pcaResults.getPcaResultList().clear();
		pcaResults.getPcaResultList().addAll(resultsList);
	}

	protected void assignVariables(IResultsMVA pcaResults, ISamples<IVariable, ISample> samples, boolean[] isSelectedVariables, Map<String, Boolean> variablesSelectionMap) {

		/*
		 * Clear the variables.
		 */
		pcaResults.getExtractedVariables().clear();
		/*
		 * Assign and validate the variables again.
		 */
		for(int i = 0; i < samples.getVariables().size(); i++) {
			if(isSelectedVariables[i]) {
				IVariable variable = samples.getVariables().get(i);
				variable.setSelected(isVariableSelected(variable, variablesSelectionMap));
				pcaResults.getExtractedVariables().add(variable);
			}
		}
	}

	protected Map<String, Boolean> getVariablesSelectionMap(List<IVariable> templateVariables) {

		/*
		 * Map existing variables. They have been probably deactivated.
		 */
		Map<String, Boolean> variablesSelectionMap = new HashMap<>();
		for(IVariable variable : templateVariables) {
			variablesSelectionMap.put(variable.getValue(), variable.isSelected());
		}
		//
		return variablesSelectionMap;
	}

	protected boolean isVariableSelected(IVariable variable, Map<String, Boolean> variablesSelectionMap) {

		if(variable.isSelected()) {
			return variablesSelectionMap.getOrDefault(variable.getValue(), true);
		}
		//
		return false;
	}
}
