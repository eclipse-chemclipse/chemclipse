/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ProxyReaderMSD;
import org.eclipse.chemclipse.msd.model.core.AbstractRegularMassSpectrumProxy;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.core.runtime.IProgressMonitor;

public class VendorScanProxy extends AbstractRegularMassSpectrumProxy implements IVendorScanProxy {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 8949336542952337617L;
	private static final Logger logger = Logger.getLogger(VendorScanProxy.class);

	private File file;
	private int offset;
	private String version;
	private IIonTransitionSettings ionTransitionSettings;

	public VendorScanProxy(File file, int offset, String version, IIonTransitionSettings ionTransitionSettings, IProgressMonitor monitor) {

		super(monitor);
		this.file = file;
		this.offset = offset;
		this.version = version;
		this.ionTransitionSettings = ionTransitionSettings;
	}

	@Override
	public void importIons(IProgressMonitor monitor) {

		try {
			ProxyReaderMSD scanProxyReaderMSD = new ProxyReaderMSD();
			scanProxyReaderMSD.readMassSpectrum(file, offset, version, this, ionTransitionSettings);
		} catch(IOException e) {
			logger.warn(e);
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
			massSpectrum.addIon(vendorIon, false);
		}
		return massSpectrum;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return makeDeepCopy();
	}
}
