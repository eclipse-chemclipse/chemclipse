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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.combined;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.exceptions.NoIntegratorAvailableException;

public interface ICombinedIntegratorSupport {

	/**
	 * Returns the id of the selected integrator name.<br/>
	 * The id of the selected integrator is used to determine which integrator
	 * should be used to calculate the integration results of the chromatogram.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getIntegratorId(int index) throws NoIntegratorAvailableException;

	/**
	 * Returns an IPeakIntegratorSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	ICombinedIntegratorSupplier getIntegratorSupplier(String integratorId) throws NoIntegratorAvailableException;

	/**
	 * Returns an ArrayList with all available peak integrator ids.<br/>
	 */
	List<String> getAvailableIntegratorIds() throws NoIntegratorAvailableException;

	/**
	 * Returns the list of available peak integrator names.
	 */
	String[] getIntegratorNames() throws NoIntegratorAvailableException;
}
