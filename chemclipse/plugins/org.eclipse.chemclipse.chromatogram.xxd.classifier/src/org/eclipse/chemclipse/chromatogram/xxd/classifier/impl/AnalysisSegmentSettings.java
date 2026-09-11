/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.classifier.impl;

import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.AbstractChromatogramClassifierSettings;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class AnalysisSegmentSettings extends AbstractChromatogramClassifierSettings {

	@JsonProperty(value = "Analysis Segment Width (ms)", defaultValue = "4000")
	@JsonPropertyDescription(value = "The width of each analysis segment in milliseconds.")
	@IntSettingsProperty(minValue = 1, maxValue = Integer.MAX_VALUE)
	private int analysisSegmentWidth = 4000;

	public int getAnalysisSegmentWidth() {

		return analysisSegmentWidth;
	}

	public void setAnalysisSegmentWidth(int analysisSegmentWidth) {

		this.analysisSegmentWidth = analysisSegmentWidth;
	}
}