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
package org.eclipse.chemclipse.msd.filter.supplier.trim.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.filter.supplier.trim.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final String DESCRIPTION = "Cutting";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(filterSettings instanceof MassSpectrumFilterSettings massSpectrumFilterSettings) {
			for(IScanMSD massSpectrum : massSpectra) {
				processingInfo.addMessages(trim(massSpectrum, massSpectrumFilterSettings));
			}
		}
		return processingInfo;
	}

	private IProcessingInfo<IMassSpectrumFilterResult> trim(IScanMSD scanMSD, MassSpectrumFilterSettings filterSettings) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(scanMSD, filterSettings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		if(scanMSD instanceof IRegularMassSpectrum massSpectrum) {
			processingInfo = validate(massSpectrum);
			if(processingInfo.hasErrorMessages()) {
				return processingInfo;
			}
			List<IIon> toRemoveIon = new ArrayList<>();
			for(IIon ion : scanMSD.getIons()) {
				if(ion.getIon() < filterSettings.getStart()) {
					toRemoveIon.add(ion);
				}
				if(ion.getIon() > filterSettings.getStop()) {
					toRemoveIon.add(ion);
				}
			}
			for(IIon ion : toRemoveIon) {
				scanMSD.removeIon(ion);
			}
			if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				List<IMassSpectrumPeak> toRemovePeaks = new ArrayList<>();
				for(IMassSpectrumPeak peak : standaloneMassSpectrum.getPeaks()) {
					if(peak.getIon() < filterSettings.getStart()) {
						toRemovePeaks.add(peak);
					}
					if(peak.getIon() > filterSettings.getStop()) {
						toRemovePeaks.add(peak);
					}
				}
				standaloneMassSpectrum.getPeaks().removeAll(toRemovePeaks);
			}
			processingInfo.setProcessingResult(new MassSpectrumFilterResult(ResultStatus.OK, "The mass spectrum was trimmed."));
		} else {
			processingInfo.addErrorMessage(DESCRIPTION, "Not a standalone mass spectrum.");
		}

		return processingInfo;
	}

	private IProcessingInfo<IMassSpectrumFilterResult> validate(IRegularMassSpectrum regularMassSpectrum) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = new ProcessingInfo<>();
		if(regularMassSpectrum.getMassSpectrumType() != MassSpectrumType.PROFILE) {
			processingInfo.addErrorMessage(DESCRIPTION, "Not a profile mass spectrum.");
		}
		return processingInfo;
	}
}
