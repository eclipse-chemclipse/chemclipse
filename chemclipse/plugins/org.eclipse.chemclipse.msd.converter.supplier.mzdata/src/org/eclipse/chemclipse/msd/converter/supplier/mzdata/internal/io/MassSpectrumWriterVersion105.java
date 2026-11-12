/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AcqSpecification;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DataProcessingType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.InstrumentDescriptionType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SourceFileType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumList;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumSettingsType;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class MassSpectrumWriterVersion105 implements IMassSpectraWriter {

	private static final Logger logger = Logger.getLogger(MassSpectrumWriterVersion105.class);

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectrum(file, massSpectrum, monitor);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectra(file, massSpectra, monitor);
	}

	private void writeMassSpectra(File file, IMassSpectra massSpectra, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, massSpectra.size());
		for(int i = 1; i <= massSpectra.size(); i++) {
			IScanMSD massSpectrum = massSpectra.getMassSpectrum(i);
			if(massSpectrum != null && massSpectrum.getNumberOfIons() > 0) {
				writeMassSpectrum(file, massSpectrum, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
			}
		}
	}

	private void writeMassSpectrum(File file, IScanMSD scanMSD, IProgressMonitor monitor) {

		try {
			writeMzData(file, scanMSD, monitor);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private void writeMzData(File file, IScanMSD scanMSD, IProgressMonitor monitor) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(createMzData(file, scanMSD, monitor), file);
	}

	private MzData createMzData(File file, IScanMSD scanMSD, IProgressMonitor monitor) {

		MzData mzData = new MzData();
		mzData.setVersion(WriterVersion105.VERSION);
		if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			mzData.setSpectrumList(createSpectrumList(standaloneMassSpectrum, monitor));
			mzData.setDescription(createDescription(file, standaloneMassSpectrum));
		}
		return mzData;
	}

	private Description createDescription(File file, IStandaloneMassSpectrum standaloneMassSpectrum) {

		Description description = new Description();
		description.setAdmin(createAdmin(file, standaloneMassSpectrum));
		description.setDataProcessing(createDataProcessing());
		description.setInstrument(createInstrumentDescription(standaloneMassSpectrum));
		return description;
	}

	private DataProcessingType createDataProcessing() {

		DataProcessingType dataProcessing = new DataProcessingType();
		dataProcessing.setSoftware(createSoftware());
		return dataProcessing;
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
		return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
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

	private SpectrumList createSpectrumList(IRegularMassSpectrum regularMassSpectrum, IProgressMonitor monitor) {

		SpectrumList spectrumList = new SpectrumList();
		spectrumList.setCount(1);
		spectrumList.getSpectrum().add(createSpectrum(regularMassSpectrum, monitor));
		return spectrumList;
	}

	private Spectrum createSpectrum(IRegularMassSpectrum regularMassSpectrum, IProgressMonitor monitor) {

		Spectrum spectrum = new Spectrum();
		spectrum.setSpectrumDesc(createSpectrumDescription(regularMassSpectrum));
		WriterVersion105.setBinaryArrays(spectrum, regularMassSpectrum, monitor);
		return spectrum;
	}

	private SpectrumDescType createSpectrumDescription(IRegularMassSpectrum regularMassSpectrum) {

		SpectrumDescType spectrumDescription = new SpectrumDescType();
		spectrumDescription.setSpectrumSettings(createSpectrumSettings(regularMassSpectrum));
		return spectrumDescription;
	}

	private SpectrumSettingsType createSpectrumSettings(IRegularMassSpectrum regularMassSpectrum) {

		SpectrumSettingsType spectrumSettings = new SpectrumSettingsType();
		spectrumSettings.setSpectrumInstrument(createSpectrumInstrument(regularMassSpectrum));
		spectrumSettings.setAcqSpecification(createAcquisitionSpecification(regularMassSpectrum));
		return spectrumSettings;
	}

	private SpectrumInstrument createSpectrumInstrument(IRegularMassSpectrum regularMassSpectrum) {

		SpectrumInstrument spectrumInstrument = new SpectrumInstrument();
		spectrumInstrument.setMsLevel(regularMassSpectrum.getMassSpectrometer());
		return spectrumInstrument;
	}

	private AcqSpecification createAcquisitionSpecification(IRegularMassSpectrum regularMassSpectrum) {

		AcqSpecification acquisitionSpecification = new AcqSpecification();
		if(regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.CENTROID) {
			acquisitionSpecification.setSpectrumType("discrete");
		} else if(regularMassSpectrum.getMassSpectrumType() == MassSpectrumType.PROFILE) {
			acquisitionSpecification.setSpectrumType("continuous");
		}
		return acquisitionSpecification;
	}
}
