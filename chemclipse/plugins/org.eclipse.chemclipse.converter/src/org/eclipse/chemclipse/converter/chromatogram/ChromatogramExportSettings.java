/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring to dynamic export name
 *******************************************************************************/
package org.eclipse.chemclipse.converter.chromatogram;

import java.io.File;

import org.eclipse.chemclipse.converter.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.settings.AbstractProcessSettings;
import org.eclipse.chemclipse.model.settings.IProcessSettings;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ChromatogramExportSettings extends AbstractProcessSettings {

	@JsonProperty(value = "Export Folder", defaultValue = "")
	@FileSettingProperty(onlyDirectory = true, dialogType = DialogType.SAVE_DIALOG)
	@JsonPropertyDescription("Set an absolute folder or use the variables.\n" + //
			"Variables:\n" + //
			VARIABLE_CURRENT_DIRECTORY //
	)
	private File exportFolder;
	@JsonProperty(value = "File Name", defaultValue = VARIABLE_CHROMATOGRAM_NAME + VARIABLE_EXTENSION)
	@JsonPropertyDescription("Set a specific name or use the variables or a combination.\n" + //
			"Variables:\n" + //
			VARIABLE_CHROMATOGRAM_NAME + "\n" + //
			VARIABLE_CHROMATOGRAM_DATANAME + "\n" + //
			VARIABLE_CHROMATOGRAM_SAMPLEGROUP + "\n" + //
			VARIABLE_CHROMATOGRAM_SHORTINFO + "\n" + //
			VARIABLE_EXTENSION //
	)
	private String filenamePattern = VARIABLE_CHROMATOGRAM_NAME + VARIABLE_EXTENSION;

	public File getExportFolder() {

		if(exportFolder == null) {
			String folder = PreferenceSupplier.getChromatogramExportFolder();
			if(folder != null && !folder.isEmpty()) {
				return new File(folder);
			}
		}
		return exportFolder;
	}

	public void setExportFolder(File exportFolder) {

		this.exportFolder = exportFolder;
	}

	public String getFileNamePattern() {

		/*
		 * Check to enable backward compatibility
		 */
		if(filenamePattern == null) {
			return VARIABLE_CHROMATOGRAM_NAME + VARIABLE_EXTENSION;
		}
		return filenamePattern;
	}

	public void setFilenamePattern(String filenamePattern) {

		this.filenamePattern = filenamePattern;
	}

	@JsonIgnore
	public File getExportFile(String extension, IChromatogram chromatogram) {

		String exportPath = resolvedFolders(chromatogram);
		String fileName = getFileName(chromatogram, getFileNamePattern(), extension);
		return new File(exportPath, fileName);
	}

	private String resolvedFolders(IChromatogram chromatogram) {

		String exportPath = getExportFolder().getAbsolutePath();
		if(exportPath.contains(IProcessSettings.VARIABLE_CURRENT_DIRECTORY)) {
			exportPath = getCleanedFileValue(exportPath);
			exportPath = exportPath.replace(IProcessSettings.VARIABLE_CURRENT_DIRECTORY, chromatogram.getFile().getParent());
		}
		return exportPath;
	}
}