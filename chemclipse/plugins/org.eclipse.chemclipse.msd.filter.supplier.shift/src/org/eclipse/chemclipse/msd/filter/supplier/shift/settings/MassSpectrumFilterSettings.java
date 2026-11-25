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
package org.eclipse.chemclipse.msd.filter.supplier.shift.settings;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.support.settings.DoubleSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class MassSpectrumFilterSettings extends AbstractMassSpectrumFilterSettings {

	@JsonProperty(value = "Ion Offset [Da]", defaultValue = "0.0")
	@JsonPropertyDescription(value = "Added to every ion and peak m/z, including the product ions of tandem MS. Use a negative value to shift towards lower masses.")
	@DoubleSettingsProperty(step = 0.001d)
	private double ionOffset = 0.0d;

	@JsonProperty(value = "Precursor Offset [Da]", defaultValue = "0.0")
	@JsonPropertyDescription(value = "Added to the precursor m/z of tandem MS scans. Leave at zero to keep the precursor unchanged.")
	@DoubleSettingsProperty(step = 0.001d)
	private double precursorOffset = 0.0d;

	public double getIonOffset() {

		return ionOffset;
	}

	public void setIonOffset(double ionOffset) {

		this.ionOffset = ionOffset;
	}

	public double getPrecursorOffset() {

		return precursorOffset;
	}

	public void setPrecursorOffset(double precursorOffset) {

		this.precursorOffset = precursorOffset;
	}
}
