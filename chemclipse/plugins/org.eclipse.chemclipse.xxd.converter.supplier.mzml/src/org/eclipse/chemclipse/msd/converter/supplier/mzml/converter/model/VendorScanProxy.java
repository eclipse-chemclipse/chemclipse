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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.io.ChromatogramMSDReaderVersion110;
import org.eclipse.chemclipse.msd.model.core.AbstractRegularMassSpectrumProxy;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBException;

public class VendorScanProxy extends AbstractRegularMassSpectrumProxy implements IVendorScanProxy {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -6126982710427047493L;

	private static final Logger logger = Logger.getLogger(VendorScanProxy.class);

	private File file;
	private long offset;
	private IVendorChromatogramMSD chromatogram;

	public VendorScanProxy(File file, long offset, IVendorChromatogramMSD chromatogram, IProgressMonitor monitor) {

		super(monitor);
		this.file = file;
		this.offset = offset;
		this.chromatogram = chromatogram;
	}

	@Override
	public void importIons(IProgressMonitor monitor) {

		try {
			SpectrumType mzMLspectrum = XmlHelper.unmarshalElementAtOffset(file, offset, SpectrumType.class);
			this.setIdentifier(mzMLspectrum.getId());
			IRegularMassSpectrum massSpectrum = ChromatogramMSDReaderVersion110.readMassSpectrum(mzMLspectrum);
			this.setMassSpectrumType(massSpectrum.getMassSpectrumType());
			this.setPolarity(massSpectrum.getPolarity());
			this.setMassSpectrometer(massSpectrum.getMassSpectrometer());
			ChromatogramMSDReaderVersion110.readIons(mzMLspectrum, this, chromatogram);
			ChromatogramMSDReaderVersion110.setRetentionTime(mzMLspectrum, this);
			ChromatogramMSDReaderVersion110.setTotalIonCurrent(mzMLspectrum, this);
		} catch(FileNotFoundException e) {
			logger.error(e);
		} catch(IOException e) {
			logger.error(e);
		} catch(JAXBException e) {
			logger.error(e);
		} catch(XMLStreamException e) {
			logger.error(e);
		} catch(FactoryConfigurationError e) {
			logger.error(e);
		}
	}

	@Override
	public IVendorScanMSD makeDeepCopy() throws CloneNotSupportedException {

		IVendorScanProxy massSpectrum = (IVendorScanProxy)super.clone();
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
