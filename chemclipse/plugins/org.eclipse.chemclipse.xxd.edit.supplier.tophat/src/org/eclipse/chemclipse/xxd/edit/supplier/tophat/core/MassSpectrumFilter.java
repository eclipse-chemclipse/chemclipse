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
package org.eclipse.chemclipse.xxd.edit.supplier.tophat.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.MassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.xxd.edit.supplier.tophat.settings.MassSpectrumFilterSettings;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final Logger logger = Logger.getLogger(MassSpectrumFilter.class);
	private static final String DESCRIPTION = "TopHat Filter Mass Spectra";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		MassSpectrumFilterSettings massSpectrumFilterSettings;
		if(filterSettings instanceof MassSpectrumFilterSettings settings) {
			massSpectrumFilterSettings = settings;
		} else {
			massSpectrumFilterSettings = new MassSpectrumFilterSettings();
		}
		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(!processingInfo.hasErrorMessages()) {
			for(IScanMSD massSpectrum : massSpectra) {
				double[] abundances = massSpectrum.getIons().stream().mapToDouble(IIon::getAbundance).toArray();

				int halfWindowSize = massSpectrumFilterSettings.getHalfWindowSize();
				if(halfWindowSize < 1) {
					halfWindowSize = TopHat.optimizeHalfWindowSize(abundances);
					logger.info("Estimated half window size: " + halfWindowSize);
				}

				double[] baseline = TopHat.baseline(abundances, halfWindowSize);

				// subtract
				int i = 0;
				for(IIon ion : massSpectrum.getIons()) {
					ion.setAbundance(ion.getAbundance() - (float)baseline[i]);
					i++;
				}
			}

			processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, "The mass spectrum has been optimized successfully."));
			IMassSpectrumFilterResult massSpectrumFilterResult = new MassSpectrumFilterResult(ResultStatus.OK, "The Top-Hat filter has been applied successfully.");
			processingInfo.setProcessingResult(massSpectrumFilterResult);
		}

		return processingInfo;
	}
}