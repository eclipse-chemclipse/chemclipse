/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.filter.chromatogram.settings;

import org.eclipse.chemclipse.xxd.filter.model.MeasurementResultDeleteOption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class MeasurementResultsFilterSettings {

	@JsonProperty(value = "Delete Option", defaultValue = "IDENTIFIER_ALL")
	@JsonPropertyDescription(value = "In case of all, the measurements results are cleared completely.")
	private MeasurementResultDeleteOption measurementResultOption = MeasurementResultDeleteOption.IDENTIFIER_ALL;
	@JsonProperty(value = "Target", defaultValue = "")
	@JsonPropertyDescription(value = "Delete the measurement results by the given target (specific identifier or regular expression).")
	private String target = "";

	public MeasurementResultDeleteOption getMeasurementResultOption() {

		return measurementResultOption;
	}

	public void setMeasurementResultOption(MeasurementResultDeleteOption measurementResultOption) {

		this.measurementResultOption = measurementResultOption;
	}

	public String getTarget() {

		return target;
	}

	public void setTarget(String target) {

		this.target = target;
	}
}