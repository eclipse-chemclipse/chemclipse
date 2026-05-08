/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - Refactor name
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.List;

import org.eclipse.chemclipse.model.statistics.IVariable;

public interface IResultsMVA extends IResults<IResultMVA, IVariable> {

	List<double[]> getLoadingVectors();

	double[] getExplainedVariances();

	List<IVariable> getExtractedVariables();

	List<IResultMVA> getPcaResultList();

	IAnalysisSettings getPcaSettings();

	void setLoadingVectors(List<double[]> loadingVectors);

	void setExplainedVariances(double[] explainedVariances);

	double[] getCumulativeExplainedVariances();

	void setCumulativeExplainedVariances(double[] cumulativeExplainedVariances);

	double[] getPCovarianceValues();

	void setPCovarianceValues(double[] pCovarianceValues);

	double[] getPCorrValues();

	void setPCorrValues(double[] pCorrValues);
}