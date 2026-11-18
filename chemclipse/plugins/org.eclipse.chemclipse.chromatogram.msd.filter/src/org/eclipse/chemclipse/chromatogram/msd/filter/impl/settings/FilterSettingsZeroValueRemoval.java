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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings;

import org.eclipse.chemclipse.chromatogram.filter.settings.AbstractChromatogramFilterSettings;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class FilterSettingsZeroValueRemoval extends AbstractChromatogramFilterSettings {

	public static final int DEF_MIN_COVERAGE = 50;
	public static final int MIN_MIN_COVERAGE = 0;
	public static final int MAX_MIN_COVERAGE = 100;
	public static final CoverageReference DEF_COVERAGE_REFERENCE = CoverageReference.FIRST_SIGNAL;
	public static final int DEF_MAX_SHORT_GAP = 2;
	public static final int MIN_MAX_SHORT_GAP = 0;
	public static final int MAX_MAX_SHORT_GAP = 20;
	//
	public static final String DESCRIPTION_MIN_COVERAGE = "Share of scans with signal from which an ion channel counts as continuous: in such a channel, gaps of any length between two signals are filled. 0 % fills every gap in every channel.";
	public static final String DESCRIPTION_COVERAGE_REFERENCE = "The scans the coverage is measured over: from the first signal of the channel (a channel that only starts later is judged on the part where it exists) or the whole range.";
	public static final String DESCRIPTION_MAX_SHORT_GAP = "In channels below the minimum coverage: the longest run of missing scans that is still filled. Longer runs there are taken as a real absence of the ion. 0 = none.";
	//
	@JsonProperty(value = "Min. Signal Coverage (%)", defaultValue = "50")
	@JsonPropertyDescription(value = DESCRIPTION_MIN_COVERAGE)
	@IntSettingsProperty(minValue = MIN_MIN_COVERAGE, maxValue = MAX_MIN_COVERAGE)
	private int minCoverage = DEF_MIN_COVERAGE;
	@JsonProperty(value = "Coverage Reference", defaultValue = "FIRST_SIGNAL")
	@JsonPropertyDescription(value = DESCRIPTION_COVERAGE_REFERENCE)
	private CoverageReference coverageReference = DEF_COVERAGE_REFERENCE;
	@JsonProperty(value = "Max. Gap in Sparse Channels (Scans)", defaultValue = "2")
	@JsonPropertyDescription(value = DESCRIPTION_MAX_SHORT_GAP)
	@IntSettingsProperty(minValue = MIN_MAX_SHORT_GAP, maxValue = MAX_MAX_SHORT_GAP)
	private int maxShortGap = DEF_MAX_SHORT_GAP;

	public int getMinCoverage() {

		return minCoverage;
	}

	public void setMinCoverage(int minCoverage) {

		this.minCoverage = minCoverage;
	}

	public CoverageReference getCoverageReference() {

		return coverageReference;
	}

	public void setCoverageReference(CoverageReference coverageReference) {

		this.coverageReference = coverageReference;
	}

	public int getMaxShortGap() {

		return maxShortGap;
	}

	public void setMaxShortGap(int maxShortGap) {

		this.maxShortGap = maxShortGap;
	}
}
