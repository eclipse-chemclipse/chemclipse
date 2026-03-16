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
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;

public class ChromatogramIntegrator extends AbstractSumareaIntegrator {

	private static final Logger logger = Logger.getLogger(ChromatogramIntegrator.class);

	@Override
	public double integrate(IChromatogramSelectionMSD chromatogramSelection) {

		return integrate(chromatogramSelection, (int)IIon.TIC_ION);
	}

	@Override
	public double integrate(IChromatogramSelectionMSD chromatogramSelection, int ion) {

		double chromatogramArea = 0.0d;
		IChromatogramMSD chromatogram = chromatogramSelection.getChromatogram();
		/*
		 * Try to integrate the signals.
		 */
		try {
			IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
			IExtractedIonSignals extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
			int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
			int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
			IExtractedIonSignal startSignal;
			IExtractedIonSignal stopSignal;
			/*
			 * Calculates the area for each segment.
			 */
			for(int scan = startScan; scan < stopScan; scan++) {
				try {
					startSignal = extractedIonSignals.getExtractedIonSignal(scan);
					stopSignal = extractedIonSignals.getExtractedIonSignal(scan + 1);
					if(startSignal != null && stopSignal != null) {
						float startAbundance = startSignal.getAbundance(ion);
						float stopAbundance = stopSignal.getAbundance(ion);
						double segmentArea = calculateArea(startSignal.getRetentionTime(), stopSignal.getRetentionTime(), startAbundance, stopAbundance);
						chromatogramArea += segmentArea;
					}
				} catch(Exception e) {
					logger.warn(e);
				}
			}
		} catch(ChromatogramIsNullException e1) {
			logger.warn(e1);
		}
		return chromatogramArea;
	}
}
