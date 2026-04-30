/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.chromatogram.settings;

import org.eclipse.chemclipse.model.identifier.IIdentifierSettings;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.xxd.filter.model.CoordinateOption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class TransferFilterSettings {

	@JsonProperty(value = "Limit Match Factor", defaultValue = "80.0")
	@JsonPropertyDescription(value = "Run an identification if no target exists with a Match Factor >= the given limit.")
	@FloatSettingsProperty(minValue = IIdentifierSettings.MIN_LIMIT_MATCH_FACTOR, maxValue = IIdentifierSettings.MAX_LIMIT_MATCH_FACTOR)
	private float limitMatchFactor = 80.0f;
	@JsonProperty(value = "Match Quality", defaultValue = "0.0")
	@JsonPropertyDescription(value = "Overrides the match quality. If zero, the existing match factor is used.")
	@FloatSettingsProperty(minValue = 0, maxValue = 100)
	private float matchQuality = 0.0f;
	@JsonProperty(value = "Coordinate Option", defaultValue = "RETENTION_TIME_MIN")
	@JsonPropertyDescription(value = "Use the given coordinate selection.")
	private CoordinateOption coordinateOption = CoordinateOption.RETENTION_TIME_MIN;
	@JsonProperty(value = "Coordinate Offset", defaultValue = "")
	@JsonPropertyDescription(value = "The given offset is used to adjust the transfer coordinates.")
	private double coordinateOffset = 0;

	public float getLimitMatchFactor() {

		return limitMatchFactor;
	}

	public void setLimitMatchFactor(float limitMatchFactor) {

		this.limitMatchFactor = limitMatchFactor;
	}

	public float getMatchQuality() {

		return matchQuality;
	}

	public void setMatchQuality(float matchQuality) {

		this.matchQuality = matchQuality;
	}

	public CoordinateOption getCoordinateOption() {

		return coordinateOption;
	}

	public void setCoordinateOption(CoordinateOption coordinateOption) {

		this.coordinateOption = coordinateOption;
	}

	public double getCoordinateOffset() {

		return coordinateOffset;
	}

	public void setCoordinateOffset(double coordinateOffset) {

		this.coordinateOffset = coordinateOffset;
	}
}