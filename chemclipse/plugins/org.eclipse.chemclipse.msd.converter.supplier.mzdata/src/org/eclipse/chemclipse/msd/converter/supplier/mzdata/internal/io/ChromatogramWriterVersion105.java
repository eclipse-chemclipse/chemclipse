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
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.InstrumentDescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SourceFileType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumList;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumSettingsType;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class ChromatogramWriterVersion105 extends AbstractChromatogramWriter implements IChromatogramMSDWriter {

	private static final Logger logger = Logger.getLogger(ChromatogramWriterVersion105.class);

	@Override
	public void writeChromatogram(File file, IChromatogramMSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(createMzData(file, chromatogram), file);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private MzData createMzData(File file, IChromatogramMSD chromatogram) {

		MzData mzData = new MzData();
		mzData.setVersion(WriterVersion105.VERSION);
		mzData.setSpectrumList(createSpectrumList(chromatogram));
		mzData.setDescription(createDescription(file, chromatogram));
		return mzData;
	}

	private Description createDescription(File file, IChromatogramMSD chromatogram) {

		Description description = new Description();
		description.setAdmin(createAdmin(file, chromatogram));
		description.setInstrument(createInstrumentDescription(chromatogram));
		return description;
	}

	private AdminType createAdmin(File file, IChromatogramMSD chromatogram) {

		AdminType admin = new AdminType();
		admin.getContact().add(createPerson(chromatogram));
		admin.setSampleDescription(createSampleDescription(chromatogram));
		admin.setSampleName(chromatogram.getSampleName());
		admin.setSourceFile(createSourceFile(file));
		return admin;
	}

	private SourceFileType createSourceFile(File file) {

		SourceFileType sourceFile = new SourceFileType();
		sourceFile.setNameOfFile(file.getName());
		sourceFile.setPathToFile(file.getAbsolutePath());
		return sourceFile;
	}

	private DescriptionType createSampleDescription(IChromatogramMSD chromatogram) {

		DescriptionType description = new DescriptionType();
		description.setComment(chromatogram.getMiscInfo());
		return description;
	}

	private PersonType createPerson(IChromatogramMSD chromatogram) {

		PersonType person = new PersonType();
		person.setName(chromatogram.getOperator());
		return person;
	}

	private InstrumentDescriptionType createInstrumentDescription(IChromatogramMSD chromatogram) {

		InstrumentDescriptionType instrumentDescription = new InstrumentDescriptionType();
		instrumentDescription.setInstrumentName(chromatogram.getInstrument());
		return instrumentDescription;
	}

	private SpectrumList createSpectrumList(IChromatogramMSD chromatogram) {

		SpectrumList spectrumList = new SpectrumList();
		spectrumList.setCount(chromatogram.getNumberOfScans());
		for(IScan scan : chromatogram.getScans()) {
			spectrumList.getSpectrum().add(createSpectrum(scan));
		}
		return spectrumList;
	}

	private Spectrum createSpectrum(IScan scan) {

		Spectrum spectrum = new Spectrum();
		spectrum.setSpectrumDesc(createSpectrumDesc(scan));
		spectrum.setId(scan.getScanNumber());
		IScanMSD scanMSD = (IScanMSD)scan;
		WriterVersion105.setBinaryArrays(spectrum, scanMSD);
		return spectrum;
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
		retentionTime.setCvLabel("psi");
		retentionTime.setName("TimeInSeconds");
		retentionTime.setAccession("PSI:1000039");
		retentionTime.setValue(String.valueOf(scan.getRetentionTime() / 1000f));
		return retentionTime;
	}
}
