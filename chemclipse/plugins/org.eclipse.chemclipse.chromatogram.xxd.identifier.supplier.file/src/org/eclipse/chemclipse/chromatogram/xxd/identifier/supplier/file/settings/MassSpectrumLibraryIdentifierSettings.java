/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - change to ComboSettingsProperty(MassSpectrumComparatorDynamicSettingProperty.class)
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.settings;

import java.io.File;
import java.util.StringJoiner;

import org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.file.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.identifier.settings.AbstractMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.util.FileListUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class MassSpectrumLibraryIdentifierSettings extends AbstractMassSpectrumIdentifierSettings implements ILibraryIdentifierSettings {

	@JsonProperty(value = "Library File", defaultValue = "")
	@JsonPropertyDescription("Select the library file.")
	@FileSettingProperty(dialogType = DialogType.OPEN_DIALOG, onlyDirectory = false, allowEmpty = false)
	private File libraryFile;

	@JsonProperty(value = "Pre-Optimization", defaultValue = "false")
	private boolean usePreOptimization = false;

	@JsonProperty(value = "Threshold Pre-Optimization", defaultValue = "0.12")
	@DoubleSettingsProperty(minValue = PreferenceSupplier.MIN_THRESHOLD_PRE_OPTIMIZATION, maxValue = PreferenceSupplier.MAX_THRESHOLD_PRE_OPTIMIZATION, step = 0.1)
	private double thresholdPreOptimization = 0.12;

	@JsonProperty(value = "Number of Targets", defaultValue = "15")
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_NUMBER_OF_TARGETS, maxValue = PreferenceSupplier.MAX_NUMBER_OF_TARGETS)
	private int numberOfTargets = 15;

	@JsonProperty(value = "Min. Match Factor", defaultValue = "80.0")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_FACTOR, maxValue = PreferenceSupplier.MAX_FACTOR)
	private float minMatchFactor = 80.0f;

	@JsonProperty(value = "Min. Reverse Match Factor", defaultValue = "80.0")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_FACTOR, maxValue = PreferenceSupplier.MAX_FACTOR)
	private float minReverseMatchFactor = 80.0f;

	@JsonIgnore
	private String alternateIdentifierId = "";

	@JsonIgnore
	private String massSpectraFiles = "";

	@Override
	public String getMassSpectraFiles() {

		if(libraryFile != null) {
			if(libraryFile.isDirectory()) {
				return getAllContainingFilesAbsolutePath(libraryFile);
			}
			return libraryFile.getAbsolutePath();
		} else {
			return massSpectraFiles;
		}
	}

	private static String getAllContainingFilesAbsolutePath(File directory) {

		StringJoiner joiner = new StringJoiner(FileListUtil.SEPARATOR_TOKEN);
		File[] files = directory.listFiles();
		if(files != null) {
			for(File file : files) {
				if(file.isFile()) {
					joiner.add(file.getAbsolutePath());
				}
			}
		}
		return joiner.toString();
	}

	@Override
	public void setMassSpectraFiles(String massSpectraFiles) {

		this.massSpectraFiles = massSpectraFiles;
	}

	@Override
	public boolean isUsePreOptimization() {

		return usePreOptimization;
	}

	@Override
	public void setUsePreOptimization(boolean usePreOptimization) {

		this.usePreOptimization = usePreOptimization;
	}

	@Override
	public double getThresholdPreOptimization() {

		return thresholdPreOptimization;
	}

	@Override
	public void setThresholdPreOptimization(double thresholdPreOptimization) {

		this.thresholdPreOptimization = thresholdPreOptimization;
	}

	@Override
	public int getNumberOfTargets() {

		return numberOfTargets;
	}

	@Override
	public void setNumberOfTargets(int numberOfTargets) {

		this.numberOfTargets = numberOfTargets;
	}

	@Override
	public float getMinMatchFactor() {

		return minMatchFactor;
	}

	@Override
	public void setMinMatchFactor(float minMatchFactor) {

		this.minMatchFactor = minMatchFactor;
	}

	@Override
	public float getMinReverseMatchFactor() {

		return minReverseMatchFactor;
	}

	@Override
	public void setMinReverseMatchFactor(float minReverseMatchFactor) {

		this.minReverseMatchFactor = minReverseMatchFactor;
	}

	@Override
	public String getAlternateIdentifierId() {

		return alternateIdentifierId;
	}

	@Override
	public void setAlternateIdentifierId(String alternateIdentifierId) {

		this.alternateIdentifierId = alternateIdentifierId;
	}
}
