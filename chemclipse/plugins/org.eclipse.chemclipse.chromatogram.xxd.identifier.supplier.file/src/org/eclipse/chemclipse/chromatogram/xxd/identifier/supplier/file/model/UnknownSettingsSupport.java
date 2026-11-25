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
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.model;

import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings.IUnknownSettings;
import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings.IUnknownSettingsMSD;
import org.eclipse.chemclipse.model.targets.TargetUnknownSettings;

public class UnknownSettingsSupport {

	public static TargetUnknownSettings getTargetUnknownSettings(IUnknownSettings unknownSettings) {

		TargetUnknownSettings targetUnknownSettings = new TargetUnknownSettings();

		targetUnknownSettings.setTargetName(unknownSettings.getTargetName());
		targetUnknownSettings.setMatchQuality(unknownSettings.getMatchQuality());
		if(unknownSettings instanceof IUnknownSettingsMSD unknownSettingsMSD) {
			targetUnknownSettings.setNumberTraces(unknownSettingsMSD.getNumberOfTraces());
			targetUnknownSettings.setIgnoreTraces(unknownSettingsMSD.getIgnoreTraces());
			targetUnknownSettings.setIncludeIntensityPercent(unknownSettingsMSD.isIncludeIntensityPercent());
		}
		targetUnknownSettings.setMarkerStart(unknownSettings.getMarkerStart());
		targetUnknownSettings.setMarkerStop(unknownSettings.getMarkerStop());
		targetUnknownSettings.setIncludeRetentionTime(unknownSettings.isIncludeRetentionTime());
		targetUnknownSettings.setIncludeRetentionIndex(unknownSettings.isIncludeRetentionIndex());

		return targetUnknownSettings;
	}
}