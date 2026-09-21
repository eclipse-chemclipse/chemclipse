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
 * A mapping from a Parquet column to a controlled vocabulary term
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "path", "accession", "unit", "term_marker"})
public class ColumnMapping {

	/**
	 * The human-readable term name
	 */
	@JsonProperty(value = "name", required = true)
	@JsonPropertyDescription("The human-readable term name")
	private String name;

	/**
	 * The path in a Parquet schema for the mapped column delimited at nesting levels by '.', omitting [list, item|element] tokens
	 */
	@JsonProperty(value = "path", required = true)
	@JsonPropertyDescription("The path in a Parquet schema for the mapped column delimited at nesting levels by '.', omitting [list, item|element] tokens")
	private String path;

	/**
	 * The CURIE for the controlled vocabulary term, or null if no controlled vocabulary term is available. Null may be used to indicate that a column has a human readable name but does not map to a controlled vocabulary term
	 */
	@JsonProperty("accession")
	@JsonPropertyDescription("The CURIE for the controlled vocabulary term, or null if no controlled vocabulary term is available. Null may be used to indicate that a column has a human readable name but does not map to a controlled vocabulary term")
	private String accession = null;

	@JsonProperty("unit")
	private String unit = null;

	/**
	 * Whether this column is marks the presence/absence of a specific value-less controlled vocabulary term, true when the term is present in the row, false or null otherwise.
	 */
	@JsonProperty("term_marker")
	@JsonPropertyDescription("Whether this column is marks the presence/absence of a specific value-less controlled vocabulary term, true when the term is present in the row, false or null otherwise.")
	private Boolean termMarker = false;

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getPath() {

		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public String getAccession() {

		return accession;
	}

	public void setAccession(String accession) {

		this.accession = accession;
	}

	public String getUnit() {

		return unit;
	}

	public void setUnit(String unit) {

		this.unit = unit;
	}

	public Boolean getTermMarker() {

		return termMarker;
	}

	public void setTermMarker(Boolean termMarker) {

		this.termMarker = termMarker;
	}
}
