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

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.AbstractRegularMassSpectrumProxy;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.io.IReaderProxy;
import org.eclipse.core.runtime.IProgressMonitor;

public class VendorScanProxy extends AbstractRegularMassSpectrumProxy implements IVendorScanProxy {

	private static final long serialVersionUID = 1995840167170528873L;

	private static final Logger logger = Logger.getLogger(VendorScanProxy.class);

	private IReaderProxy readerProxy;

	public VendorScanProxy(IReaderProxy readerProxy, IProgressMonitor monitor) {

		super(monitor);
		this.readerProxy = readerProxy;
	}

	@Override
	public void importIons(IProgressMonitor monitor) {

		try {
			readerProxy.readMassSpectrum(this, monitor);
		} catch(IOException e) {
			logger.error(e);
		}
	}

	/**
	 * Keep in mind, it is a covariant return.<br/>
	 * IMassSpectrum is needed. IMassSpectrum is a subtype of
	 * ISupplierMassSpectrum is a subtype of IMassSpectrum.
	 */
	@Override
	public IVendorScan makeDeepCopy() throws CloneNotSupportedException {

		IVendorScanProxy massSpectrum = (IVendorScanProxy)super.clone();
		/*
		 * The instance variables have been copied by super.clone();.<br/> The
		 * ions in the ion list need not to be removed via
		 * removeAllIons as the method super.clone() has created a new
		 * list.<br/> It is necessary to fill the list again, as the abstract
		 * super class does not know each available type of ion.<br/>
		 * Make a deep copy of all ions.
		 */
		for(IIon ion : getIons()) {
			IVendorIon vendorIon = new VendorIon(ion.getIon(), ion.getAbundance());
			massSpectrum.addIon(vendorIon);
		}
		return massSpectrum;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return makeDeepCopy();
	}
}