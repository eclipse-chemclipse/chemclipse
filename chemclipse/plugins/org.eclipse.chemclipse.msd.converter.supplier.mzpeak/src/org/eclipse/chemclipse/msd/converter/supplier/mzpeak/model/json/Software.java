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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A piece of software. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#software.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "version", "parameters"})
public class Software {

	/**
	 * A unique identifier for this software, even amongst different versions of the same software.
	 */
	@JsonProperty("id")
	@JsonPropertyDescription("A unique identifier for this software, even amongst different versions of the same software.")
	private String id;

	/**
	 * The version of the software.
	 */
	@JsonProperty("version")
	@JsonPropertyDescription("The version of the software.")
	private String version;

	/**
	 * Additional parameters describing this software, such as its controlled vocabulary identifier, or the term MS:1000799 for custom unreleased software to denote its name.
	 */
	@JsonProperty("parameters")
	@JsonPropertyDescription("Additional parameters describing this software, such as its controlled vocabulary identifier, or the term MS:1000799 for custom unreleased software to denote its name.")
	private List<Param> parameters = new ArrayList<Param>();

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

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
