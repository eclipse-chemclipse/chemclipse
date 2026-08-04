/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add total signal except excluded
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.wsd.model.xwc.IExtractedSingleWavelengthSignal;
import org.eclipse.chemclipse.wsd.model.xwc.IExtractedWavelengthSignal;

public interface IScanWSD extends IScan {

	IScanSignalWSD getScanSignal(int scan);

	/**
	 * method return signal on exact wavelength
	 * 
	 * @param wavelength
	 * @return signal scan
	 */
	Optional<IScanSignalWSD> getScanSignal(float wavelength);

	void deleteScanSignals();

	void addScanSignal(IScanSignalWSD scanSignalWSD);

	void removeScanSignal(IScanSignalWSD scanSignalWSD);

	int getNumberOfScanSignals();

	List<IScanSignalWSD> getScanSignals();

	void removeScanSignal(int scan);

	void removeScanSignals(Set<Integer> wavelengths);

	IExtractedWavelengthSignal getExtractedWavelengthSignal();

	IExtractedWavelengthSignal getExtractedWavelengthSignal(float startWavelength, float stopWavelength);

	Optional<IExtractedSingleWavelengthSignal> getExtractedSingleWavelengthSignal(float wavelength);

	boolean hasScanSignals();

	IWavelengthBounds getWavelengthBounds();

	float getTotalSignal(IMarkedTraces<ITrace> excludedWavelenths);
}