/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.targets;

public class TargetUnknownSettings {

	/*
	 * Unknown [57,71,43,85,41]
	 */
	private String targetName = "Unknown"; //$NON-NLS-1$
	private float matchQuality = 100.0f;
	private int numberTraces = 5;
	private boolean includeIntensityPercent = false;
	private String markerStart = "["; //$NON-NLS-1$
	private String markerStop = "]"; //$NON-NLS-1$
	private boolean includeRetentionTime = false;
	private boolean includeRetentionIndex = false;

	public String getTargetName() {

		return targetName;
	}

	public void setTargetName(String targetName) {

		this.targetName = (targetName == null) ? "" : targetName; //$NON-NLS-1$
	}

	public float getMatchQuality() {

		return matchQuality;
	}

	public void setMatchQuality(float matchQuality) {

		this.matchQuality = matchQuality;
	}

	public int getNumberTraces() {

		return numberTraces;
	}

	public void setNumberTraces(int numberTraces) {

		this.numberTraces = (numberTraces < 0) ? 0 : numberTraces;
	}

	public boolean isIncludeIntensityPercent() {

		return includeIntensityPercent;
	}

	public void setIncludeIntensityPercent(boolean includeIntensityPercent) {

		this.includeIntensityPercent = includeIntensityPercent;
	}

	public String getMarkerStart() {

		return markerStart;
	}

	public void setMarkerStart(String markerStart) {

		this.markerStart = (markerStart == null) ? "" : markerStart; //$NON-NLS-1$
	}

	public String getMarkerStop() {

		return markerStop;
	}

	public void setMarkerStop(String markerStop) {

		this.markerStop = (markerStop == null) ? "" : markerStop; //$NON-NLS-1$
	}

	public boolean isIncludeRetentionTime() {

		return includeRetentionTime;
	}

	public void setIncludeRetentionTime(boolean includeRetentionTime) {

		this.includeRetentionTime = includeRetentionTime;
	}

	public boolean isIncludeRetentionIndex() {

		return includeRetentionIndex;
	}

	public void setIncludeRetentionIndex(boolean includeRetentionIndex) {

		this.includeRetentionIndex = includeRetentionIndex;
	}
}
