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
package org.eclipse.chemclipse.xxd.edit.supplier.alsl.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.settings.AbstractBaselineDetectorSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaselineDetectorSettings extends AbstractBaselineDetectorSettings {

	private static final Logger logger = Logger.getLogger(BaselineDetectorSettings.class);

	@JsonProperty(value = "Asymmetry Factor", defaultValue = "0.000001")
	@DoubleSettingsProperty(minValue = 0, maxValue = 1)
	private double asymmetryFactor = 1E-6;

	@JsonProperty(value = "Smoothness Factor", defaultValue = "100000")
	@DoubleSettingsProperty(minValue = 0)
	private double smoothnessFactor = 1E5;

	@JsonProperty(value = "Iterations", defaultValue = "8")
	@IntSettingsProperty(minValue = 1)
	private int iterations = 8;

	public double getSmoothnessFactor() {

		return smoothnessFactor;
	}

	public double getAsymmetryFactor() {

		return asymmetryFactor;
	}

	public int getIterations() {

		return iterations;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		List<LiteratureReference> literatureReferences = new ArrayList<>();
		literatureReferences.add(createLiteratureReference("citation-228961729.ris", "https://prod-dcd-datasets-public-files-eu-west-1.s3.eu-west-1.amazonaws.com/dd7c1919-302c-4ba0-8f88-8aa61e86bb9d"));
		return literatureReferences;
	}

	private static LiteratureReference createLiteratureReference(String file, String url) {

		String content;
		try {
			content = new String(MassSpectrumFilterSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = url;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}