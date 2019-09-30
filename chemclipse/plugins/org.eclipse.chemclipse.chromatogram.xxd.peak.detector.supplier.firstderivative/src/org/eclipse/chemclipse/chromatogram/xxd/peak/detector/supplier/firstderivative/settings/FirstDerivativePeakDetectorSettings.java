/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - derived from WSD/MSD/CSD variants
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings;

import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.numeric.statistics.WindowSize;
import org.eclipse.chemclipse.support.settings.EnumSelectionRadioButtonsSettingProperty;
import org.eclipse.chemclipse.support.settings.EnumSelectionSettingProperty;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class FirstDerivativePeakDetectorSettings extends PeakDetectorSettingsMSD {

	@JsonProperty(value = "Threshold", defaultValue = "MEDIUM")
	@EnumSelectionRadioButtonsSettingProperty
	private Threshold threshold = Threshold.MEDIUM;
	@JsonProperty(value = "Include Background (VV: true, BV|VB: false)", defaultValue = "false")
	private boolean includeBackground = false;
	@JsonProperty(value = "Min S/N Ratio", defaultValue = "0")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_SN_RATIO_MIN, maxValue = PreferenceSupplier.MIN_SN_RATIO_MAX)
	private float minimumSignalToNoiseRatio;
	@JsonProperty(value = "Window Size", defaultValue = "WIDTH_5")
	@JsonPropertyDescription(value = "Window Size: 3, 5, 7, ..., 45")
	@EnumSelectionSettingProperty
	private WindowSize windowSize = WindowSize.WIDTH_5;
	@JsonProperty(value = "IonFilter", defaultValue = "EXCLUDE")
	@EnumSelectionRadioButtonsSettingProperty
	FilterMode filterMode = FilterMode.EXCLUDE;
	@JsonPropertyDescription(value = "Ions to filter: 16, 18, ...")
	@JsonProperty
	String filterIonsString;

	public FirstDerivativePeakDetectorSettings() {

		this(DataType.MSD);
	}

	public FirstDerivativePeakDetectorSettings(DataType dataType) {

		// we could load optimized settings depending on datatype here
	}
}
