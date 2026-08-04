/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - refactor traces
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.chemclipse.fsd.model.core.IScanFSD;
import org.eclipse.chemclipse.fsd.model.core.IScanSignalFSD;
import org.eclipse.chemclipse.model.core.AbstractScan;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.support.traces.ITrace;

public abstract class AbstractScanFSD extends AbstractScan implements IScanFSD {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 2858355503545583806L;

	private List<IScanSignalFSD> scanSignals = new ArrayList<>();

	protected AbstractScanFSD() {

	}

	protected AbstractScanFSD(IScanFSD scanFSD, float actualPercentageIntensity) throws IllegalArgumentException {

		if(scanFSD == null) {
			throw new IllegalArgumentException("The scanFSD must not be null");
		}
		if(actualPercentageIntensity <= 0.0f) {
			throw new IllegalArgumentException("The percentageIntensity must not be > 0.");
		}
		/*
		 * 
		 */
		for(IScanSignalFSD scanSignal : scanFSD.getScanSignals()) {
			float abundance = (scanSignal.getFluorescence() / actualPercentageIntensity) * 100.0f;
			scanSignals.add(new ScanSignalFSD(scanSignal.getWavelength(), abundance));
		}
	}

	@Override
	public IScanSignalFSD getScanSignal(int scan) {

		return scanSignals.get(scan);
	}

	@Override
	public Optional<IScanSignalFSD> getScanSignal(float wavelength) {

		for(IScanSignalFSD scanSignal : scanSignals) {
			if(scanSignal.getWavelength() == wavelength) {
				return Optional.of(scanSignal);
			}
		}
		return Optional.empty();
	}

	@Override
	public void deleteScanSignals() {

		scanSignals.clear();
	}

	@Override
	public void addScanSignal(IScanSignalFSD scanSignalFSD) {

		scanSignals.add(scanSignalFSD);
	}

	@Override
	public void removeScanSignal(IScanSignalFSD scanSignalFSD) {

		scanSignals.remove(scanSignalFSD);
	}

	@Override
	public void removeScanSignal(int scan) {

		scanSignals.remove(scan);
	}

	@Override
	public void removeScanSignals(Set<Integer> wavelengths) {

		List<IScanSignalFSD> scanSignalsToRemove = new ArrayList<>();
		for(int wavelength : wavelengths) {
			for(IScanSignalFSD scanSignal : scanSignals) {
				if(scanSignal.getWavelength() == wavelength) {
					scanSignalsToRemove.add(scanSignal);
				}
			}
		}

		scanSignals.removeAll(scanSignalsToRemove);
	}

	@Override
	public int getNumberOfScanSignals() {

		return scanSignals.size();
	}

	@Override
	public List<IScanSignalFSD> getScanSignals() {

		return Collections.unmodifiableList(scanSignals);
	}

	@Override
	public float getTotalSignal() {

		float totalSignal = 0.0f;
		for(IScanSignalFSD scan : scanSignals) {
			totalSignal += scan.getFluorescence();
		}
		return totalSignal;
	}

	@Override
	public float getTotalSignal(IMarkedTraces<ITrace> markedWavelengths) {

		float totalSignal = 0;
		/*
		 * If the excluded ions are null, return the total signal.
		 */
		if(markedWavelengths == null || markedWavelengths.isEmpty()) {
			totalSignal = getTotalSignal();
		} else {
			Iterator<IScanSignalFSD> iterator = scanSignals.iterator();
			while(iterator.hasNext()) {
				IScanSignalFSD scan = iterator.next();
				if(useWavelength(scan, markedWavelengths)) {
					totalSignal += scan.getFluorescence();
				}
			}
		}
		return totalSignal;
	}

	private static boolean useWavelength(IScanSignalFSD scan, IMarkedTraces<ITrace> filterWavelengths) {

		Set<Float> wavelengths = new HashSet<>();
		for(ITrace trace : filterWavelengths) {
			wavelengths.add((float)trace.getValue());
		}
		/*
		 * Apply
		 */
		switch(filterWavelengths.getMarkedTraceModus()) {
			case EXCLUDE:
				return wavelengths.contains(scan.getWavelength());
			case INCLUDE:
				return !wavelengths.contains(scan.getWavelength());
			default:
				return true;
		}
	}

	@Override
	public void adjustTotalSignal(float totalSignal) {

		/*
		 * If the total signal is 0 there would be no wavelength stored in
		 * the list.<br/> That's not what we want.
		 */
		if(totalSignal < 0.0f || Float.isNaN(totalSignal) || Float.isInfinite(totalSignal)) {
			return;
		}
		/*
		 * Do not cause a division by zero exception :-).
		 */
		if(getTotalSignal() == 0.0f) {
			return;
		}
		float base = 100.0f;
		float correctionFactor = ((base / getTotalSignal()) * totalSignal) / base;
		float fluorescence;
		for(IScanSignalFSD scanSignal : scanSignals) {
			fluorescence = scanSignal.getFluorescence();
			fluorescence *= correctionFactor;
			scanSignal.setFluorescence(fluorescence);
		}
	}

	@Override
	public boolean hasScanSignals() {

		return !scanSignals.isEmpty();
	}
}
