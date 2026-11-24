/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.ionremover.settings;

import org.eclipse.chemclipse.chromatogram.msd.filter.settings.AbstractMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.support.settings.StringSettingsProperty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class MassSpectrumFilterSettings extends AbstractMassSpectrumFilterSettings implements ITraceRemoverFilterSettings {

	@JsonProperty(value = "Ions", defaultValue = "18 28 84 207")
	@JsonPropertyDescription(value = "List the ions, separated by a white space.")
	@StringSettingsProperty(isMultiLine = false, allowEmpty = false)
	private String ionsToRemove = "18 28 84 207";
	@JsonProperty(value = "Mode", defaultValue = "INCLUDE")
	@JsonPropertyDescription(value = "Gives the mode to use (include = remove all ions given in the list, exclude = remove all ions not in the list)")
	private MarkedTraceModus markedTraceModus = MarkedTraceModus.INCLUDE;

	@Override
	public String getIonsToRemove() {

		return ionsToRemove;
	}

	@Override
	public void setIonsToRemove(String ionsToRemove) {

		this.ionsToRemove = ionsToRemove;
	}

	@Override
	public MarkedTraceModus getMarkedTraceModus() {

		return markedTraceModus;
	}

	@Override
	public void setMarkedTraceModus(MarkedTraceModus markedTraceModus) {

		this.markedTraceModus = markedTraceModus;
	}

	@Override
	public boolean isValidFor(IScanMSD scanMSD) {

		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			return regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID;
		}
		return false;
	}
}