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
package org.eclipse.chemclipse.msd.calculator.noise.mad;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.numeric.statistics.Calculations;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class MedianAbsoluteDeviationFilter extends AbstractMassSpectrumFilter {

	// 1.0 / NormalDistribution.of(0, 1).inverseCumulativeProbability(0.75);
	private static final double NORMAL_CONSISTENCY_FACTOR = 1.482602218505602;

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings settings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, settings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		for(IScanMSD massSpectrum : massSpectra) {
			if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				estimateNoise(standaloneMassSpectrum, monitor);
			}
		}
		return processingInfo;
	}

	private void estimateNoise(IStandaloneMassSpectrum standaloneMassSpectrum, IProgressMonitor monitor) {

		standaloneMassSpectrum.getNoise().clear();

		double[] y = standaloneMassSpectrum.getIons().stream().mapToDouble(IIon::getAbundance).toArray();
		if(y.length == 0) {
			standaloneMassSpectrum.getNoise().clear();
			return;
		}

		double center = Calculations.getMedian(y);

		monitor.beginTask("Calculate noise", y.length);
		double[] deviations = new double[y.length];
		for(int i = 0; i < y.length; i++) {
			deviations[i] = Math.abs(y[i] - center);
			monitor.worked(1);
		}

		double noise = NORMAL_CONSISTENCY_FACTOR * Calculations.getMedian(deviations);

		standaloneMassSpectrum.getNoise().clear();
		for(int i = 0; i < standaloneMassSpectrum.getNumberOfIons(); i++) {
			standaloneMassSpectrum.getNoise().add(noise);
		}
	}
}