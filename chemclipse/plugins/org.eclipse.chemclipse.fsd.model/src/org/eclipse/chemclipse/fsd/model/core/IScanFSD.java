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
 * Matthias Mailänder - initial API
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.support.traces.ITrace;

public interface IScanFSD extends IScan {

	IScanSignalFSD getScanSignal(int scan);

	/**
	 * method return signal on exact wavelength
	 * 
	 * @param wavelength
	 * @return signal scan
	 */
	Optional<IScanSignalFSD> getScanSignal(float wavelength);

	void deleteScanSignals();

	void addScanSignal(IScanSignalFSD scanSignalFSD);

	void removeScanSignal(IScanSignalFSD scanSignalFSD);

	int getNumberOfScanSignals();

	List<IScanSignalFSD> getScanSignals();

	void removeScanSignal(int scan);

	void removeScanSignals(Set<Integer> wavelengths);

	boolean hasScanSignals();

	float getTotalSignal(IMarkedTraces<ITrace> excludedWavelenths);
}