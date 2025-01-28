/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings;

import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.ChromatogramVersion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ChromatogramExportSettings {

	/*
	 * Avoid duplication of settings in ConverterProcessSettings.
	 * Hence, only version is listed here.
	 */
	@JsonProperty(value = "Version Chromatogram (*.ocb)", defaultValue = "V_1502")
	@JsonPropertyDescription(value = "Defines the version to store the chromatogram data.")
	private ChromatogramVersion chromatogramVersion = ChromatogramVersion.V_1502;

	public ChromatogramVersion getChromatogramVersion() {

		return chromatogramVersion;
	}

	public void setChromatogramVersion(ChromatogramVersion chromatogramVersion) {

		this.chromatogramVersion = chromatogramVersion;
	}
}