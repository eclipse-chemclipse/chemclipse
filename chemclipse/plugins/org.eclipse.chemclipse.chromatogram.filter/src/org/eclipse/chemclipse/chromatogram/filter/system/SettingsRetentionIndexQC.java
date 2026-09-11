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
package org.eclipse.chemclipse.chromatogram.filter.system;

import org.eclipse.chemclipse.processing.system.ISystemProcessSettings;
import org.eclipse.chemclipse.support.settings.LabelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsRetentionIndexQC implements ISystemProcessSettings {

	@JsonProperty(value = "Sort targets by RI delta", defaultValue = "false")
	@LabelProperty(value = "%UseRetentionIndex")
	private boolean useRetentionIndexQC = false;

	public boolean isUseRetentionIndexQC() {

		return useRetentionIndexQC;
	}

	public void setUseRetentionIndexQC(boolean useRetentionIndexQC) {

		this.useRetentionIndexQC = useRetentionIndexQC;
	}
}
