/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumList;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumSettingsType;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class ChromatogramWriterVersion105 extends AbstractChromatogramWriter implements IChromatogramMSDWriter {

	public static final String VERSION = "1.05";
	//
	private static final Logger logger = Logger.getLogger(ChromatogramWriterVersion105.class);

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(createMzData(chromatogram), file);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private MzData createMzData(IChromatogramMSD chromatogram) {

		MzData mzData = new MzData();
		mzData.setVersion(VERSION);
		mzData.setSpectrumList(createSpectrumList(chromatogram));
		return mzData;
	}

	private SpectrumList createSpectrumList(IChromatogramMSD chromatogram) {

		SpectrumList spectrumList = new SpectrumList();
		for(IScan scan : chromatogram.getScans()) {
			spectrumList.getSpectrum().add(createSpectrum(scan));
		}
		return spectrumList;
	}

	private Spectrum createSpectrum(IScan scan) {

		Spectrum spectrum = new Spectrum();
		spectrum.setSpectrumDesc(createSpectrumDesc(scan));
		IScanMSD scanMSD = (IScanMSD)scan;
		setBinaryArrays(spectrum, scanMSD);
		return spectrum;
	}

	private void setBinaryArrays(Spectrum spectrum, IScanMSD scanMSD) {

		double[] ions = new double[scanMSD.getNumberOfIons()];
		float[] abundances = new float[scanMSD.getNumberOfIons()];
		int i = 0;
		for(IIon ion : scanMSD.getIons()) {
			ions[i] = ion.getIon();
			abundances[i] = ion.getAbundance();
			i++;
		}
		spectrum.setMzArrayBinary(WriterVersion105.createFromDoubles(ions));
		spectrum.setIntenArrayBinary(WriterVersion105.createFromFloats(abundances));
	}

	private SpectrumDescType createSpectrumDesc(IScan scan) {

		SpectrumDescType spectrumDesc = new SpectrumDescType();
		spectrumDesc.setSpectrumSettings(createSpectrumSettings(scan));
		return spectrumDesc;
	}

	private SpectrumSettingsType createSpectrumSettings(IScan scan) {

		SpectrumSettingsType spectrumSettings = new SpectrumSettingsType();
		spectrumSettings.setSpectrumInstrument(createSpectrumInstrument(scan));
		return spectrumSettings;
	}

	private SpectrumInstrument createSpectrumInstrument(IScan scan) {

		SpectrumInstrument spectrumInstrument = new SpectrumInstrument();
		spectrumInstrument.getCvParamOrUserParam().add(createRetentionTime(scan));
		return spectrumInstrument;
	}

	private CvParamType createRetentionTime(IScan scan) {

		CvParamType retentionTime = new CvParamType();
		retentionTime.setName("TimeInSeconds");
		retentionTime.setValue(String.valueOf(scan.getRetentionTime() / 1000f));
		return retentionTime;
	}
}
