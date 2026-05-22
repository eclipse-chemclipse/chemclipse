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
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.peak.detector.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.peak.detector.exceptions.NoPeakDetectorAvailableException;

public interface IPeakDetectorSupport {

	/**
	 * Returns the id of the selected peak detector name.<br/>
	 * The id of the selected filter is used to determine which detector should
	 * be used to calculate the integration results of the chromatogram.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getPeakDetectorId(int index) throws NoPeakDetectorAvailableException;

	/**
	 * Returns an IPeakDetectorSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IPeakDetectorSupplier getPeakDetectorSupplier(String peakDetectorId) throws NoPeakDetectorAvailableException;

	/**
	 * Returns an ArrayList with all available peak detector ids.<br/>
	 */
	List<String> getAvailablePeakDetectorIds() throws NoPeakDetectorAvailableException;

	/**
	 * Returns the list of available peak detector names.
	 */
	String[] getPeakDetectorNames() throws NoPeakDetectorAvailableException;
}
