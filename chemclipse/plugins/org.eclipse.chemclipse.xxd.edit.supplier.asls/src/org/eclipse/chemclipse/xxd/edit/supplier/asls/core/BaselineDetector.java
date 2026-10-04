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

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core.AbstractBaselineDetector;
import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.settings.IBaselineDetectorSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.edit.supplier.alsl.settings.BaselineDetectorSettings;
import org.eclipse.core.runtime.IProgressMonitor;

public class BaselineDetector extends AbstractBaselineDetector {

	@Override
	public IProcessingInfo<?> setBaseline(IChromatogramSelection chromatogramSelection, IBaselineDetectorSettings baselineDetectorSettings, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo = super.validate(chromatogramSelection, baselineDetectorSettings, monitor);
		if(!processingInfo.hasErrorMessages()) {
			if(baselineDetectorSettings instanceof BaselineDetectorSettings settings) {
				calculateBaseline(chromatogramSelection, settings);
			}
		}
		return processingInfo;
	}

	private static void calculateBaseline(IChromatogramSelection chromatogramSelection, BaselineDetectorSettings detectorSettings) {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		ITotalScanSignalExtractor totalScanSignalExtractor = new TotalScanSignalExtractor(chromatogram);

		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());

		ITotalScanSignals totalScanSignals = totalScanSignalExtractor.getTotalScanSignals(startScan, stopScan);
		double[] abundances = totalScanSignals.getTotalScanSignals().stream().mapToDouble(ITotalScanSignal::getTotalSignal).toArray();
		double[] baseline = AsymmetricLeastSquares.baseline(abundances, //
				detectorSettings.getAsymmetryFactor(), //
				detectorSettings.getSmoothnessFactor(), //
				detectorSettings.getIterations());

		int i = 0;
		for(ITotalScanSignal totalScanSignal : totalScanSignals.getTotalScanSignals()) {
			totalScanSignal.setTotalSignal((float)baseline[i]);
			i++;
		}

		chromatogram.getBaselineModel().addBaseline(totalScanSignals);
	}
}