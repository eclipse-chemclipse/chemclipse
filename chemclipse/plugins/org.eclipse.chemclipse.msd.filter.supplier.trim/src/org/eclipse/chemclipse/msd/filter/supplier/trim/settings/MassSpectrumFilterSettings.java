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
package org.eclipse.chemclipse.msd.filter.supplier.trim.settings;

import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MassSpectrumFilterSettings extends AbstractMassSpectrumFilterSettings {

	@JsonProperty(value = "Start [m/z]", defaultValue = "2000")
	@IntSettingsProperty(minValue = 0)
	private int start = 2000;

	@JsonProperty(value = "Stop [m/z]", defaultValue = "12000")
	@IntSettingsProperty(minValue = 0)
	private int stop = 12000;

	public int getStart() {

		return start;
	}

	public int getStop() {

		return stop;
	}

	@Override
	public List<MassSpectrumType> appliesToMassSpectrumTypes() {

		return Arrays.asList(MassSpectrumType.PROFILE);
	}
}
