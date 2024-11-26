/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.impl.settings;

import org.eclipse.chemclipse.chromatogram.filter.settings.AbstractChromatogramFilterSettings;
import org.eclipse.chemclipse.support.settings.StringSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class FilterSettingsHeaderInput extends AbstractChromatogramFilterSettings {

	@JsonProperty(value = "Header Key", defaultValue = "My Key")
	@JsonPropertyDescription(value = "The header key.")
	@StringSettingsProperty(allowEmpty = false)
	private String headerKey = "My Key";
	@JsonProperty(value = "Header Value", defaultValue = "")
	@JsonPropertyDescription(value = "The header value.")
	private String headerValue = "";

	public String getHeaderKey() {

		return headerKey;
	}

	public void setHeaderKey(String headerKey) {

		this.headerKey = headerKey;
	}

	public String getHeaderValue() {

		return headerValue;
	}

	public void setHeaderValue(String headerValue) {

		this.headerValue = headerValue;
	}
}