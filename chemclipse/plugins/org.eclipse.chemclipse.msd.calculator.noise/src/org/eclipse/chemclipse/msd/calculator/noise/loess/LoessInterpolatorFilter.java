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
package org.eclipse.chemclipse.msd.calculator.noise.loess;

import java.util.List;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class LoessInterpolatorFilter extends AbstractMassSpectrumFilter {

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

		int numberIons = standaloneMassSpectrum.getNumberOfIons();
		double[] x = new double[numberIons];
		double[] y = new double[numberIons];

		monitor.beginTask("Copy ions", numberIons);
		for(int i = 0; i < numberIons; i++) {
			IIon ion = standaloneMassSpectrum.getIons().get(i);
			x[i] = ion.getIon();
			y[i] = ion.getAbundance();
			monitor.worked(1);
		}

		LoessInterpolator loessInterpolator = new LoessInterpolator();
		monitor.beginTask("Smooth", IProgressMonitor.UNKNOWN);
		double[] smoothed = loessInterpolator.smooth(x, y);
		for(double noise : smoothed) {
			standaloneMassSpectrum.getNoise().add(noise);
		}
	}
}
