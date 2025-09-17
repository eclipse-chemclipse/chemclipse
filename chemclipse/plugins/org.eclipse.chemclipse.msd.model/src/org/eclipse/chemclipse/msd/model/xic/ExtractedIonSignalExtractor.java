/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - use the more generic {@link IRetentionTimeRange} instead of Chromatogram selection
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.xic;

import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.support.IRetentionTimeRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;

public class ExtractedIonSignalExtractor implements IExtractedIonSignalExtractor {

	private final IChromatogramMSD chromatogram;

	/**
	 * All values will be extracted from IChromatogram.
	 * 
	 * @param chromatogram
	 * @throws ChromatogramIsNullException
	 */
	public ExtractedIonSignalExtractor(IChromatogramMSD chromatogram) throws ChromatogramIsNullException {

		if(chromatogram == null) {
			throw new ChromatogramIsNullException();
		}
		this.chromatogram = chromatogram;
	}

	@Override
	public IExtractedIonSignals getExtractedIonSignals(float startIon, float stopIon) {

		int startScan = 1;
		int stopScan = chromatogram.getNumberOfScans();
		return getExtractedIonSignals(startScan, stopScan, startIon, stopIon);
	}

	@Override
	public IExtractedIonSignals getExtractedIonSignals() {

		int startScan = 1;
		int stopScan = chromatogram.getNumberOfScans();
		return getExtractedIonSignals(startScan, stopScan);
	}

	@Override
	public IExtractedIonSignals getExtractedIonSignals(IRetentionTimeRange chromatogramSelection) {

		if(chromatogramSelection == null) {
			return new ExtractedIonSignals(0, chromatogram);
		}
		/*
		 * Get the start and stop scan.
		 */
		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
		return getExtractedIonSignals(startScan, stopScan);
	}

	@Override
	public IExtractedIonSignals getExtractedIonSignals(int startScan, int stopScan) {

		return getExtractedIonSignals(startScan, stopScan, 0, 0);
	}

	private IExtractedIonSignals getExtractedIonSignals(int startScan, int stopScan, float startIon, float stopIon) {

		if(chromatogram.getNumberOfScans() == 0) {
			return new ExtractedIonSignals(0, chromatogram);
		}
		/*
		 * Adjust the range.
		 */
		if(startScan > stopScan) {
			int tmp = startScan;
			startScan = stopScan;
			stopScan = tmp;
		}
		/*
		 * Do additional checks.
		 */
		stopScan = (stopScan > chromatogram.getNumberOfScans()) ? chromatogram.getNumberOfScans() : stopScan;
		int start = (startScan < 1) ? 1 : startScan;
		int stop = stopScan;
		/*
		 * Get the start without empty scans.
		 */
		exitloop:
		for(int scan = start; scan <= stop; scan++) {
			if(!chromatogram.getScan(scan).isEmpty()) {
				startScan = scan;
				break exitloop;
			}
		}
		/*
		 * Get the stop without empty scans.
		 */
		for(int scan = stop; scan > startScan; scan--) {
			if(chromatogram.getScan(scan).isEmpty()) {
				stopScan = scan - 1;
			}
		}
		/*
		 * Extract the signals.
		 */
		IExtractedIonSignals extractedIonSignals = new ExtractedIonSignals(startScan, stopScan, chromatogram);
		for(int scan = startScan; scan <= stopScan; scan++) {
			extractSignals(extractedIonSignals, chromatogram.getScan(scan), startIon, stopIon);
		}

		return extractedIonSignals;
	}

	private boolean extractSignals(IExtractedIonSignals extractedIonSignals, IScanMSD scanMSD, float startIon, float stopIon) {

		if(!scanMSD.isEmpty()) {
			if(startIon == 0 && stopIon == 0) {
				extractedIonSignals.add(scanMSD.getExtractedIonSignal());
			} else {
				extractedIonSignals.add(scanMSD.getExtractedIonSignal(startIon, stopIon));
			}
			return true;
		}
		return false;
	}
}
