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
 * A description (one) of the samples used to generate this dataset. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#sample.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "parameters"})
public class Sample {

	/**
	 * A unique identifier for this sample.
	 */
	@JsonProperty(value = "id", required = true)
	@JsonPropertyDescription("A unique identifier for this sample.")
	private String id;

	/**
	 * A human-readable name for this sample that might be easier to recognize.
	 */
	@JsonProperty(value = "name", required = true)
	@JsonPropertyDescription("A human-readable name for this sample that might be easier to recognize.")
	private String name;

	/**
	 * Additional parameters describing this sample.
	 */
	@JsonProperty(value = "parameters", required = true)
	@JsonPropertyDescription("Additional parameters describing this sample.")
	private List<Param> parameters = new ArrayList<Param>();

	/**
	 * A unique identifier for this sample.
	 */
	@JsonProperty(value = "id", required = true)
	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
