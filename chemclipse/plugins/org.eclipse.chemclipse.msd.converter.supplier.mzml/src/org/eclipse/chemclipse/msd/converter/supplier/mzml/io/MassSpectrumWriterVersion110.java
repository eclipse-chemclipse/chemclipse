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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.io;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraWriter;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.Activator;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlWriter110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.FileDescriptionType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.InstrumentConfigurationListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ObjectFactory;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.RunType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SourceFileListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SourceFileType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class MassSpectrumWriterVersion110 implements IMassSpectraWriter {

	private static final Logger logger = Logger.getLogger(MassSpectrumWriterVersion110.class);

	@Override
	public void write(File file, IScanMSD massSpectrum, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		IMassSpectra massSpectra = new MassSpectra();
		massSpectra.addMassSpectrum(massSpectrum);
		writeMassSpectra(file, massSpectra);
	}

	@Override
	public void write(File file, IMassSpectra massSpectra, boolean append, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeMassSpectra(file, massSpectra);
	}

	private void writeMassSpectra(File file, IMassSpectra massSpectra) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0.xsd");
			marshaller.marshal(createMzML(massSpectra), file);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	private MzMLType createMzML(IMassSpectra massSpectra) {

		MzMLType mzML = new MzMLType();
		mzML.setVersion(XmlReader110.VERSION);
		SourceFileListType sourceFileList = createSourceFileList(massSpectra);
		IScanMSD scanMSD = massSpectra.getList().get(0);
		if(scanMSD instanceof IRegularMassSpectrum regularMassSpectrum) {
			mzML.setFileDescription(createFileDescription(regularMassSpectrum, sourceFileList));
			if(regularMassSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				mzML.setId(standaloneMassSpectrum.getFile().getName());
			}
		}
		SoftwareListType softwareList = XmlWriter110.createSoftwareList();
		mzML.setSoftwareList(softwareList);
		InstrumentConfigurationListType instrumentConfigurationList = XmlWriter110.createInstrumentConfigurationList(softwareList.getSoftware().get(0));
		mzML.setInstrumentConfigurationList(instrumentConfigurationList);
		DataProcessingListType dataProcessingList = createDataProcessingList(softwareList.getSoftware().get(0));
		mzML.setDataProcessingList(dataProcessingList);
		mzML.setRun(createRun(massSpectra, dataProcessingList, sourceFileList, instrumentConfigurationList));
		mzML.setCvList(XmlWriter110.createCvList());
		return mzML;
	}

	private SourceFileListType createSourceFileList(IMassSpectra massSpectra) {

		SourceFileListType sourceFileListType = new SourceFileListType();
		sourceFileListType.setCount(BigInteger.valueOf(1));
		for(IScanMSD massSpectrum : massSpectra.getList()) {
			if(massSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
				SourceFileType sourceFile = XmlWriter110.createSourceFile(standaloneMassSpectrum.getFile());
				//
				if(massSpectra.getConverterId().equals("org.eclipse.chemclipse.msd.converter.supplier.mzdata.ms")) {
					CVParamType cvParamFileFormat = new CVParamType();
					cvParamFileFormat.setCvRef(XmlWriter110.MS);
					cvParamFileFormat.setAccession("MS:1000564");
					cvParamFileFormat.setName("PSI mzData format");
					cvParamFileFormat.setValue("");
					sourceFile.getCvParam().add(cvParamFileFormat);
					sourceFileListType.setCount(BigInteger.valueOf(2));
				} else if(massSpectra.getConverterId().equals("org.eclipse.chemclipse.msd.converter.supplier.mzxml.ms")) {
					CVParamType cvParamFileFormat = new CVParamType();
					cvParamFileFormat.setCvRef(XmlWriter110.MS);
					cvParamFileFormat.setAccession("MS:1000566");
					cvParamFileFormat.setName("ISB mzXML format");
					cvParamFileFormat.setValue("");
					sourceFile.getCvParam().add(cvParamFileFormat);
					sourceFileListType.setCount(BigInteger.valueOf(2));
				} else if(massSpectra.getConverterId().equals("net.openchrom.msd.converter.supplier.bruker.flex.massspectrum")) {
					CVParamType cvParamFileFormat = new CVParamType();
					cvParamFileFormat.setCvRef(XmlWriter110.MS);
					cvParamFileFormat.setAccession("MS:1000825");
					cvParamFileFormat.setName("Bruker FID format");
					cvParamFileFormat.setValue("");
					sourceFile.getCvParam().add(cvParamFileFormat);
					sourceFileListType.setCount(BigInteger.valueOf(2));
				} else if(massSpectra.getConverterId().equals("net.openchrom.msd.converter.supplier.sciex.t2d")) {
					CVParamType cvParamFileFormat = new CVParamType();
					cvParamFileFormat.setCvRef(XmlWriter110.MS);
					cvParamFileFormat.setAccession("MS:1001560");
					cvParamFileFormat.setName("SCIEX TOF/TOF T2D format");
					cvParamFileFormat.setValue("");
					sourceFile.getCvParam().add(cvParamFileFormat);
					sourceFileListType.setCount(BigInteger.valueOf(2));
				}
				sourceFileListType.getSourceFile().add(sourceFile);
			}
		}
		return sourceFileListType;
	}

	private FileDescriptionType createFileDescription(IRegularMassSpectrum regularMassSpectrum, SourceFileListType sourceFiles) {

		FileDescriptionType fileDescriptionType = new FileDescriptionType();
		fileDescriptionType.setSourceFileList(sourceFiles);
		fileDescriptionType.setFileContent(createFileContent(regularMassSpectrum));
		if(regularMassSpectrum instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			ParamGroupType paramGroupType = XmlWriter110.createOperator(standaloneMassSpectrum.getOperator());
			if(paramGroupType != null) {
				fileDescriptionType.getContact().add(paramGroupType);
			}
		}
		return fileDescriptionType;
	}

	private ParamGroupType createFileContent(IRegularMassSpectrum regularMassSpectrum) {

		ParamGroupType fileContent = new ParamGroupType();
		fileContent.getCvParam().add(XmlWriter110.createSpectrumDimension(regularMassSpectrum));
		fileContent.getCvParam().add(XmlWriter110.createSpectrumType(regularMassSpectrum));
		return fileContent;
	}

	private DataProcessingListType createDataProcessingList(SoftwareType software) {

		DataProcessingListType dataProcessingList = new DataProcessingListType();
		dataProcessingList.setCount(BigInteger.valueOf(1));
		dataProcessingList.getDataProcessing().add(createDataProcessing(software));
		return dataProcessingList;
	}

	private DataProcessingType createDataProcessing(SoftwareType software) {

		DataProcessingType dataProcessing = new DataProcessingType();
		dataProcessing.setId(Activator.getContext().getBundle().getSymbolicName());
		dataProcessing.getProcessingMethod().add(XmlWriter110.createExportProcessingMethod(software));
		return dataProcessing;
	}

	private RunType createRun(IMassSpectra massSpectra, DataProcessingListType dataProcessingList, SourceFileListType sourceFileList, InstrumentConfigurationListType instrumentConfigurationList) {

		RunType run = new RunType();
		run.setDefaultInstrumentConfigurationRef(instrumentConfigurationList.getInstrumentConfiguration().get(0));
		run.setDefaultSourceFileRef(sourceFileList.getSourceFile().get(0));
		run.setId(massSpectra.getName());
		SpectrumListType spectrumList = createSpectrumList(massSpectra, dataProcessingList);
		run.setSpectrumList(spectrumList);
		writeScans(massSpectra, spectrumList);
		IScanMSD scanMSD = massSpectra.getList().get(0);
		if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
			setDate(run, standaloneMassSpectrum);
		}
		return run;
	}

	private void setDate(RunType run, IStandaloneMassSpectrum standaloneMassSpectrum) {

		try {
			XMLGregorianCalendar date = XmlWriter110.createDate(standaloneMassSpectrum.getDate());
			if(date != null) {
				run.setStartTimeStamp(date);
			}
		} catch(DatatypeConfigurationException e) {
			logger.warn(e);
		}
	}

	private SpectrumListType createSpectrumList(IMassSpectra massSpectra, DataProcessingListType dataProcessingList) {

		SpectrumListType spectrumList = new SpectrumListType();
		spectrumList.setCount(BigInteger.valueOf(massSpectra.size()));
		spectrumList.setDefaultDataProcessingRef(dataProcessingList.getDataProcessing().get(0));
		return spectrumList;
	}

	private void writeScans(IMassSpectra massSpectra, SpectrumListType spectrumList) {

		int i = 0;
		for(IScanMSD scanMSD : massSpectra.getList()) {
			SpectrumType spectrum = new SpectrumType();
			spectrum.setId("scan=" + i);
			spectrum.setIndex(BigInteger.valueOf((i)));
			spectrum.getCvParam().add(XmlWriter110.createMassSpectrumType());
			spectrum.getCvParam().add(XmlWriter110.createLowestObservedIon(scanMSD));
			spectrum.getCvParam().add(XmlWriter110.createHighestObservedIon(scanMSD));
			// full spectra
			boolean compression = PreferenceSupplier.getMassSpectraSaveCompression();
			spectrum.setBinaryDataArrayList(XmlWriter110.createFullSpectrumBinaryDataArrayList(scanMSD, compression));
			if(scanMSD instanceof IRegularMassSpectrum massSpectrum) {
				spectrum.getCvParam().add(XmlWriter110.createSpectrumDimension(massSpectrum));
				spectrum.getCvParam().add(XmlWriter110.createSpectrumLevel(massSpectrum));
				spectrum.getCvParam().add(XmlWriter110.createSpectrumType(massSpectrum));
			}
			spectrum.setDefaultArrayLength(scanMSD.getNumberOfIons());
			spectrumList.getSpectrum().add(spectrum);
		}
		i++;
	}
}
