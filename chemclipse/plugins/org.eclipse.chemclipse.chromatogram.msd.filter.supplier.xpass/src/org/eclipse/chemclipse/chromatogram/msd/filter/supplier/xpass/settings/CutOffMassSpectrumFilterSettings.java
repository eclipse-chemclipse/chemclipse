/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.support.literature.LiteratureReference;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CutOffMassSpectrumFilterSettings implements IMassSpectrumFilterSettings {

	@JsonProperty(value = "Threshold", defaultValue = "1000")
	@IntSettingsProperty(minValue = 0)
	private int threshold = 1000;

	public int getThreshold() {

		return threshold;
	}

	@Override
	public List<LiteratureReference> getLiteratureReferences() {

		return new ArrayList<>();
	}

	@Override
	public boolean isValidFor(IScanMSD scanMSD) {

		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			return regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID;
		}
		return false;
	}
}
