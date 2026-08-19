/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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

public class WebIdentifierSettings extends AbstractIdentifierSettingsWSD implements IChromatogramIdentifierSettings {

	private static final Logger logger = Logger.getLogger(WebIdentifierSettings.class);

	@JsonProperty(value = "Endpoint", defaultValue = "https://blast.ncbi.nlm.nih.gov/blast/Blast.cgi")
	@JsonPropertyDescription("Select the web server to query against.")
	private String endpoint = "https://blast.ncbi.nlm.nih.gov/blast/Blast.cgi";

	@JsonProperty(value = "Database", defaultValue = "nt")
	@JsonPropertyDescription(value = "Select the database to search.")
	private String database = "nt";

	@JsonProperty(value = "Task", defaultValue = "megablast")
	private Task task = Task.MEGABLAST;

	public String getEndpoint() {

		return endpoint;
	}

	public void setEndPoint(String endpoint) {

		this.endpoint = endpoint;
	}

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
			content = new String(WebIdentifierSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}
