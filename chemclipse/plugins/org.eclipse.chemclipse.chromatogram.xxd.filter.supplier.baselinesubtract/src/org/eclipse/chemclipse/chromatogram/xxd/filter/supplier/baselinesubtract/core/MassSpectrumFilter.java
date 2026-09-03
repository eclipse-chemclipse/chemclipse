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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.baselinesubtract.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final String DESCRIPTION = "Baseline Subtract";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(!processingInfo.hasErrorMessages()) {
			for(IScanMSD scanMSD : massSpectra) {
				if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {

					if(standaloneMassSpectrum.getBaseline().isEmpty()) {
						processingInfo.addMessage(new ProcessingMessage(MessageType.ERROR, DESCRIPTION, "No baseline to subtract.", "Detect baseline first."));
						return processingInfo;
					}

					int i = 0;
					for(IIon ion : scanMSD.getIons()) {
						ion.setAbundance(ion.getAbundance() - standaloneMassSpectrum.getBaseline().get(i).floatValue());
						i++;
					}

					standaloneMassSpectrum.getBaseline().clear();

					processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, "Baseline subtracted."));
					IMassSpectrumFilterResult massSpectrumFilterResult = new MassSpectrumFilterResult(ResultStatus.OK, "Baseline subtracted.");
					processingInfo.setProcessingResult(massSpectrumFilterResult);
				}
			}
		}

		return processingInfo;
	}
}