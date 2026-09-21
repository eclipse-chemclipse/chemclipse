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
 * A data file that was read in order to produce this mzPeak file. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#sourceFile
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "location", "parameters"})
public class SourceFile {

	/**
	 * A unique identifier for this source file.
	 */
	@JsonProperty(value = "id", required = true)
	@JsonPropertyDescription("A unique identifier for this source file.")
	private String id;

	/**
	 * The name of the source file, not including parent directory
	 */
	@JsonProperty(value = "name", required = true)
	@JsonPropertyDescription("The name of the source file, not including parent directory")
	private String name;

	/**
	 * The path to the source file, URI encoded. This may include file:// protocols and UNC paths
	 */
	@JsonProperty(value = "location", required = true)
	@JsonPropertyDescription("The path to the source file, URI encoded. This may include file:// protocols and UNC paths")
	private String location;

	/**
	 * Additional parameters describing this source file, like checksums, nativeID format, or file format
	 */
	@JsonProperty(value = "parameters", required = true)
	@JsonPropertyDescription("Additional parameters describing this source file, like checksums, nativeID format, or file format")
	private List<Param> parameters = new ArrayList<Param>();

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

	public String getLocation() {

		return location;
	}

	public void setLocation(String location) {

		this.location = location;
	}

	public List<Param> getParameters() {

		return parameters;
	}

	public void setParameters(List<Param> parameters) {

		this.parameters = parameters;
	}
}
