/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - ResultsPCA implements IResultsMVA
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.IVariable;

public class ResultsPCA implements IResultsMVA {

	private List<double[]> loadingVectors;
	private double[] explainedVariances;
	private double[] cumulativeExplainedVariances;
	private double[] crossValidations;
	private double[] cumulativeCrossValidations;
	private List<IVariable> extractedVariables;
	private List<IResultMVA> pcaResultList;
	private double[] pCovarianceValues;
	private double[] pCorrValues;

	private IAnalysisSettings analysisSettings;

	public ResultsPCA(IAnalysisSettings analysisSettings) {

		this.analysisSettings = analysisSettings;
		extractedVariables = new ArrayList<>();
		pcaResultList = new ArrayList<>();
	}

	@Override
	public List<double[]> getLoadingVectors() {

		return loadingVectors;
	}

	@Override
	public List<IVariable> getExtractedVariables() {

		return extractedVariables;
	}

	@Override
	public List<IResultMVA> getPcaResultList() {

		return pcaResultList;
	}

	@Override
	public IAnalysisSettings getPcaSettings() {

		return analysisSettings;
	}

	@Override
	public void setLoadingVectors(List<double[]> loadingVectors) {

		this.loadingVectors = loadingVectors;
	}

	@Override
	public double[] getExplainedVariances() {

		return this.explainedVariances;
	}

	@Override
	public void setExplainedVariances(double[] explainedVariances) {

		this.explainedVariances = explainedVariances;
	}

	@Override
	public double[] getCumulativeExplainedVariances() {

		return this.cumulativeExplainedVariances;
	}

	@Override
	public void setCumulativeExplainedVariances(double[] cumulativeExplainedVariances) {

		this.cumulativeExplainedVariances = cumulativeExplainedVariances;
	}

	@Override
	public double[] getCrossValidations() {

		return this.crossValidations;
	}

	@Override
	public void setCrossValidations(double[] crossValidations) {

		this.crossValidations = crossValidations;
	}

	@Override
	public double[] getCumulativeCrossValidations() {

		return this.cumulativeCrossValidations;
	}

	@Override
	public void setCumulativeCrossValidations(double[] cumulativeCrossValidations) {

		this.cumulativeCrossValidations = cumulativeCrossValidations;
	}

	@Override
	public double[] getPCovarianceValues() {

		return pCovarianceValues;
	}

	@Override
	public void setPCovarianceValues(double[] pCovarianceValues) {

		this.pCovarianceValues = pCovarianceValues;
	}

	@Override
	public double[] getPCorrValues() {

		return pCorrValues;
	}

	@Override
	public void setPCorrValues(double[] pCorrValues) {

		this.pCorrValues = pCorrValues;
	}
}
