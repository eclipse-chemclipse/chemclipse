/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io;

import java.io.IOException;
import java.util.Map;

import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model.IVendorScanProxy;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IReaderProxy {

	void readMassSpectrum(IVendorScanProxy massSpectrum) throws IOException;

	/**
	 * Reads the ions of all given scan proxies in a single pass over the spectra file.
	 *
	 * @param scanProxies
	 *            scan proxies that are not loaded yet, keyed by their zero based spectrum index
	 */
	void readMassSpectra(Map<Integer, IVendorScanProxy> scanProxies, IProgressMonitor monitor) throws IOException;
}
