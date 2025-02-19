/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - add highlight data, prediction
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;

public class EvaluationPCA {

	private ISamplesPCA<? extends IVariable, ? extends ISample> samples = null;
	private IResultsPCA<? extends IResultPCA, ? extends IVariable> results = null;
	private ISamplesPCA<? extends IVariable, ? extends ISample> predict = null;
	/*
	 * The feature data matrix can be calculated after samples and results are set.
	 */
	private FeatureDataMatrix featureDataMatrix = null;
	private List<ISample> highlightedSamples = new ArrayList<ISample>();
	private List<IVariable> highlightedVariables = new ArrayList<IVariable>();

	public EvaluationPCA(ISamplesPCA<? extends IVariable, ? extends ISample> samples, IResultsPCA<? extends IResultPCA, ? extends IVariable> results) {

		this.samples = samples;
		this.results = results;
	}

	public ISamplesPCA<? extends IVariable, ? extends ISample> getSamples() {

		return samples;
	}

	public IResultsPCA<? extends IResultPCA, ? extends IVariable> getResults() {

		return results;
	}

	public FeatureDataMatrix getFeatureDataMatrix() {

		return featureDataMatrix;
	}

	public void setFeatureDataMatrix(FeatureDataMatrix featureDataMatrix) {

		this.featureDataMatrix = featureDataMatrix;
	}

	public void setHighlightedSamples(List<ISample> samples) {

		this.highlightedSamples = samples;
	}

	public List<ISample> getHighlightedSamples() {

		return this.highlightedSamples;
	}

	public void setHighlightedVariables(List<IVariable> variables) {

		this.highlightedVariables = variables;
	}

	public List<IVariable> getHighlightedVariables() {

		return this.highlightedVariables;
	}
}