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
package org.eclipse.chemclipse.xxd.edit.supplier.tophat.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.settings.AbstractBaselineDetectorSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class BaselineDetectorSettings extends AbstractBaselineDetectorSettings {

	private static final Logger logger = Logger.getLogger(BaselineDetectorSettings.class);

	@JsonProperty(value = "Half Window Size", defaultValue = "")
	@JsonPropertyDescription(value = "Leave empty to estimate best value.")
	private int halfWindowSize = 0;

	public int getHalfWindowSize() {

		return halfWindowSize;
	}

	public void setHalfWindowSize(int halfWindowSize) {

		this.halfWindowSize = halfWindowSize;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		List<LiteratureReference> literatureReferences = new ArrayList<>();
		literatureReferences.add(createLiteratureReference("csp_64_.ris", "10.1366/000370210791414281"));
		return literatureReferences;
	}

	private static LiteratureReference createLiteratureReference(String file, String doi) {

		String content;
		try {
			content = new String(BaselineDetectorSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}