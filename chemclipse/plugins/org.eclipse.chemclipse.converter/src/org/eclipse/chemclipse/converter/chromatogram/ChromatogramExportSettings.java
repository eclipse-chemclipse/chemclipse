/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.chemclipse.support.settings.SystemSettings;
import org.eclipse.chemclipse.support.settings.SystemSettingsStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@SystemSettings(value = SystemSettingsStrategy.DYNAMIC, dynamicCheckMethod = "getSystemSettingsStrategy")
public class ChromatogramExportSettings extends AbstractProcessSettings implements IProcessSettings {

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

	/**
	 * Method that check if system settings are available, this is used in conjunction with the SystemSettings annotation but can also be called by user code.
	 * 
	 * @return
	 */
	public static SystemSettingsStrategy getSystemSettingsStrategy() {

		String folder = PreferenceSupplier.getChromatogramExportFolder();
		if(folder != null && !folder.isEmpty()) {
			return SystemSettingsStrategy.NEW_INSTANCE;
		} else {
			return SystemSettingsStrategy.NONE;
		}
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
