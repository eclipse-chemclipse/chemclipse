/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - remove the window size enum, add wavelength filters
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.peak.detector.supplier.firstderivative.settings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.chemclipse.chromatogram.peak.detector.core.FilterMode;
import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.wsd.peak.detector.settings.AbstractPeakDetectorWSDSettings;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.model.DetectorType;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty.Validation;
import org.eclipse.chemclipse.support.settings.LabelProperty;
import org.eclipse.chemclipse.support.settings.serialization.WindowSizeDeserializer;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceRasteredWSD;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PeakDetectorSettingsWSD extends AbstractPeakDetectorWSDSettings {

	@JsonProperty(value = "Threshold", defaultValue = "MEDIUM")
	@LabelProperty(value = "%Threshold")
	private Threshold threshold = Threshold.MEDIUM;

	@JsonProperty(value = "Detector Type", defaultValue = "VV")
	@LabelProperty(value = "Detector Type", tooltip = "Select the option to set the peak baseline.")
	private DetectorType detectorType = DetectorType.VV;

	@JsonProperty(value = "Min S/N Ratio", defaultValue = "0")
	@LabelProperty(value = "%MinSignalToNoiseRatio")
	@FloatSettingsProperty(minValue = 0f, maxValue = Float.MAX_VALUE)
	private float minimumSignalToNoiseRatio;

	@JsonProperty(value = "Window Size", defaultValue = "5")
	@LabelProperty(value = "%WindowSize", tooltip = "%WindowSizeDescription")
	@JsonDeserialize(using = WindowSizeDeserializer.class)
	@IntSettingsProperty(minValue = 0, maxValue = 45, validation = Validation.ODD_NUMBER_INCLUDING_ZERO)
	private int windowSize = 5;

	@JsonProperty(value = "Use Noise-Segments", defaultValue = "false")
	@LabelProperty(value = "%UseNoiseSegments", tooltip = "%UseNoiseSegmentsDescription")
	private boolean useNoiseSegments = false;

	@JsonProperty(value = "Filter Mode", defaultValue = "EXCLUDE")
	@LabelProperty(value = "%FilterMode")
	private FilterMode filterMode = FilterMode.EXCLUDE;

	@JsonProperty(value = "Wavelengths to filter", defaultValue = "")
	@LabelProperty(value = "%FilterWavelengths")
	private String filterWavelengths;

	@JsonProperty(value = "Use Individual Wavelengths", defaultValue = "false")
	@LabelProperty(value = "%UseIndividualWavelengths", tooltip = "%UseIndividualWavelengthsDescription")
	private boolean useIndividualWavelengths = false;

	@JsonProperty(value = "Optimize Baseline (VV)", defaultValue = "false")
	@LabelProperty(value = "%OptimizeBaselineVV")
	private boolean optimizeBaseline = false;

	public PeakDetectorSettingsWSD() {

		windowSize = 5;
	}

	public Threshold getThreshold() {

		return threshold;
	}

	public void setThreshold(Threshold threshold) {

		if(threshold != null) {
			this.threshold = threshold;
		}
	}

	public DetectorType getDetectorType() {

		return detectorType;
	}

	public void setDetectorType(DetectorType detectorType) {

		this.detectorType = detectorType;
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

	public boolean isUseNoiseSegments() {

		return useNoiseSegments;
	}

	public void setUseNoiseSegments(boolean useNoiseSegments) {

		this.useNoiseSegments = useNoiseSegments;
	}

	public FilterMode getFilterMode() {

		return filterMode == null ? FilterMode.EXCLUDE : filterMode;
	}

	public void setFilterMode(FilterMode filterMode) {

		this.filterMode = filterMode;
	}

	static Collection<Number> parseWavelengths(String input) {

		if(StringUtils.isBlank(input)) {
			return Collections.emptyList();
		}

		List<Number> waveLengths = new ArrayList<>();
		String[] split = input.trim().split("[\\s.,;]+");
		for(String value : split) {
			try {
				waveLengths.add(new BigDecimal(value));
			} catch(NumberFormatException e) {
				// invalid or empty string
			}
		}

		return waveLengths;
	}

	public boolean isIndividualWavelengths() {

		return useIndividualWavelengths;
	}

	public void setUseIndividualTraces(boolean useIndividualWavelengths) {

		this.useIndividualWavelengths = useIndividualWavelengths;
	}

	@JsonIgnore
	public Collection<IMarkedTraces<ITrace>> getFilterWavelengths() {

		MarkedTraceModus markedTraceModus;
		switch(getFilterMode()) {
			case EXCLUDE:
				markedTraceModus = MarkedTraceModus.INCLUDE;
				break;
			case INCLUDE:
				markedTraceModus = MarkedTraceModus.EXCLUDE;
				break;
			default:
				throw new IllegalArgumentException("Unsupported filter mode " + getFilterMode());
		}
		/*
		 * Calculate the wavelengths to be used.
		 */
		Set<ITrace> traces = parseWavelengths(filterWavelengths).stream().map(e -> new TraceRasteredWSD(e.doubleValue())).collect(Collectors.toSet());
		if(isIndividualWavelengths()) {
			List<IMarkedTraces<ITrace>> list = new ArrayList<>();
			for(ITrace trace : traces) {
				IMarkedTraces<ITrace> markedTraces = new MarkedTraces(markedTraceModus);
				markedTraces.add(trace);
				list.add(markedTraces);
			}
			return list;
		} else {
			IMarkedTraces<ITrace> markedTraces = new MarkedTraces(markedTraceModus);
			markedTraces.addAll(traces);
			return Collections.singleton(markedTraces);
		}
	}

	public boolean isOptimizeBaseline() {

		return optimizeBaseline;
	}

	public void setOptimizeBaseline(boolean optimizeBaseline) {

		this.optimizeBaseline = optimizeBaseline;
	}
}
