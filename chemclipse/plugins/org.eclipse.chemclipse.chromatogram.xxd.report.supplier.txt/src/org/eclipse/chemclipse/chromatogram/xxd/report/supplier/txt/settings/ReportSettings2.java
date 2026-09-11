/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - extend configuration
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.settings;

import org.eclipse.chemclipse.chromatogram.xxd.report.settings.DefaultChromatogramReportSettings;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.txt.preferences.PreferenceSupplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ReportSettings2 extends DefaultChromatogramReportSettings {

	@JsonProperty(value = "Delta Retention Time Left [ms]", defaultValue = "0")
	@JsonPropertyDescription(value = "This is the left delta retention time in milliseconds.")
	private int deltaRetentionTimeLeft = 0;

	@JsonProperty(value = "Delta Retention Time Right [ms]", defaultValue = "0")
	@JsonPropertyDescription(value = "This is the right delta retention time in milliseconds.")
	private int deltaRetentionTimeRight = 0;

	@JsonProperty(value = "Use Best Match", defaultValue = "" + PreferenceSupplier.DEF_USE_BEST_MATCH)
	private boolean useBestMatch;

	@JsonProperty(value = "Add Peak Area", defaultValue = "" + PreferenceSupplier.DEF_ADD_PEAK_AREA)
	private boolean addPeakArea;

	public int getDeltaRetentionTimeLeft() {

		return deltaRetentionTimeLeft;
	}

	public void setDeltaRetentionTimeLeft(int deltaRetentionTimeLeft) {

		this.deltaRetentionTimeLeft = deltaRetentionTimeLeft;
	}

	public int getDeltaRetentionTimeRight() {

		return deltaRetentionTimeRight;
	}

	public void setDeltaRetentionTimeRight(int deltaRetentionTimeRight) {

		this.deltaRetentionTimeRight = deltaRetentionTimeRight;
	}

	public boolean isUseBestMatch() {

		return useBestMatch;
	}

	public void setUseBestMatch(boolean useBestMatch) {

		this.useBestMatch = useBestMatch;
	}

	public boolean isAddPeakArea() {

		return addPeakArea;
	}

	public void setAddPeakArea(boolean addPeakArea) {

		this.addPeakArea = addPeakArea;
	}
}
