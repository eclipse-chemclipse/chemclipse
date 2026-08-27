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
package org.eclipse.chemclipse.xxd.edit.supplier.convexhull.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.support.literature.LiteratureReference;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class MassSpectrumFilterSettings extends AbstractMassSpectrumFilterSettings {

	private static final Logger logger = Logger.getLogger(MassSpectrumFilterSettings.class);

	@JsonProperty(value = "Tolerance", defaultValue = "1e-10")
	@JsonPropertyDescription(value = "below which points are considered identical")
	private double tolerance = 1e-10; // AbstractConvexHullGenerator2D.DEFAULT_TOLERANCE

	public double getTolerance() {

		return tolerance;
	}

	public void setTolerance(double tolerance) {

		this.tolerance = tolerance;
	}

	@Override
	public List<MassSpectrumType> appliesToMassSpectrumTypes() {

		return Arrays.asList(MassSpectrumType.PROFILE);
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		List<LiteratureReference> literatureReferences = new ArrayList<>();
		literatureReferences.add(createLiteratureReference("0020019079900723.ris", "10.1016/0020-0190(79)90072-3"));
		return literatureReferences;
	}

	private static LiteratureReference createLiteratureReference(String file, String doi) {

		String content;
		try {
			content = new String(MassSpectrumFilterSettings.class.getResourceAsStream(file).readAllBytes());
		} catch(Exception e) {
			content = doi;
			logger.warn(e);
		}
		return new LiteratureReference(content);
	}
}