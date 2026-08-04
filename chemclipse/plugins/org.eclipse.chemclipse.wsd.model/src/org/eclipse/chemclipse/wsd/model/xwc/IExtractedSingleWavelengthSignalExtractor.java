/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - refactor traces
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.xwc;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;

public interface IExtractedSingleWavelengthSignalExtractor {

	/**
	 * 
	 * @param chromatogramSelection
	 * @return List of signals for marked wavelengths, signals are sorted according to wavelength
	 *         first is the shortest wavelength and last is longest wavelength.
	 *         if parameter join signal is to true signal for each wavelength is return together, otherwise intervals are saved separately
	 */
	List<IExtractedSingleWavelengthSignals> getExtractedWavelengthSignals(IChromatogramSelectionWSD chromatogramSelection);

	/**
	 * 
	 * @return List of signals for marked wavelengths, signals are sorted according to wavelength
	 *         first is the shortest wavelength and last is longest wavelength.
	 *         if parameter join signal is to true signal for each wavelength is return together, otherwise intervals are saved separately
	 */
	List<IExtractedSingleWavelengthSignals> getExtractedWavelengthSignals();

	/**
	 * signal start and finish in interval between startScan and stopScan
	 * 
	 * @param startScan
	 * @param stopScan
	 * @return List of signals for marked wavelengths, signals are sorted according to wavelength
	 *         first is the shortest wavelength and last is longest wavelength.
	 *         if parameter join signal is to true signal for each wavelength is return together, otherwise intervals are saved separately
	 */
	List<IExtractedSingleWavelengthSignals> getExtractedWavelengthSignals(int startScan, int stopScan);

	/**
	 * return list of signals for all marked wavelength
	 * each signal start and finish in interval between startScan and stopScan
	 * 
	 * @param startScan
	 * @param stopScan
	 * @param markedWavelengths
	 * @return List of signals for marked wavelengths, signals are sorted according to wavelength
	 *         first is the shortest wavelength and last is longest wavelength.
	 *         if parameter join signal is to true signal for each wavelength is return together, otherwise intervals are saved separately
	 */
	List<IExtractedSingleWavelengthSignals> getExtractedWavelengthSignals(int startScan, int stopScan, IMarkedTraces<ITrace> markedWavelengths);

	/**
	 * return list of signals for all marked wavelength
	 * each signal start and finish in interval between startScan and stopScan
	 * 
	 * @param markedWavelengths
	 * @return List of signals for marked wavelengths, signals are sorted according to wavelength
	 *         first is the shortest wavelength and last is longest wavelength.
	 *         if parameter join signal is to true signal for each wavelength is return together, otherwise intervals are saved separately
	 */
	List<IExtractedSingleWavelengthSignals> getExtractedWavelengthSignals(IMarkedTraces<ITrace> markedWavelengths);

	/**
	 * signal start and finish in interval between startScan and stopScan
	 * 
	 * @param startScan
	 * @param stopScan
	 * @param markedWavelength
	 * @return signal
	 */
	Optional<IExtractedSingleWavelengthSignals> getExtractWavelengthContinuousSignal(int startScan, int stopScan, IMarkedTraces<ITrace> markedWavelength);

	/**
	 * 
	 * signal start and finish in interval between startScan and stopScan
	 * 
	 * @param markedWavelength
	 * @return signal
	 */
	Optional<IExtractedSingleWavelengthSignals> getExtractWavelengthContinuousSignal(IMarkedTraces<ITrace> markedWavelength);

	/**
	 * if set true, signal, which has some wavelength, will be storage together and missing signal will be interpolated
	 * otherwise if signal contains discontinuity, it will be split.
	 * 
	 * @return
	 */
	boolean isJoinSignal();

	/**
	 * if set true, signal, which has some wavelength, will be storage together and missing signal will be interpolated
	 * otherwise if signal contains discontinuity, it will be split.
	 * 
	 * @param joinSignal
	 */
	void setJoinSignal(boolean joinSignal);

	static SortedMap<Double, SortedSet<IExtractedSingleWavelengthSignals>> sortExtractedSignals(List<IExtractedSingleWavelengthSignals> extractedSingleWavelengthSignals) {

		SortedMap<Double, SortedSet<IExtractedSingleWavelengthSignals>> sortedMap = new TreeMap<>();
		for(IExtractedSingleWavelengthSignals signals : extractedSingleWavelengthSignals) {
			double wavelength = signals.getWavelength();
			sortedMap.putIfAbsent(wavelength, new TreeSet<>((s1, s2) -> Integer.compare(s1.getStartScan(), s2.getStartScan())));
			SortedSet<IExtractedSingleWavelengthSignals> signalOnWavelength = sortedMap.get(wavelength);
			signalOnWavelength.add(signals);
		}
		return sortedMap;
	}
}