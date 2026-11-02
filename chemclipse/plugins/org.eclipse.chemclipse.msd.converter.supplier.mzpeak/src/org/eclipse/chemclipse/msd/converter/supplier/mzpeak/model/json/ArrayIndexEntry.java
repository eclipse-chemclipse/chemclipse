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
 * A single data array of an mzPeak array index. Absent values are written as null, as the reference
 * implementation does.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"context", "path", "data_type", "array_type", "array_name", "unit", "buffer_format", "transform", "data_processing_id", "buffer_priority", "sorting_rank"})
public class ArrayIndexEntry {

	/**
	 * The entity type the array describes, e.g. "spectrum" or "chromatogram"
	 */
	@JsonProperty(value = "context", required = true)
	@JsonPropertyDescription("The entity type the array describes")
	private String context;
	/**
	 * The column path in the Parquet schema
	 */
	@JsonProperty(value = "path", required = true)
	@JsonPropertyDescription("The column path in the Parquet schema")
	private String path;
	/**
	 * The CURIE of the data type of the array, e.g. MS:1000523 for 64-bit float
	 */
	@JsonProperty(value = "data_type", required = true)
	@JsonPropertyDescription("The CURIE of the data type of the array")
	private String dataType;
	/**
	 * The CURIE of the array type, e.g. MS:1000514 for m/z array
	 */
	@JsonProperty(value = "array_type", required = true)
	@JsonPropertyDescription("The CURIE of the array type")
	private String arrayType;
	/**
	 * The human readable name of the array
	 */
	@JsonProperty(value = "array_name", required = true)
	@JsonPropertyDescription("The human readable name of the array")
	private String arrayName;
	/**
	 * The CURIE of the unit of the values, null if they have none
	 */
	@JsonProperty(value = "unit", required = true)
	@JsonPropertyDescription("The CURIE of the unit of the values")
	private String unit;
	/**
	 * How the array is stored in the layout, e.g. "index" or "point"
	 */
	@JsonProperty(value = "buffer_format", required = true)
	@JsonPropertyDescription("How the array is stored in the layout")
	private String bufferFormat;
	/**
	 * The CURIE of a transformation applied to the values, null if none was
	 */
	@JsonProperty("transform")
	@JsonPropertyDescription("The CURIE of a transformation applied to the values")
	private String transform;
	/**
	 * The data processing governing this array, null for the default one of the run
	 */
	@JsonProperty("data_processing_id")
	@JsonPropertyDescription("The data processing governing this array")
	private String dataProcessingId;
	/**
	 * Either "primary" or "secondary"
	 */
	@JsonProperty("buffer_priority")
	@JsonPropertyDescription("Either primary or secondary")
	private String bufferPriority;
	/**
	 * The rank in which the values are sorted within an entry, null if they impose no order
	 */
	@JsonProperty("sorting_rank")
	@JsonPropertyDescription("The rank in which the values are sorted within an entry")
	private Integer sortingRank;

	public String getContext() {

		return context;
	}

	public void setContext(String context) {

		this.context = context;
	}

	public String getPath() {

		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public String getDataType() {

		return dataType;
	}

	public void setDataType(String dataType) {

		this.dataType = dataType;
	}

	public String getArrayType() {

		return arrayType;
	}

	public void setArrayType(String arrayType) {

		this.arrayType = arrayType;
	}

	public String getArrayName() {

		return arrayName;
	}

	public void setArrayName(String arrayName) {

		this.arrayName = arrayName;
	}

	public String getUnit() {

		return unit;
	}

	public void setUnit(String unit) {

		this.unit = unit;
	}

	public String getBufferFormat() {

		return bufferFormat;
	}

	public void setBufferFormat(String bufferFormat) {

		this.bufferFormat = bufferFormat;
	}

	public String getTransform() {

		return transform;
	}

	public void setTransform(String transform) {

		this.transform = transform;
	}

	public String getDataProcessingId() {

		return dataProcessingId;
	}

	public void setDataProcessingId(String dataProcessingId) {

		this.dataProcessingId = dataProcessingId;
	}

	public String getBufferPriority() {

		return bufferPriority;
	}

	public void setBufferPriority(String bufferPriority) {

		this.bufferPriority = bufferPriority;
	}

	public Integer getSortingRank() {

		return sortingRank;
	}

	public void setSortingRank(Integer sortingRank) {

		this.sortingRank = sortingRank;
	}
}
