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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Describe the JSON format of a controlled vocabulary, analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#cv
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "version", "full_name", "uri"})
public class Cv {

	/**
	 * The short identifier used for CURIEs from this controlled vocabulary.
	 */
	@JsonProperty(value = "id", required = true)
	@JsonPropertyDescription("The short identifier used for CURIEs from this controlled vocabulary.")
	private String id;

	/**
	 * The version for this controlled vocabulary, like a release number, date, or similar. No particular format is expected.
	 */
	@JsonProperty(value = "version", required = true)
	@JsonPropertyDescription("The version for this controlled vocabulary, like a release number, date, or similar. No particular format is expected.")
	private String version;

	/**
	 * The usual name for the resource (e.g. The PSI-MS Controlled Vocabulary).
	 */
	@JsonProperty(value = "full_name", required = true)
	@JsonPropertyDescription("The usual name for the resource (e.g. The PSI-MS Controlled Vocabulary).")
	private String fullName;

	/**
	 * The URI for the controlled vocabulary.
	 */
	@JsonProperty(value = "uri", required = true)
	@JsonPropertyDescription("The URI for the controlled vocabulary.")
	private String uri;

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getVersion() {

		return version;
	}

	public void setVersion(String version) {

		this.version = version;
	}

	public String getFullName() {

		return fullName;
	}

	public void setFullName(String fullName) {

		this.fullName = fullName;
	}

	public String getUri() {

		return uri;
	}

	public void setUri(String uri) {

		this.uri = uri;
	}
}
