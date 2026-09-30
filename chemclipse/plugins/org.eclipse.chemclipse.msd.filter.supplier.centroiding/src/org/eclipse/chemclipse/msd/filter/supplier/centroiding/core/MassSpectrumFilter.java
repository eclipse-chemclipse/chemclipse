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
package org.eclipse.chemclipse.msd.filter.supplier.centroiding.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.filter.supplier.centroiding.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final String CENTROIDING = "Centroiding";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(filterSettings instanceof MassSpectrumFilterSettings massSpectrumFilterSettings) {
			for(IScanMSD massSpectrum : massSpectra) {
				processingInfo.addMessages(centroidisation(massSpectrum, massSpectrumFilterSettings));
			}
		}
		return processingInfo;
	}

	private IProcessingInfo<IMassSpectrumFilterResult> centroidisation(IScanMSD scanMSD, MassSpectrumFilterSettings massSpectrumFilterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(scanMSD, massSpectrumFilterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(scanMSD instanceof IStandaloneMassSpectrum massSpectrum) {
			processingInfo = validate(massSpectrum);
			if(processingInfo.hasErrorMessages()) {
				return processingInfo;
			}
			massSpectrum.removeAllIons();
			for(IMassSpectrumPeak massSpectrumPeak : massSpectrum.getPeaks()) {
				IIon ion = new Ion(massSpectrumPeak.getIon(), (float)massSpectrumPeak.getAbundance());
				massSpectrum.addIon(ion);
			}
			massSpectrum.getPeaks().clear();
			massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
			massSpectrum.setDirty(true);
			processingInfo.setProcessingResult(new MassSpectrumFilterResult(ResultStatus.OK, "The mass spectrum was centroided successfully."));
		} else {
			processingInfo.addErrorMessage(CENTROIDING, "Not a standalone mass spectrum.");
		}

		return processingInfo;
	}

	private IProcessingInfo<IMassSpectrumFilterResult> validate(IStandaloneMassSpectrum standaloneMassSpectrum) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		if(standaloneMassSpectrum.getMassSpectrumType() != MassSpectrumType.PROFILE) {
			processingInfo.addErrorMessage(CENTROIDING, "Not a profile mass spectrum.");
		}
		if(standaloneMassSpectrum.getPeaks().isEmpty()) {
			processingInfo.addErrorMessage(CENTROIDING, "There are no peaks stored.");
		}
		return processingInfo;
	}
}
