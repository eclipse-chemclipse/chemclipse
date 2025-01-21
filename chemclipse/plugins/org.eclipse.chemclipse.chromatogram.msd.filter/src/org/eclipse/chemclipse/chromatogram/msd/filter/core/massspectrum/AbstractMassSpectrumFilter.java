/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - add generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;

public abstract class AbstractMassSpectrumFilter implements IMassSpectrumFilter {

	private static final String DESCRIPTION = "Mass Spectrum Filter";

	public IProcessingInfo<IMassSpectrumFilterResult> validate(IScanMSD massSpectrum, IMassSpectrumFilterSettings massSpectrumFilterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validateMassSpectrum(massSpectrum));
		processingInfo.addMessages(validateFilterSettings(massSpectrumFilterSettings));
		return processingInfo;
	}

	public IProcessingInfo<IMassSpectrumFilterResult> validate(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings massSpectrumFilterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validateMassSpectra(massSpectra));
		processingInfo.addMessages(validateFilterSettings(massSpectrumFilterSettings));
		return processingInfo;
	}

	/**
	 * Validates the mass spectrum.
	 * 
	 * @param massSpectrum
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IMassSpectrumFilterResult> validateMassSpectrum(IScanMSD massSpectrum) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		if(massSpectrum == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The mass spectrum is not valid.");
		}
		return processingInfo;
	}

	/**
	 * Validates the mass spectra.
	 * 
	 * @param massSpectra
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IMassSpectrumFilterResult> validateMassSpectra(List<IScanMSD> massSpectra) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		if(massSpectra == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The mass spectrum list is not valid.");
		}
		return processingInfo;
	}

	/**
	 * Validates that the filter settings are not null.
	 * 
	 * @param massSpectrumFilterSettings
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IMassSpectrumFilterResult> validateFilterSettings(IMassSpectrumFilterSettings massSpectrumFilterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		if(massSpectrumFilterSettings == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The filter settings are not valid.");
		}
		return processingInfo;
	}
}
