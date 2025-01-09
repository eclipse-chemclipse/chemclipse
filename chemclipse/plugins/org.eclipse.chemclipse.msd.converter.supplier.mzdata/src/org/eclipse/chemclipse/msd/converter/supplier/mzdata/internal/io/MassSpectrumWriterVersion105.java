/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DataProcessingType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.InstrumentDescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SourceFileType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumList;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class MassSpectrumWriterVersion105 implements IMassSpectraWriter {

	private static final Logger logger = Logger.getLogger(MassSpectrumWriterVersion105.class);

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectrum(file, massSpectrum);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectra(file, massSpectra, monitor);
	}

	private void writeMassSpectra(File file, IMassSpectra massSpectra, IProgressMonitor monitor) throws IOException {

		for(int i = 1; i <= massSpectra.size(); i++) {
			IScanMSD massSpectrum = massSpectra.getMassSpectrum(i);
			if(massSpectrum != null && massSpectrum.getNumberOfIons() > 0) {
				writeMassSpectrum(file, massSpectrum);
			}
		}
	}

	private void writeMassSpectrum(File file, IScanMSD scanMSD) {

		try {
			writeMzData(file, scanMSD);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private void writeMzData(File file, IScanMSD scanMSD) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(createMzData(file, scanMSD), file);
	}

	private MzData createMzData(File file, IScanMSD scanMSD) {

		MzData mzData = new MzData();
		mzData.setVersion(WriterVersion105.VERSION);
		mzData.setSpectrumList(createSpectrumList(scanMSD));
		if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			mzData.setDescription(createDescription(file, standaloneMassSpectrum));
		}
		return mzData;
	}

	private Description createDescription(File file, IStandaloneMassSpectrum standaloneMassSpectrum) {

		Description description = new Description();
		description.setAdmin(createAdmin(file, standaloneMassSpectrum));
		description.setDataProcessing(createDataProcessing(standaloneMassSpectrum));
		description.setInstrument(createInstrumentDescription(standaloneMassSpectrum));
		return description;
	}

	private DataProcessingType createDataProcessing(IStandaloneMassSpectrum standaloneMassSpectrum) {

		DataProcessingType dataProcessing = new DataProcessingType();
		dataProcessing.setSoftware(createSoftware());
		if(standaloneMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID) {
			dataProcessing.setProcessingMethod(createProcessingMethod(standaloneMassSpectrum));
		}
		return dataProcessing;
	}

	private ParamType createProcessingMethod(IStandaloneMassSpectrum standaloneMassSpectrum) {

		ParamType processingMethod = new ParamType();
		processingMethod.getCvParamOrUserParam().add(createPeakProcessingCentroided(standaloneMassSpectrum));
		return processingMethod;
	}

	private CvParamType createPeakProcessingCentroided(IStandaloneMassSpectrum standaloneMassSpectrum) {

		CvParamType cvParamType = new CvParamType();
		cvParamType.setCvLabel("psi");
		cvParamType.setAccession("PSI:1000035");
		cvParamType.setName("PeakProcessing");
		cvParamType.setValue("CentroidMassSpectrum");
		return cvParamType;
	}

	private Software createSoftware() {

		Software software = new Software();
		IProduct product = Platform.getProduct();
		if(product != null) {
			software.setName(product.getName());
			Version version = product.getDefiningBundle().getVersion();
			software.setVersion(version.getMajor() + "." + version.getMinor() + "." + version.getMicro());
		}
		try {
			software.setCompletionTime(createGregorianCalendarNow());
		} catch(DatatypeConfigurationException e) {
			logger.warn(e);
		}
		return software;
	}

	private XMLGregorianCalendar createGregorianCalendarNow() throws DatatypeConfigurationException {

		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		return xmlGregorianCalendar;
	}

	private InstrumentDescriptionType createInstrumentDescription(IStandaloneMassSpectrum standaloneMassSpectrum) {

		InstrumentDescriptionType instrumentDescription = new InstrumentDescriptionType();
		instrumentDescription.setInstrumentName(standaloneMassSpectrum.getInstrument());
		return instrumentDescription;
	}

	private AdminType createAdmin(File file, IStandaloneMassSpectrum standaloneMassSpectrum) {

		AdminType admin = new AdminType();
		admin.getContact().add(createPerson(standaloneMassSpectrum));
		admin.setSampleDescription(createSampleDescription(standaloneMassSpectrum));
		admin.setSampleName(standaloneMassSpectrum.getSampleName());
		admin.setSourceFile(createSourceFile(file));
		return admin;
	}

	private SourceFileType createSourceFile(File file) {

		SourceFileType sourceFile = new SourceFileType();
		sourceFile.setNameOfFile(file.getName());
		sourceFile.setPathToFile(file.getAbsolutePath());
		return sourceFile;
	}

	private DescriptionType createSampleDescription(IStandaloneMassSpectrum standaloneMassSpectrum) {

		DescriptionType description = new DescriptionType();
		description.setComment(standaloneMassSpectrum.getDescription());
		return description;
	}

	private PersonType createPerson(IStandaloneMassSpectrum standaloneMassSpectrum) {

		PersonType person = new PersonType();
		person.setName(standaloneMassSpectrum.getOperator());
		return person;
	}

	private SpectrumList createSpectrumList(IScanMSD scanMSD) {

		SpectrumList spectrumList = new SpectrumList();
		spectrumList.setCount(1);
		spectrumList.getSpectrum().add(createSpectrum(scanMSD));
		return spectrumList;
	}

	private Spectrum createSpectrum(IScanMSD scanMSD) {

		Spectrum spectrum = new Spectrum();
		WriterVersion105.setBinaryArrays(spectrum, scanMSD);
		return spectrum;
	}
}
