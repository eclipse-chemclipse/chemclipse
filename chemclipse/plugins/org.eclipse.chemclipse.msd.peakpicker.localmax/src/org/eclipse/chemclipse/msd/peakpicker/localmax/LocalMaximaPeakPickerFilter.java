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
package org.eclipse.chemclipse.msd.peakpicker.localmax;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum.AbstractMassSpectrumFilter;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.peakpicker.localmax.settings.LocalMaximaPeakPickerFilterSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;

public class LocalMaximaPeakPickerFilter extends AbstractMassSpectrumFilter {

	private static final String DESCRIPTION = "Local Maxima Peak Picker";

	@Override
	public IProcessingInfo<IMassSpectrumFilterResult> applyFilter(List<IScanMSD> massSpectra, IMassSpectrumFilterSettings settings, IProgressMonitor monitor) {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = validate(massSpectra, settings);
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}

		if(settings instanceof LocalMaximaPeakPickerFilterSettings localMaximaSettings) {
			for(IScanMSD massSpectrum : massSpectra) {
				if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					if(standaloneMassSpectrum.getNoise().isEmpty()) {
						String message = "Noise is missing.";
						String solution = "Estimate noise first.";
						processingInfo.addMessage(new ProcessingMessage(MessageType.ERROR, DESCRIPTION, message, solution));
						continue;
					}
					int pickedPeaks = pickPeaks(standaloneMassSpectrum, localMaximaSettings, monitor);
					String message = "The local maxima peak picker has detected " + pickedPeaks + " peaks.";
					processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, message));
				}
			}
		}

		return processingInfo;
	}

	private int pickPeaks(IStandaloneMassSpectrum massSpectrum, LocalMaximaPeakPickerFilterSettings settings, IProgressMonitor monitor) {

		List<IIon> ions = massSpectrum.getIons();
		double[] intensities = ions.stream().mapToDouble(IIon::getAbundance).toArray();
		boolean[] maxima = detectLocalMaxima(intensities, Math.max(1, settings.getHalfWindowSize()), monitor);

		int pickedPeaks = 0;

		for(int i = 0; i < ions.size(); i++) {
			if(monitor.isCanceled()) {
				break;
			}
			if(maxima[i]) {
				IIon ion = ions.get(i);
				double noise = massSpectrum.getNoise().get(i);
				if(noise > 0.0d && ion.getAbundance() > settings.getSignalToNoiseRatio() * noise) {
					massSpectrum.getPeaks().add(createPeak(ion, noise));
					pickedPeaks++;
				}
			}
		}

		return pickedPeaks;
	}

	// 1D maximum filter test in neighborhood
	private static boolean[] detectLocalMaxima(double[] intensity, int halfWindowSize, IProgressMonitor monitor) {

		int n = intensity.length;
		boolean[] maxima = new boolean[n];

		for(int i = 0; i < n; i++) {
			if(monitor.isCanceled()) {
				break;
			}

			double center = intensity[i];
			maxima[i] = true;

			for(int j = i - halfWindowSize; j <= i + halfWindowSize; j++) {
				double value = 0.0d;

				if(j >= 0 && j < n) {
					value = intensity[j];
				}

				if(value > center) {
					maxima[i] = false;
					break;
				}
			}
		}

		return maxima;
	}

	private static IMassSpectrumPeak createPeak(IIon ion, double noise) {

		IMassSpectrumPeak peak = new MassSpectrumPeak();
		peak.setIon(ion.getIon());
		float intensity = ion.getAbundance();
		peak.setAbundance(intensity);
		peak.setSignalToNoise(intensity / noise);
		return peak;
	}
}