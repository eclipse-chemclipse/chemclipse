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
 * Philip Wenig - refactoring
 * Lorenz Gerber - area/concentration labels
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.List;

import org.eclipse.chemclipse.model.statistics.AbstractSamples;
import org.eclipse.chemclipse.model.statistics.IVariable;

public class Samples extends AbstractSamples<IVariable, Sample> implements ISamplesPCA<IVariable, Sample> {

	/**
	 * What the sample data values represent. Only informative (labels/display) - all calculations are
	 * independent of it. Empty (default) when the source doesn't say, e.g. long/text format imports.
	 */
	public static final String VALUE_TYPE_AREA = "Area";
	public static final String VALUE_TYPE_CONCENTRATION = "Concentration";

	private IAnalysisSettings analysisSettings = new AnalysisSettings();
	private String valueType = "";
	private String valueUnit = "";

	public Samples(List<Sample> samples) {

		super();
		for(Sample sample : samples) {
			getSamples().add(sample);
		}
	}

	@Override
	public IAnalysisSettings getAnalysisSettings() {

		return analysisSettings;
	}

	@Override
	public void setAnalysisSettings(IAnalysisSettings analysisSettings) {

		this.analysisSettings = analysisSettings;
	}

	/**
	 * @return {@link #VALUE_TYPE_AREA}, {@link #VALUE_TYPE_CONCENTRATION}, another label or "" if unknown (default)
	 */
	public String getValueType() {

		return valueType;
	}

	public void setValueType(String valueType) {

		this.valueType = valueType == null ? "" : valueType.trim();
	}

	/**
	 * @return the unit of the values, e.g. of a concentration, or "" if unknown/not applicable
	 */
	public String getValueUnit() {

		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {

		this.valueUnit = valueUnit == null ? "" : valueUnit;
	}

	/**
	 * @return e.g. "Area" or "Concentration [mg/L]", or "" if the value type is unknown
	 */
	public String getValueLabel() {

		if(valueType.isBlank()) {
			return "";
		}
		return valueUnit.isBlank() ? valueType : valueType + " [" + valueUnit + "]";
	}
}
