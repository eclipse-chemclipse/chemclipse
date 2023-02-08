/*******************************************************************************
 * Copyright (c) 2008, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Dr. Alexander Kerner - implementation
 * Christoph Läubrich - add option to detect on individual traces
 * Matthias Mailänder - remove the window size enum
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.settings.AbstractPeakDetectorSettingsMSD;
import org.eclipse.chemclipse.chromatogram.peak.detector.core.FilterMode;
import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty.Validation;
import org.eclipse.chemclipse.support.settings.serialization.WindowSizeDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PeakDetectorSettingsMSD extends AbstractPeakDetectorSettingsMSD {

	@JsonProperty(value = "%Threshold", defaultValue = "MEDIUM")
	private Threshold threshold = Threshold.MEDIUM;
	//
	@JsonProperty(value = "%IncludeBackground", defaultValue = "false")
	@JsonPropertyDescription("%IncludeBackgroundDescription")
	private boolean includeBackground = false;
	//
	@JsonProperty(value = "%MinSignalToNoiseRatio", defaultValue = "0")
	@FloatSettingsProperty(minValue = 0f, maxValue = Float.MAX_VALUE)
	private float minimumSignalToNoiseRatio;
	//
	@JsonProperty(value = "%WindowSize", defaultValue = "5")
	@JsonPropertyDescription(value = "%WindowSizeDescription")
	@JsonDeserialize(using = WindowSizeDeserializer.class)
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_WINDOW_SIZE, maxValue = PreferenceSupplier.MAX_WINDOW_SIZE, validation = Validation.ODD_NUMBER_INCLUDING_ZERO)
	private int windowSize = 5;
	//
	@JsonProperty(value = "%UseNoiseSegments", defaultValue = "false")
	@JsonPropertyDescription(value = "%UseNoiseSegmentsDescription")
	private boolean useNoiseSegments = false;
	//
	@JsonProperty(value = "%FilterMode", defaultValue = "EXCLUDE")
	private FilterMode filterMode = FilterMode.EXCLUDE;
	//
	@JsonProperty(value = "%FilterMasses", defaultValue = "")
	private String filterIonsString;
	//
	@JsonProperty(value = "%UseIndividualTraces", defaultValue = "false")
	@JsonPropertyDescription("%UseIndividualTracesDescription")
	private boolean useIndividualTraces = false;
	//
	@JsonProperty(value = "%OptimizeBaselineVV", defaultValue = "false")
	private boolean optimizeBaseline = false;

	@JsonIgnore
	public Collection<IMarkedIons> getFilterIons() {

		MarkedTraceModus ionMarkMode;
		switch(getFilterMode()) {
			case EXCLUDE:
				ionMarkMode = MarkedTraceModus.INCLUDE;
				break;
			case INCLUDE:
				ionMarkMode = MarkedTraceModus.EXCLUDE;
				break;
			default:
				throw new IllegalArgumentException("Unsupported filter mode " + getFilterMode());
		}
		Set<MarkedIon> ions = parseIons(filterIonsString).stream().map(e -> new MarkedIon(e.doubleValue())).collect(Collectors.toSet());
		if(isUseIndividualTraces()) {
			List<IMarkedIons> list = new ArrayList<>();
			for(MarkedIon ion : ions) {
				MarkedIons ionlist = new MarkedIons(ionMarkMode);
				ionlist.add(ion);
				list.add(ionlist);
			}
			return list;
		} else {
			MarkedIons ionlist = new MarkedIons(ionMarkMode);
			ionlist.addAll(ions);
			return Collections.singleton(ionlist);
		}
	}

	public boolean isIncludeBackground() {

		return includeBackground;
	}

	public void setIncludeBackground(boolean includeBackground) {

		this.includeBackground = includeBackground;
	}

	public Threshold getThreshold() {

		return threshold;
	}

	public void setThreshold(Threshold threshold) {

		this.threshold = threshold;
	}

	public float getMinimumSignalToNoiseRatio() {

		return minimumSignalToNoiseRatio;
	}

	public void setMinimumSignalToNoiseRatio(float minimumSignalToNoiseRatio) {

		this.minimumSignalToNoiseRatio = minimumSignalToNoiseRatio;
	}

	public int getMovingAverageWindowSize() {

		return windowSize;
	}

	public void setMovingAverageWindowSize(int windowSize) {

		this.windowSize = windowSize;
	}

	public FilterMode getFilterMode() {

		return filterMode == null ? FilterMode.EXCLUDE : filterMode;
	}

	public void setFilterMode(FilterMode filterMode) {

		this.filterMode = filterMode;
	}

	public String getFilterIonsString() {

		return filterIonsString;
	}

	public void setFilterIonsString(String filterIonsString) {

		this.filterIonsString = filterIonsString;
	}

	static Collection<Number> parseIons(String filterIonsString) {

		if(StringUtils.isBlank(filterIonsString)) {
			return Collections.emptyList();
		}
		List<Number> ionNumbers = new ArrayList<>();
		String[] split = filterIonsString.trim().split("[\\s.,;]+");
		for(String s : split) {
			try {
				ionNumbers.add(new BigDecimal(s));
			} catch(NumberFormatException e) {
				// invalid or empty string
			}
		}
		return ionNumbers;
	}

	public boolean isUseNoiseSegments() {

		return useNoiseSegments;
	}

	public void setUseNoiseSegments(boolean useNoiseSegments) {

		this.useNoiseSegments = useNoiseSegments;
	}

	public boolean isOptimizeBaseline() {

		return optimizeBaseline;
	}

	public void setOptimizeBaseline(boolean optimizeBaseline) {

		this.optimizeBaseline = optimizeBaseline;
	}

	public boolean isUseIndividualTraces() {

		return useIndividualTraces;
	}

	public void setUseIndividualTraces(boolean useIndividualTraces) {

		this.useIndividualTraces = useIndividualTraces;
	}
}
