/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.exceptions.NoMassSpectrumFilterSupplierAvailableException;

public interface IMassSpectrumFilterSupport {

	/**
	 * Returns the id of the selected filter name.<br/>
	 * The id of the selected filter is used to determine which filter should be used.
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getFilterId(int index) throws NoMassSpectrumFilterSupplierAvailableException;

	/**
	 * Returns an IMassSpectrumFilterSupplier object.<br/>
	 * The object stores some additional supplier information.
	 */
	IMassSpectrumFilterSupplier getFilterSupplier(String filterId) throws NoMassSpectrumFilterSupplierAvailableException;

	/**
	 * Returns an ArrayList with all available mass spectrum filter supplier ids.<br/>
	 */
	List<String> getAvailableFilterIds() throws NoMassSpectrumFilterSupplierAvailableException;

	/**
	 * Returns the list of available mass spectrum filter names.
	 */
	String[] getFilterNames() throws NoMassSpectrumFilterSupplierAvailableException;

	Collection<IMassSpectrumFilterSupplier> getSuppliers();
}
