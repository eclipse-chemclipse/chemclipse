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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * mzPeak array index
 * <p>
 * Describes the data arrays of a signal data file. It is stored in the Parquet key-value metadata
 * under "&lt;entity_type&gt;_array_index", as drawn from
 * https://raw.githubusercontent.com/HUPO-PSI/mzPeak-specification/refs/heads/main/schema/array_index.json
 */
@JsonPropertyOrder({"prefix", "entries"})
public class ArrayIndex {

	/**
	 * The name of the top-level group node of the layout, e.g. "point" or "chunk"
	 */
	@JsonProperty(value = "prefix", required = true)
	@JsonPropertyDescription("The name of the top-level group node of the layout")
	private String prefix;
	/**
	 * The arrays stored in the file
	 */
	@JsonProperty(value = "entries", required = true)
	@JsonPropertyDescription("The arrays stored in the file")
	private List<ArrayIndexEntry> entries = new ArrayList<>();

	public String getPrefix() {

		return prefix;
	}

	public void setPrefix(String prefix) {

		this.prefix = prefix;
	}

	public List<ArrayIndexEntry> getEntries() {

		return entries;
	}

	public void setEntries(List<ArrayIndexEntry> entries) {

		this.entries = entries;
	}
}
