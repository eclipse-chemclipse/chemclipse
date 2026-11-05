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
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model;

import org.eclipse.chemclipse.msd.model.core.IMassSpectrumProxy;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io.IReaderProxy;

public interface IVendorScanProxy extends IVendorScan, IMassSpectrumProxy {

	IReaderProxy getReaderProxy();

	/**
	 * Returns the index of the spectrum in the Parquet table.
	 * It stays the same when scans are removed from the chromatogram.
	 */
	int getSpectrumIndex();

	/**
	 * Returns true once the proxy has left the proxy mode, i.e. its ions are available.
	 */
	boolean isLoaded();

	/**
	 * Leaves the proxy mode with an empty ion list instead of reading the ions from file.
	 * Used by a reader that fills many scans in one pass.
	 */
	void loadWithoutImport();
}
