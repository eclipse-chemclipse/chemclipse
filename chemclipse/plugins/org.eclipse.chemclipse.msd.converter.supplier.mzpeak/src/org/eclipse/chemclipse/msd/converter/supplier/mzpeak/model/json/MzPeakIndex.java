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
 * mzPeak file index JSON
 * <p>
 * Describe the JSON format of the file index
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"files", "metadata"})
public class MzPeakIndex {

	/**
	 * The files described in the index
	 */
	@JsonProperty(value = "files", required = true)
	@JsonPropertyDescription("The files described in the index")
	private List<File> files = new ArrayList<File>();

	@JsonProperty(value = "metadata", required = true)
	private Metadata metadata;

	public List<File> getFiles() {

		return files;
	}

	public void setFiles(List<File> files) {

		this.files = files;
	}

	public Metadata getMetadata() {

		return metadata;
	}

	public void setMetadata(Metadata metadata) {

		this.metadata = metadata;
	}
}
