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

import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.AbstractChromatogramMSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io.IReaderProxy;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class VendorChromatogram extends AbstractChromatogramMSD implements IVendorChromatogram {

	private static final long serialVersionUID = 2052424115761266178L;

	private static final Logger logger = Logger.getLogger(VendorChromatogram.class);

	/**
	 * Loading each scan proxy on its own passes through the whole spectra file per scan.
	 * Hence, collect all scan proxies that are not loaded yet and read them in one pass.
	 */
	@Override
	public void enforceLoadScanProxies(IProgressMonitor monitor) {

		if(isUnloaded()) {
			return;
		}

		Map<IReaderProxy, Map<Integer, IVendorScanProxy>> scanProxiesByReader = new IdentityHashMap<>();
		for(IScan scan : getScans()) {
			if(scan instanceof IVendorScanProxy scanProxy && !scanProxy.isLoaded()) {
				Map<Integer, IVendorScanProxy> scanProxies = scanProxiesByReader.get(scanProxy.getReaderProxy());
				if(scanProxies == null) {
					scanProxies = new HashMap<>();
					scanProxiesByReader.put(scanProxy.getReaderProxy(), scanProxies);
				}
				scanProxies.put(scanProxy.getScanNumber() - 1, scanProxy);
			}
		}

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Load Scans", scanProxiesByReader.size());
		for(Map.Entry<IReaderProxy, Map<Integer, IVendorScanProxy>> entry : scanProxiesByReader.entrySet()) {
			try {
				entry.getKey().readMassSpectra(entry.getValue(), subMonitor.split(1));
			} catch(IOException e) {
				logger.error(e);
			}
		}
	}
}
