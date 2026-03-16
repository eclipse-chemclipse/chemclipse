/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.sumarea.internal.core;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;

public class BackgroundIntegrator extends AbstractSumareaIntegrator {

	private static final Logger logger = Logger.getLogger(BackgroundIntegrator.class);

	@Override
	public double integrate(IChromatogramSelectionMSD chromatogramSelection) {

		return integrate(chromatogramSelection, (int)IIon.TIC_ION);
	}

	@Override
	public double integrate(IChromatogramSelectionMSD chromatogramSelection, int ion) {

		double backgroundArea = 0.0d;
		IChromatogramMSD chromatogram = chromatogramSelection.getChromatogram();
		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
		IBaselineModel baselineModel = chromatogram.getBaselineModel();
		/*
		 * Try to get the and integrate the signals.
		 */
		try {
			IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
			IExtractedIonSignals extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
			IExtractedIonSignal startSignal;
			IExtractedIonSignal stopSignal;
			int start;
			int stop;
			/*
			 * Calculates the area for each background element.
			 */
			for(int scan = startScan; scan < stopScan; scan++) {
				try {
					startSignal = extractedIonSignals.getExtractedIonSignal(scan);
					stopSignal = extractedIonSignals.getExtractedIonSignal(scan + 1);
					IScanMSD supplierMassSpectrumStart = chromatogram.getScan(scan);
					IScanMSD supplierMassSpectrumStop = chromatogram.getScan(scan + 1);
					double ionPercentage = calculateIonPercentageOfScans(supplierMassSpectrumStart, supplierMassSpectrumStop, ion);
					if(startSignal != null && stopSignal != null) {
						start = startSignal.getRetentionTime();
						stop = stopSignal.getRetentionTime();
						float startAbundance = baselineModel.getBackground(start);
						float stopAbundance = baselineModel.getBackground(stop);
						double segmentArea = calculateArea(start, stop, startAbundance, stopAbundance);
						backgroundArea += segmentArea * ionPercentage;
					}
				} catch(Exception e) {
					logger.warn(e);
				}
			}
		} catch(ChromatogramIsNullException e1) {
			logger.warn(e1);
		}
		return backgroundArea;
	}

	private double calculateIonPercentageOfScans(IScanMSD massSpectrumStart, IScanMSD massSpectrumStop, int ion) {

		IExtractedIonSignal extractedIonSignalStart = massSpectrumStart.getExtractedIonSignal();
		IExtractedIonSignal extractedIonSignalStop = massSpectrumStop.getExtractedIonSignal();
		double percentageStartSignal = 0.0d;
		float startSignal = extractedIonSignalStart.getTotalSignal();
		if(startSignal > 0) {
			percentageStartSignal = (100.0d / startSignal) * extractedIonSignalStart.getAbundance(ion);
		}
		double percentageStopSignal = 0.0d;
		float stopSignal = extractedIonSignalStop.getTotalSignal();
		if(stopSignal > 0) {
			percentageStopSignal = (100.0d / stopSignal) * extractedIonSignalStop.getAbundance(ion);
		}
		return ((percentageStartSignal + percentageStopSignal) / 2) / 100.0d;
	}
}
