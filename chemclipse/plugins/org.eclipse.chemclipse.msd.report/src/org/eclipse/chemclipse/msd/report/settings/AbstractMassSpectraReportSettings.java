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
package org.eclipse.chemclipse.msd.report.settings;

import java.io.File;

import org.eclipse.chemclipse.model.settings.AbstractProcessSettings;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public abstract class AbstractMassSpectraReportSettings extends AbstractProcessSettings implements IMassSpectraReportSettings {

	@JsonProperty(value = "Export Folder", defaultValue = "")
	@FileSettingProperty(onlyDirectory = true, dialogType = DialogType.SAVE_DIALOG, allowEmpty = false)
	@JsonPropertyDescription("Set a specific folder or use the placeholder.\n" + //
			"Variables:\n" + //
			VARIABLE_CURRENT_DIRECTORY //
	)
	private File exportFolder;
	@JsonProperty(value = "Append", defaultValue = "false")
	private boolean append = false;
	@JsonProperty(value = "File Name", defaultValue = VARIABLE_MASSSPECTRUM_NAME + VARIABLE_EXTENSION)
	@JsonPropertyDescription("Set a specific name or use the variables or a combination.\n" + //
			"Variables:\n" + //
			VARIABLE_MASSSPECTRUM_NAME + "\n" + //
			VARIABLE_EXTENSION //
	)
	private String filenamePattern = VARIABLE_MASSSPECTRUM_NAME + VARIABLE_EXTENSION;

	@Override
	public File getExportFolder() {

		if(exportFolder == null) {
			String folder = getDefaultFolder();
			if(folder != null && !folder.isEmpty()) {
				return new File(folder);
			}
		}
		return exportFolder;
	}

	public void setExportFolder(File exportFolder) {

		this.exportFolder = exportFolder;
	}

	@Override
	public boolean isAppend() {

		return append;
	}

	@Override
	public String getFileNamePattern() {

		return filenamePattern;
	}

	protected abstract String getDefaultFolder();
}
