/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.core.AbstractPeakQuantifier;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.core.IPeakQuantifier;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.settings.IPeakQuantifierSettings;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.internal.core.PeakQuantitationCalculatorISTD;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.settings.PeakQuantifierSettings;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class PeakQuantifierISTD extends AbstractPeakQuantifier implements IPeakQuantifier {

	private PeakQuantitationCalculatorISTD calculatorISTD = new PeakQuantitationCalculatorISTD();

	@Override
	public IProcessingInfo<?> quantify(List<IPeak> peaks, IPeakQuantifierSettings peakQuantifierSettings, IProgressMonitor monitor) {

		return calculatorISTD.quantify(peaks);
	}

	@Override
	public IProcessingInfo<?> quantify(IPeak peak, IPeakQuantifierSettings peakQuantifierSettings, IProgressMonitor monitor) {

		List<IPeak> peaks = new ArrayList<>();
		peaks.add(peak);
		return quantify(peaks, peakQuantifierSettings, monitor);
	}

	@Override
	public IProcessingInfo<?> quantify(IPeak peak, IProgressMonitor monitor) {

		List<IPeak> peaks = new ArrayList<>();
		peaks.add(peak);
		PeakQuantifierSettings peakQuantifierSettings = PreferenceSupplier.getPeakQuantifierSettings();
		return quantify(peaks, peakQuantifierSettings, monitor);
	}

	@Override
	public IProcessingInfo<?> quantify(List<IPeak> peaks, IProgressMonitor monitor) {

		PeakQuantifierSettings peakQuantifierSettings = PreferenceSupplier.getPeakQuantifierSettings();
		return quantify(peaks, peakQuantifierSettings, monitor);
	}

	public IProcessingInfo<?> quantifySelectedPeak(IChromatogramSelection chromatogramSelection) {

		return calculatorISTD.quantifySelectedPeak(chromatogramSelection);
	}

	public IProcessingInfo<?> quantifyAllPeaks(IChromatogramSelection chromatogramSelection) {

		return calculatorISTD.quantifyAllPeaks(chromatogramSelection);
	}
}
