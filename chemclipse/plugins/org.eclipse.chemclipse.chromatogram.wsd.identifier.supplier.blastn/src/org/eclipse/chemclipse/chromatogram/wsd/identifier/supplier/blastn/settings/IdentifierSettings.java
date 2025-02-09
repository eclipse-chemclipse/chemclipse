/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.AbstractIdentifierSettingsWSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class IdentifierSettings extends AbstractIdentifierSettingsWSD implements IChromatogramIdentifierSettings {

	private static final Logger logger = Logger.getLogger(IdentifierSettings.class);

	@JsonProperty(value = "Database Folder", defaultValue = "")
	@JsonPropertyDescription("Select the located where update_blastdb downloaded the databases to.")
	@FileSettingProperty(dialogType = DialogType.OPEN_DIALOG, onlyDirectory = true)
	private File databaseFolder = null;

	@JsonProperty(value = "Database", defaultValue = "16S_ribosomal_RNA")
	@JsonPropertyDescription(value = "Select the database to query against.")
	private String database = "16S_ribosomal_RNA";

	public File getDatabaseFolder() {

		return databaseFolder;
	}

	public void setDatabaseFolder(File databaseFolder) {

		this.databaseFolder = databaseFolder;
	}

	public String getDatabase() {

		return database;
	}

	public void setDatabase(String database) {

		this.database = database;
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
			content = new String(IdentifierSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}
