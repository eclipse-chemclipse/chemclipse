/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.io;

import java.io.File;
import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.io.IChromatogramWriterMzML;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlWriter110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.FileDescriptionType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.InstrumentConfigurationListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ObjectFactory;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.RunType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ScanListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ScanType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SourceFileListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SourceFileType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.FrameworkUtil;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class ChromatogramWriterVersion110 extends AbstractChromatogramWriter implements IChromatogramWriterMzML {

	private static final Logger logger = Logger.getLogger(ChromatogramWriterVersion110.class);

	@Override
	public void writeChromatogram(File file, IChromatogram chromatogram, IProgressMonitor monitor) {

		monitor.beginTask(ConverterMessages.exportChromatogram, IProgressMonitor.UNKNOWN);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0.xsd");
			marshaller.marshal(createMzML(chromatogram, monitor), file);
		} catch(JAXBException e) {
			logger.warn(e);
		}
	}

	public MzMLType createMzML(IChromatogram chromatogram, IProgressMonitor monitor) {

		MzMLType mzML = new MzMLType();
		mzML.setVersion(XmlReader110.VERSION);
		SourceFileListType sourceFileList = createSourceFileList(chromatogram);
		mzML.setFileDescription(createFileDescription(chromatogram, sourceFileList));
		SoftwareListType softwareList = XmlWriter110.createSoftwareList();
		mzML.setSoftwareList(softwareList);
		InstrumentConfigurationListType instrumentConfigurationList = XmlWriter110.createInstrumentConfigurationList(softwareList.getSoftware().get(0));
		mzML.setInstrumentConfigurationList(instrumentConfigurationList);
		DataProcessingListType dataProcessingList = createDataProcessingList(softwareList.getSoftware().get(0));
		mzML.setDataProcessingList(dataProcessingList);
		mzML.setRun(createRun(chromatogram, dataProcessingList, sourceFileList, instrumentConfigurationList, monitor));
		mzML.setCvList(XmlWriter110.createCvList());
		return mzML;
	}

	private RunType createRun(IChromatogram chromatogram, DataProcessingListType dataProcessingList, SourceFileListType sourceFileList, InstrumentConfigurationListType instrumentConfigurationList, IProgressMonitor monitor) {

		RunType run = new RunType();
		run.setId(chromatogram.getName());

		run.setDefaultInstrumentConfigurationRef(instrumentConfigurationList.getInstrumentConfiguration().getFirst());
		if(sourceFileList != null && !sourceFileList.getSourceFile().isEmpty()) {
			run.setDefaultSourceFileRef(sourceFileList.getSourceFile().getFirst());
		}
		SpectrumListType spectrumList = createSpectrumList(chromatogram, dataProcessingList);
		run.setSpectrumList(spectrumList);
		writeScans(chromatogram, spectrumList, monitor);
		for(IChromatogram referencedChromatograms : chromatogram.getReferencedChromatograms()) {
			writeScans(referencedChromatograms, spectrumList, monitor);
		}
		run.setChromatogramList(createChromatogramListType(dataProcessingList, chromatogram));
		setDate(chromatogram, run);
		return run;
	}

	private void setDate(IChromatogram chromatogram, RunType run) {

		try {
			XMLGregorianCalendar date = XmlWriter110.createDate(chromatogram.getDate());
			if(date != null) {
				run.setStartTimeStamp(date);
			}
		} catch(DatatypeConfigurationException e) {
			logger.warn(e);
		}
	}

	private ChromatogramListType createChromatogramListType(DataProcessingListType dataProcessingList, IChromatogram chromatogram) {

		ChromatogramListType chromatogramList = new ChromatogramListType();
		chromatogramList.setDefaultDataProcessingRef(dataProcessingList.getDataProcessing().get(0));
		chromatogramList.setCount(BigInteger.valueOf(chromatogram.getReferencedChromatograms().size() + 1));

		createChromatogram(chromatogram, chromatogramList);
		for(IChromatogram referencedChromatograms : chromatogram.getReferencedChromatograms()) {
			createChromatogram(referencedChromatograms, chromatogramList);
		}

		return chromatogramList;
	}

	private void createChromatogram(IChromatogram chromatogram, ChromatogramListType chromatogramList) {

		if(chromatogram instanceof IChromatogramMSD) {
			TotalSignalData arrayData = writeTotalSignals(chromatogram);
			ChromatogramType tic = createTIC(arrayData);
			chromatogramList.getChromatogram().add(tic);
		} else if(chromatogram instanceof IChromatogramWSD) {
			TotalSignalData arrayData = writeTotalSignals(chromatogram);
			ChromatogramType pda = createPDA(arrayData);
			chromatogramList.getChromatogram().add(pda);
		}

	}

	private SpectrumListType createSpectrumList(IChromatogram chromatogram, DataProcessingListType dataProcessingList) {

		SpectrumListType spectrumList = new SpectrumListType();

		int numberScans = chromatogram.getNumberOfScans();
		for(IChromatogram referencedChromatogram : chromatogram.getReferencedChromatograms()) {
			numberScans += referencedChromatogram.getNumberOfScans();
		}

		spectrumList.setCount(BigInteger.valueOf(numberScans));
		spectrumList.setDefaultDataProcessingRef(dataProcessingList.getDataProcessing().get(0));
		return spectrumList;
	}

	private ChromatogramType createTIC(TotalSignalData data) {

		ChromatogramType tic = new ChromatogramType();
		tic.setId("TIC");
		tic.setIndex(BigInteger.valueOf(0));
		tic.getCvParam().add(XmlWriter110.createTotalIonCurrrentChromatogramType());
		tic.setDefaultArrayLength(data.totalSignals.length);
		tic.setBinaryDataArrayList(createTotalSignalBinaryDataArrayListType(data.totalSignals, data.retentionTimes));
		return tic;
	}

	private ChromatogramType createPDA(TotalSignalData data) {

		ChromatogramType pda = new ChromatogramType();
		pda.setId("PDA");
		pda.setIndex(BigInteger.valueOf(0));
		pda.getCvParam().add(XmlWriter110.createAbsorptionChromatogramType());
		pda.setDefaultArrayLength(data.totalSignals.length);
		pda.setBinaryDataArrayList(createTotalSignalBinaryDataArrayListType(data.totalSignals, data.retentionTimes));
		return pda;
	}

	private BinaryDataArrayListType createTotalSignalBinaryDataArrayListType(float[] totalSignals, float[] retentionTimes) {

		BinaryDataArrayListType binaryDataArrayList = new BinaryDataArrayListType();
		binaryDataArrayList.setCount(BigInteger.valueOf(2));
		binaryDataArrayList.getBinaryDataArray().add(createTotalSignalsBinaryDataArrayType(totalSignals));
		binaryDataArrayList.getBinaryDataArray().add(createRetentionTimesBinaryDataArrayType(retentionTimes));
		return binaryDataArrayList;
	}

	private BinaryDataArrayType createRetentionTimesBinaryDataArrayType(float[] retentionTimes) {

		boolean compression = PreferenceSupplier.getChromatogramSaveCompression();
		BinaryDataArrayType retentionTimesBinaryDataArrayType = XmlWriter110.createBinaryData(retentionTimes, compression);
		retentionTimesBinaryDataArrayType.getCvParam().add(XmlWriter110.createRetentionTimeType());
		return retentionTimesBinaryDataArrayType;
	}

	private BinaryDataArrayType createTotalSignalsBinaryDataArrayType(float[] totalSignals) {

		boolean compression = PreferenceSupplier.getChromatogramSaveCompression();
		BinaryDataArrayType totalSignalsBinaryDataArrayType = XmlWriter110.createBinaryData(totalSignals, compression);
		totalSignalsBinaryDataArrayType.getCvParam().add(XmlWriter110.createIntensityArrayType());
		return totalSignalsBinaryDataArrayType;
	}

	record TotalSignalData(float[] totalSignals, float[] retentionTimes) {
	}

	private TotalSignalData writeTotalSignals(IChromatogram chromatogram) {

		int scans = chromatogram.getNumberOfScans();
		float[] totalSignals = new float[scans];
		float[] retentionTimes = new float[scans];
		int i = 0;
		for(IScan scan : chromatogram.getScans()) {
			totalSignals[i] = scan.getTotalSignal();
			retentionTimes[i] = (float)(scan.getRetentionTime() / IChromatogramOverview.SECOND_CORRELATION_FACTOR);
			i++;
		}
		return new TotalSignalData(totalSignals, retentionTimes);
	}

	private void writeScans(IChromatogram chromatogram, SpectrumListType spectrumList, IProgressMonitor monitor) {

		monitor.beginTask(ConverterMessages.writeScans, chromatogram.getNumberOfScans());
		for(IScan scan : chromatogram.getScans()) {
			SpectrumType spectrum = new SpectrumType();
			spectrum.setId("scan=" + scan.getScanNumber());
			spectrum.setIndex(BigInteger.valueOf((scan.getScanNumber() - 1)));
			spectrum.setScanList(createScanList(scan));
			boolean compression = PreferenceSupplier.getChromatogramSaveCompression();
			if(scan instanceof IScanMSD scanMSD) {
				spectrum.getCvParam().add(XmlWriter110.createTotalIonCurrentType(scan));
				spectrum.getCvParam().add(XmlWriter110.createBasePeakMassType(scanMSD));
				spectrum.getCvParam().add(XmlWriter110.createBasePeakIntensity(scanMSD));
				// full spectra
				spectrum.setBinaryDataArrayList(XmlWriter110.createFullSpectrumBinaryDataArrayList(scanMSD, compression));
				if(scanMSD instanceof IRegularMassSpectrum massSpectrum) {
					spectrum.getCvParam().add(XmlWriter110.createMassSpectrumDimension(massSpectrum));
					if(massSpectrum.getPolarity() != Polarity.NONE) {
						spectrum.getCvParam().add(XmlWriter110.createPolarity(massSpectrum));
					}
					spectrum.getCvParam().add(XmlWriter110.createSpectrumLevel(massSpectrum));
					spectrum.getCvParam().add(XmlWriter110.createSpectrumType(massSpectrum));
				}
				spectrum.setDefaultArrayLength(scanMSD.getNumberOfIons());
			} else if(scan instanceof IScanWSD scanWSD) {
				spectrum.getCvParam().add(XmlWriter110.createWavelengthSpectrumType());
				spectrum.getCvParam().add(XmlWriter110.createWavelengthScanRangeLowest(scanWSD));
				spectrum.getCvParam().add(XmlWriter110.createWavelengthScanRangeHighest(scanWSD));
				spectrum.setDefaultArrayLength(scanWSD.getNumberOfScanSignals());
				spectrum.setBinaryDataArrayList(XmlWriter110.createFullSpectrumBinaryDataArrayList(scanWSD, compression));
			}
			spectrumList.getSpectrum().add(spectrum);
			monitor.worked(1);
		}
	}

	private ScanListType createScanList(IScan scan) {

		ScanListType scanList = new ScanListType();
		scanList.getCvParam().add(XmlWriter110.createCombinationType());
		scanList.setCount(BigInteger.valueOf(1));
		scanList.getScan().add(createScanType(scan));
		return scanList;
	}

	private ScanType createScanType(IScan scan) {

		ScanType scanType = new ScanType();
		scanType.getCvParam().add(XmlWriter110.createScanStartTimeType(scan));
		return scanType;
	}

	private SourceFileListType createSourceFileList(IChromatogram chromatogram) {

		SourceFileListType sourceFileListType = new SourceFileListType();
		sourceFileListType.setCount(BigInteger.valueOf(1));

		File file = chromatogram.getFile();
		SourceFileType sourceFile = XmlWriter110.createSourceFile(file);
		if(sourceFile == null) {
			return null;
		}

		if(chromatogram.getConverterId().equals("org.eclipse.chemclipse.xxd.converter.supplier.chemclipse")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1003374");
			cvParamFileFormat.setName("Open Chromatography Binary OCB format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.mz5")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000560");
			cvParamFileFormat.setName("mz5 format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.cdf")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1002443");
			cvParamFileFormat.setName("Andi-CHROM format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.mzmlb")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000560");
			cvParamFileFormat.setName("mzMLb format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.shimadzu.lcd")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1003009");
			cvParamFileFormat.setName("Shimadzu Biotech LCD format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.waters.micromass")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000526");
			cvParamFileFormat.setName("Waters raw format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.absciex")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000562");
			cvParamFileFormat.setName("ABI WIFF format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.finnigan.raw")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000563");
			cvParamFileFormat.setName("Thermo RAW format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("org.eclipse.chemclipse.msd.converter.supplier.mzdata")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000564");
			cvParamFileFormat.setName("PSI mzData format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("org.eclipse.chemclipse.msd.converter.supplier.mzxml")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000566");
			cvParamFileFormat.setName("ISB mzXML format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.bruker.baf")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000815");
			cvParamFileFormat.setName("Bruker BAF format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.bruker.flex.chromatogram")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1000825");
			cvParamFileFormat.setName("Bruker FID format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("org.eclipse.chemclipse.msd.converter.supplier.mgf")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1001062");
			cvParamFileFormat.setName("Mascot MGF format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		} else if(chromatogram.getConverterId().equals("net.openchrom.msd.converter.supplier.masshunter.msd")) {
			CVParamType cvParamFileFormat = new CVParamType();
			cvParamFileFormat.setCvRef(XmlWriter110.MS);
			cvParamFileFormat.setAccession("MS:1001509");
			cvParamFileFormat.setName("Agilent MassHunter format");
			cvParamFileFormat.setValue("");
			sourceFile.getCvParam().add(cvParamFileFormat);
			sourceFileListType.setCount(BigInteger.valueOf(2));
		}
		sourceFileListType.getSourceFile().add(sourceFile);
		return sourceFileListType;
	}

	private FileDescriptionType createFileDescription(IChromatogram chromatogram, SourceFileListType sourceFiles) {

		FileDescriptionType fileDescriptionType = new FileDescriptionType();
		if(sourceFiles != null) {
			fileDescriptionType.setSourceFileList(sourceFiles);
		}
		fileDescriptionType.setFileContent(createFileContent(chromatogram));
		ParamGroupType paramGroupType = XmlWriter110.createOperator(chromatogram.getOperator());
		if(paramGroupType != null) {
			fileDescriptionType.getContact().add(paramGroupType);
		}
		return fileDescriptionType;
	}

	private ParamGroupType createFileContent(IChromatogram chromatogram) {

		ParamGroupType fileContent = new ParamGroupType();
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			IScanMSD firstScan = chromatogramMSD.getScan(1);
			if(firstScan instanceof IRegularMassSpectrum massSpectrum) {
				fileContent.getCvParam().add(XmlWriter110.createMassSpectrumDimension(massSpectrum));
				fileContent.getCvParam().add(XmlWriter110.createSpectrumType(massSpectrum));
			}
		}
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
		dataProcessing.setId(FrameworkUtil.getBundle(ChromatogramWriterVersion110.class).getSymbolicName());
		dataProcessing.getProcessingMethod().add(XmlWriter110.createExportProcessingMethod(software));
		return dataProcessing;
	}
}
