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
 * mzPeak metadata file description
 * <p>
 * Describe the JSON format of the file description section
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"contents", "source_files", "contacts"})
public class FileDescription {

	/**
	 * Parameters describing the contents of the file, such as types of spectra. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#fileContent
	 */
	@JsonProperty(value = "contents", required = true)
	@JsonPropertyDescription("Parameters describing the contents of the file, such as types of spectra. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#fileContent")
	private List<Param> contents = new ArrayList<Param>();

	/**
	 * List of all files used as data sources for this mzPeak file. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#sourceFileList
	 */
	@JsonProperty(value = "source_files", required = true)
	@JsonPropertyDescription("List of all files used as data sources for this mzPeak file. Analogous to https://peptideatlas.org/tmp/mzML1.1.0.html#sourceFileList")
	private List<SourceFile> sourceFiles = new ArrayList<SourceFile>();

	/**
	 * Persons or entities responsible for the data contained here.
	 */
	@JsonProperty("contacts")
	@JsonPropertyDescription("Persons or entities responsible for the data contained here.")
	private List<Contact> contacts = new ArrayList<Contact>();

	public List<Param> getContents() {

		return contents;
	}

	public void setContents(List<Param> contents) {

		this.contents = contents;
	}

	public List<SourceFile> getSourceFiles() {

		return sourceFiles;
	}

	public void setSourceFiles(List<SourceFile> sourceFiles) {

		this.sourceFiles = sourceFiles;
	}

	public List<Contact> getContacts() {

		return contacts;
	}

	public void setContacts(List<Contact> contacts) {

		this.contacts = contacts;
	}
}
