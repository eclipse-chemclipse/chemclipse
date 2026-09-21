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
 * Describes a single step of data processing.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"order", "software_reference", "parameters"})
public class ProcessingMethod {

	/**
	 * The order in which the step is applied in the data processing pipeline.
	 */
	@JsonProperty("order")
	@JsonPropertyDescription("The order in which the step is applied in the data processing pipeline.")
	private Integer order;

	/**
	 * The identifier for a software entry that performed this operation.
	 */
	@JsonProperty("software_reference")
	@JsonPropertyDescription("The identifier for a software entry that performed this operation.")
	private String softwareReference;

	/**
	 * Additional parameters describing this data processing step denoting actions, parameters, and other descriptors.
	 */
	@JsonProperty("parameters")
	@JsonPropertyDescription("Additional parameters describing this data processing step denoting actions, parameters, and other descriptors.")
	private List<Param> parameters = new ArrayList<Param>();

	public Integer getOrder() {

		return order;
	}

	public void setOrder(Integer order) {

		this.order = order;
	}

	public String getSoftwareReference() {

		return softwareReference;
	}

	public void setSoftwareReference(String softwareReference) {

		this.softwareReference = softwareReference;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
