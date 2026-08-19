/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.AbstractIdentifierSettingsWSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class LocalIdentifierSettings extends AbstractIdentifierSettingsWSD implements IChromatogramIdentifierSettings {

	private static final Logger logger = Logger.getLogger(LocalIdentifierSettings.class);

	@JsonProperty(value = "Database", defaultValue = "16S_ribosomal_RNA")
	@JsonPropertyDescription(value = "Select the database to query against.")
	private String database = "16S_ribosomal_RNA";

	@JsonProperty(value = "Task", defaultValue = "blastn")
	private Task task = Task.BLASTN;

	public String getDatabase() {

		return database;
	}

	public void setDatabase(String database) {

		this.database = database;
	}

	public Task getTask() {

		return task;
	}

	public void setTask(Task task) {

		this.task = task;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		List<LiteratureReference> literatureReferences = new ArrayList<>();
		literatureReferences.add(createLiteratureReference("csp_7_203.ris", "10.1089/10665270050081478"));
		return literatureReferences;
	}

	private static LiteratureReference createLiteratureReference(String file, String doi) {

		String content;
		try {
			content = new String(LocalIdentifierSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}
