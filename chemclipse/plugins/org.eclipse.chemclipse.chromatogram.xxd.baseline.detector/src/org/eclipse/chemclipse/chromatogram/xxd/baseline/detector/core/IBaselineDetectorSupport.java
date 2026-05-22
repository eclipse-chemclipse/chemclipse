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
package org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.exceptions.NoBaselineDetectorAvailableException;

public interface IBaselineDetectorSupport {

	/**
	 * Returns the id of the selected detector name.<br/>
	 * The id of the selected filter is used to determine which detector should
	 * be used to set the baseline of the chromatogram.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getDetectorId(int index) throws NoBaselineDetectorAvailableException;

	/**
	 * Returns an IBaselineDetectorSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IBaselineDetectorSupplier getBaselineDetectorSupplier(String detectorId) throws NoBaselineDetectorAvailableException;

	/**
	 * Returns an ArrayList with all available baseline detector ids.<br/>
	 */
	List<String> getAvailableDetectorIds() throws NoBaselineDetectorAvailableException;

	/**
	 * Returns the list of available baseline detector names.
	 */
	String[] getDetectorNames() throws NoBaselineDetectorAvailableException;
}
