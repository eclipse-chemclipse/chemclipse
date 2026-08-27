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
 * A single file in the mzPeak archive of a certain type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "entity_type", "data_kind", "column_mapping", "parameters"})
public class File {

	/**
	 * The name of the file, relative to the root of the archive
	 */
	@JsonProperty(value = "name", required = true)
	@JsonPropertyDescription("The name of the file, relative to the root of the archive")
	private String name;

	/**
	 * The things being described in one facet or another by this file Controlled values are spelled with underscores, not spaces: `wavelength_spectrum`. A reader encountering a value outside the controlled list treats it as `other`, so this is intentionally not a closed `enum`.
	 */
	@JsonProperty(value = "entity_type", required = true)
	@JsonPropertyDescription("The things being described in one facet or another by this file")
	private String entityType;

	/**
	 * The facet of the thing being described in this file Controlled values are spelled with underscores, not spaces: `data_arrays`, `selected_ions`. A reader encountering a value outside the controlled list treats it as `other`, so this is intentionally not a closed `enum`.
	 */
	@JsonProperty(value = "data_kind", required = true)
	@JsonPropertyDescription("The facet of the thing being described in this file")
	private String dataKind;

	/**
	 * A list of Parquet column to controlled vocabulary term mappings
	 */
	@JsonProperty("column_mapping")
	@JsonPropertyDescription("A list of Parquet column to controlled vocabulary term mappings")
	private List<ColumnMapping> columnMapping = new ArrayList<ColumnMapping>();

	/**
	 * A list of parameters describing the file stored in the mzPeak archive itself
	 */
	@JsonProperty("parameters")
	@JsonPropertyDescription("A list of parameters describing the file stored in the mzPeak archive itself")
	private List<Param> parameters = new ArrayList<Param>();

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getEntityType() {

		return entityType;
	}

	public void setEntityType(String entityType) {

		this.entityType = entityType;
	}

	public String getDataKind() {

		return dataKind;
	}

	public void setDataKind(String dataKind) {

		this.dataKind = dataKind;
	}

	public List<ColumnMapping> getColumnMapping() {

		return columnMapping;
	}

	public void setColumnMapping(List<ColumnMapping> columnMapping) {

		this.columnMapping = columnMapping;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
