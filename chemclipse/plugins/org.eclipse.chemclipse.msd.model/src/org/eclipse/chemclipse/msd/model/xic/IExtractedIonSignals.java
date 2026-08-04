/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add method to get all ion values as array
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.xic;

import java.util.List;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.support.traces.ITrace;

public interface IExtractedIonSignals {

	/**
	 * Returns the chromatogram where these signals derive from.
	 */
	IChromatogramMSD getChromatogram();

	/**
	 * Adds a new ExtractedIonSignal at the end of the stored signals.
	 */
	void add(IExtractedIonSignal extractedIonSignal);

	/**
	 * Adds the given ion with its abundance to the scan, next to the retention
	 * time.<br/>
	 * Use removePreviousAbundance to clear the abundance value except of the
	 * given one.
	 */
	void add(int ion, float abundance, int retentionTime, boolean removePreviousAbundance);

	/**
	 * Returns an IExtractedIonSignal instance from the given scan position.
	 */
	// TODO eventuell doch null zurückgeben, wenn kein Signal gespeichert ist
	IExtractedIonSignal getExtractedIonSignal(int scan) throws NoExtractedIonSignalStoredException;

	// TODO JUnit
	/**
	 * Returns a list of extracted ion signals.
	 */
	List<IExtractedIonSignal> getExtractedIonSignals();

	/**
	 * Returns a {@link ITotalScanSignals} instance of the given ion
	 * (ion) value.
	 */
	ITotalScanSignals getTotalIonSignals(int ion);

	/**
	 * Returns all stored signals as a {@link ITotalScanSignals} instance.
	 */
	ITotalScanSignals getTotalIonSignals();

	/**
	 * Returns a {@link ITotalScanSignals} instance of the given ion
	 * (ion) value and the given scan range.
	 */
	ITotalScanSignals getTotalIonSignals(int ion, IScanRange scanRange);

	/**
	 * Returns a {@link ITotalScanSignals} instance of the total ion signals and
	 * the given scan range.
	 */
	ITotalScanSignals getTotalIonSignals(IScanRange scanRange);

	/**
	 * Returns an {@link IScanMSD} instance for the given scan.<br/>
	 * If no mass spectrum is available, null will be returned.
	 */
	IScanMSD getScan(int scan);

	/**
	 * Returns an {@link IScanMSD} instance for the given scan without the
	 * excluded ions.<br/>
	 * If no mass spectrum is available, null will be returned.
	 */
	IScanMSD getScan(int scan, IMarkedTraces<ITrace> excludedIons);

	/**
	 * Returns the lowest start ion (ion).
	 */
	int getStartIon();

	/**
	 * Returns the highest stop ion (ion).
	 */
	int getStopIon();

	/**
	 * Returns the size.
	 */
	int size();

	/**
	 * Returns the start scan.
	 */
	int getStartScan();

	/**
	 * Returns the stop scan.
	 */
	int getStopScan();

	/**
	 * Returns a deep copy of the actual extracted ion signals.<br/>
	 * The copy will contain scans as {@link IExtractedIonSignal} but without
	 * ion values.<br/>
	 * The returned instance can be used to calculate another ion
	 * distribution but with the same scan - retention time pattern.
	 */
	IExtractedIonSignals makeDeepCopyWithoutSignals();

	/**
	 * Calculates the median from mean using the extracted ion signals.
	 */
	default float[] getValues(IScanRange range, int ion) {

		int width = range.getWidth();
		float[] values = new float[width];

		if(width > 0) {
			int counter = 0;
			for(int scan = range.getStartScan(); scan <= range.getStopScan(); scan++) {
				try {
					IExtractedIonSignal signal = getExtractedIonSignal(scan);
					/*
					 * If the ion represents the TIC than use the total signal,
					 * otherwise get the abundance of the given ion.
					 */
					if(ion == IIon.TIC_ION) {
						values[counter] = signal.getTotalSignal();
					} else {
						values[counter] = signal.getAbundance(ion);
					}
				} catch(NoExtractedIonSignalStoredException e) {
					values[counter] = 0;
				} finally {
					/*
					 * Increment counters position.
					 */
					counter++;
				}
			}
		}

		return values;
	}
}