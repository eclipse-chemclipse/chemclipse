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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings;

import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class NominalizeFilterSettings extends AbstractMassSpectrumFilterSettings {

	@JsonProperty(value = "Preserve TandemMS", defaultValue = "false")
	@JsonPropertyDescription("When applying the nominalization, perserve the tandem MS information.")
	private boolean preserveTandemMS = false;

	public boolean isPreserveTandemMS() {

		return preserveTandemMS;
	}

	public void setPreserveTandemMS(boolean preserveTandemMS) {

		this.preserveTandemMS = preserveTandemMS;
	}

	@Override
	public List<MassSpectrumType> appliesToMassSpectrumTypes() {

		return Arrays.asList(MassSpectrumType.CENTROID);
	}
}