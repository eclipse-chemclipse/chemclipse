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
package org.eclipse.chemclipse.xxd.edit.supplier.asls.core;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.chemclipse.xxd.edit.supplier.alsl.settings.MassSpectrumFilterSettings;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumFilter extends AbstractMassSpectrumFilter {

	private static final String DESCRIPTION = "AsLS";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings filterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, filterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(filterSettings instanceof MassSpectrumFilterSettings settings) {
				calculateBaseline(massSpectra, settings);
				processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, "Baseline set."));
				IMassSpectrumFilterResult massSpectrumFilterResult = new MassSpectrumFilterResult(ResultStatus.OK, "The AsLS filter has been applied successfully.");
				processingInfo.setProcessingResult(massSpectrumFilterResult);
			}
		}
		return processingInfo;
	}

	private static void calculateBaseline(List<IScanMSD> massSpectra, MassSpectrumFilterSettings filterSettings) {

		for(IScanMSD scanMSD : massSpectra) {
			double[] abundances = scanMSD.getIons().stream().mapToDouble(IIon::getAbundance).toArray();
			double[] baseline = AsymmetricLeastSquares.baseline(abundances, //
					filterSettings.getAsymmetryFactor(), //
					filterSettings.getSmoothnessFactor(), //
					filterSettings.getIterations());
			if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				standaloneMassSpectrum.setBaseline(new ArrayList<>(Arrays.stream(baseline).boxed().toList()));
			}
		}
	}
}