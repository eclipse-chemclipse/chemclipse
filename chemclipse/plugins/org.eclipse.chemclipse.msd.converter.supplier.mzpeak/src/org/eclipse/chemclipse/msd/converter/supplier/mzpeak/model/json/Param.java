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
 * mzPeak metadata JSON parameter
 * <p>
 * Describe the JSON format of controlled vocabulary or user-defined parameters.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "accession", "value", "unit"})
public class Param {

	/**
	 * The name of the parameter. If controlled, this should be the name from the source controlled vocabulary.
	 */
	@JsonProperty(value = "name", required = true)
	@JsonPropertyDescription("The name of the parameter. If controlled, this should be the name from the source controlled vocabulary.")
	private String name;

	/**
	 * The compact CURIE for the controlled vocabulary term, if it exists, null otherwise
	 */
	@JsonProperty("accession")
	@JsonPropertyDescription("The compact CURIE for the controlled vocabulary term, if it exists, null otherwise")
	private String accession = null;

	/**
	 * The value for this parameter, if any. This may be omitted if null
	 */
	@JsonProperty("value")
	@JsonPropertyDescription("The value for this parameter, if any. This may be omitted if null")
	private String value = null;

	/**
	 * The compact CURIE for the unit describing the measurement for this parameter
	 */
	@JsonProperty("unit")
	@JsonPropertyDescription("The compact CURIE for the unit describing the measurement for this parameter")
	private String unit = null;

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getAccession() {

		return accession;
	}

	public void setAccession(String accession) {

		this.accession = accession;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

	public String getUnit() {

		return unit;
	}

	public void setUnit(String unit) {

		this.unit = unit;
	}
}
