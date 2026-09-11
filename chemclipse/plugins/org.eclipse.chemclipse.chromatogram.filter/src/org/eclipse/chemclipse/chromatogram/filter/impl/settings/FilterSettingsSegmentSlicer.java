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
package org.eclipse.chemclipse.chromatogram.filter.impl.settings;

import org.eclipse.chemclipse.chromatogram.filter.model.SegmentSlicerOption;
import org.eclipse.chemclipse.chromatogram.filter.settings.AbstractChromatogramFilterSettings;
import org.eclipse.chemclipse.model.core.support.HeaderField;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class FilterSettingsSegmentSlicer extends AbstractChromatogramFilterSettings {

	@JsonProperty(value = "Header Field", defaultValue = "DATA_NAME")
	@JsonPropertyDescription(value = "Store the extracted transition in the selected header field.")
	private HeaderField headerField = HeaderField.DATA_NAME;

	@JsonProperty(value = "Cutter Option", defaultValue = "PEAKS")
	@JsonPropertyDescription(value = "Select whether to use existing peaks or analysis segments to determine the cut positions.")
	private SegmentSlicerOption cutterOption = SegmentSlicerOption.PEAKS;

	@JsonProperty(value = "Keep Peak In Center", defaultValue = "false")
	@JsonPropertyDescription(value = "Each section is centered exactly on its peak. The symmetric half-width is the global minimum left/right distance across all sections. Only applies when Cutter Option is Peaks.")
	private boolean keepPeakInCenter = false;

	@JsonProperty(value = "Reset Retention Times", defaultValue = "false")
	@JsonPropertyDescription(value = "The retention times are recalculated for each chromatogram with a scan delay of 0.")
	private boolean resetRetentionTimes = false;

	public HeaderField getHeaderField() {

		return headerField;
	}

	public void setHeaderField(HeaderField headerField) {

		this.headerField = headerField;
	}

	public SegmentSlicerOption getCutterOption() {

		return cutterOption;
	}

	public void setCutterOption(SegmentSlicerOption cutterOption) {

		this.cutterOption = cutterOption;
	}

	public boolean isKeepPeakInCenter() {

		return keepPeakInCenter;
	}

	public void setKeepPeakInCenter(boolean keepPeakInCenter) {

		this.keepPeakInCenter = keepPeakInCenter;
	}

	public boolean isResetRetentionTimes() {

		return resetRetentionTimes;
	}

	public void setResetRetentionTimes(boolean resetRetentionTimes) {

		this.resetRetentionTimes = resetRetentionTimes;
	}
}