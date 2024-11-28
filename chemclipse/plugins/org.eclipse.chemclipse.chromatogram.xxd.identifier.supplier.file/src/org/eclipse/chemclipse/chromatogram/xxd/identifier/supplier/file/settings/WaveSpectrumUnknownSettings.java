/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.IWaveSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.identifier.AbstractIdentifierSettings;
import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class WaveSpectrumUnknownSettings extends AbstractIdentifierSettings implements IWaveSpectrumIdentifierSettings, IUnknownSettingsWSD {

	@JsonProperty(value = "Limit Match Factor", defaultValue = "80.0")
	@JsonPropertyDescription(value = "Run an identification if no target exists with a Match Factor >= the given limit.")
	@FloatSettingsProperty(minValue = IIdentifierSettings.MIN_LIMIT_MATCH_FACTOR, maxValue = IIdentifierSettings.MAX_LIMIT_MATCH_FACTOR)
	private float limitMatchFactor = 80.0f;
	@JsonProperty(value = "Target Name", defaultValue = PreferenceSupplier.DEF_TARGET_NAME_UNKNOWN)
	private String targetName = "Unknown";
	@JsonProperty(value = "Match Quality", defaultValue = "80.0")
	@JsonPropertyDescription(value = "The match quality is set as the Match Factor.")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_FACTOR, maxValue = PreferenceSupplier.MAX_FACTOR)
	private float matchQuality = 80.0f;
	@JsonProperty(value = "Number of wavelengths", defaultValue = "5")
	@JsonPropertyDescription(value = "This is the number of wavelengths printed, sorted asc by intensity.")
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_NUMBER_OF_WAVELENGTH, maxValue = PreferenceSupplier.MAX_NUMBER_OF_WAVELENGTH)
	private int numberOfWavelengths = 5;
	@JsonProperty(value = "Include Intensity [%]", defaultValue = "false")
	private boolean includeIntensityPercent = false;
	@JsonProperty(value = "Marker Start", defaultValue = PreferenceSupplier.DEF_MARKER_START_UNKNOWN)
	@JsonPropertyDescription(value = "Use e.g. a opening bracket.")
	private String markerStart = "[";
	@JsonProperty(value = "Marker Stop", defaultValue = PreferenceSupplier.DEF_MARKER_STOP_UNKNOWN)
	@JsonPropertyDescription(value = "Use e.g. a closing bracket.")
	private String markerStop = "]";
	@JsonProperty(value = "Include Retention Time", defaultValue = "false")
	private boolean includeRetentionTime = false;
	@JsonProperty(value = "Include Retention Index", defaultValue = "false")
	private boolean includeRetentionIndex = false;

	@Override
	public float getLimitMatchFactor() {

		return limitMatchFactor;
	}

	@Override
	public void setLimitMatchFactor(float limitMatchFactor) {

		this.limitMatchFactor = limitMatchFactor;
	}

	@Override
	public String getTargetName() {

		return targetName;
	}

	@Override
	public void setTargetName(String targetName) {

		this.targetName = targetName;
	}

	@Override
	public float getMatchQuality() {

		return matchQuality;
	}

	@Override
	public void setMatchQuality(float matchQuality) {

		this.matchQuality = matchQuality;
	}

	public int getNumberOfWavelengths() {

		return numberOfWavelengths;
	}

	public int getNumberOfTraces() {

		return getNumberOfWavelengths();
	}

	public void setNumberOfWavelengths(int numberOfWavelengths) {

		this.numberOfWavelengths = numberOfWavelengths;
	}

	@Override
	public boolean isIncludeIntensityPercent() {

		return includeIntensityPercent;
	}

	@Override
	public void setIncludeIntensityPercent(boolean includeIntensityPercent) {

		this.includeIntensityPercent = includeIntensityPercent;
	}

	@Override
	public String getMarkerStart() {

		return markerStart;
	}

	@Override
	public void setMarkerStart(String markerStart) {

		this.markerStart = markerStart;
	}

	@Override
	public String getMarkerStop() {

		return markerStop;
	}

	@Override
	public void setMarkerStop(String markerStop) {

		this.markerStop = markerStop;
	}

	@Override
	public boolean isIncludeRetentionTime() {

		return includeRetentionTime;
	}

	@Override
	public void setIncludeRetentionTime(boolean includeRetentionTime) {

		this.includeRetentionTime = includeRetentionTime;
	}

	@Override
	public boolean isIncludeRetentionIndex() {

		return includeRetentionIndex;
	}

	@Override
	public void setIncludeRetentionIndex(boolean includeRetentionIndex) {

		this.includeRetentionIndex = includeRetentionIndex;
	}
}
