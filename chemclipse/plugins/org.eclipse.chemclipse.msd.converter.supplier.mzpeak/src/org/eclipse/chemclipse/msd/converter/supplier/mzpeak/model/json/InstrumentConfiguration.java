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
 * Describes a single instrument configuration that was used. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#instrumentConfiguration
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"components", "software_reference", "id", "parameters"})
public class InstrumentConfiguration {

	@JsonProperty(value = "components", required = true)
	private List<ComponentType> components = new ArrayList<ComponentType>();

	/**
	 * The identifier for a software that was associated with the data acquisition process.
	 */
	@JsonProperty(value = "software_reference", required = true)
	@JsonPropertyDescription("The identifier for a software that was associated with the data acquisition process.")
	private String softwareReference;

	/**
	 * A unique identifier for this instrument configuration.
	 */
	@JsonProperty(value = "id", required = true)
	@JsonPropertyDescription("A unique identifier for this instrument configuration.")
	private Integer id;

	/**
	 * Additional parameters describing this configuration, like the instrument model and serial number
	 */
	@JsonProperty(value = "parameters", required = true)
	@JsonPropertyDescription("Additional parameters describing this configuration, like the instrument model and serial number")
	private List<Param> parameters = new ArrayList<Param>();

	public List<ComponentType> getComponents() {

		return components;
	}

	public void setComponents(List<ComponentType> components) {

		this.components = components;
	}

	public String getSoftwareReference() {

		return softwareReference;
	}

	public void setSoftwareReference(String softwareReference) {

		this.softwareReference = softwareReference;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
