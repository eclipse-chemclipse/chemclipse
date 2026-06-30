/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorChromatogramWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorScanSignal;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.IVendorScanWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorChromatogramWSD;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorScanSignal;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.model.VendorScanWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.BinaryReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.MetadataReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class ChromatogramWSDReaderVersion110 extends AbstractChromatogramReader implements IChromatogramWSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramWSDReaderVersion110.class);

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramWSD chromatogram = null;
		try {
			MzMLType mzMLwithoutRun = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			chromatogram = new VendorChromatogramWSD();
			chromatogram.setFile(file);
			chromatogram = (IVendorChromatogramWSD)MetadataReader110.readMetadata(mzMLwithoutRun, chromatogram);
			readChromatograms(file, chromatogram);
		} catch(IOException | JAXBException | XMLStreamException e) {
			logger.error(e);
		}
		return chromatogram;
	}

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramWSD chromatogram = null;
		MzMLType mzMLwithoutRun = null;

		try {
			mzMLwithoutRun = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			chromatogram = new VendorChromatogramWSD();
			chromatogram.setFile(file);
			chromatogram = (IVendorChromatogramWSD)MetadataReader110.readMetadata(mzMLwithoutRun, chromatogram);
		} catch(IOException | JAXBException | XMLStreamException e) {
			logger.error(e);
		}
		if(mzMLwithoutRun == null || chromatogram == null) {
			return chromatogram;
		}

		readChromatograms(file, chromatogram);
		readSpectra(file, mzMLwithoutRun, chromatogram, monitor);

		return chromatogram;
	}

	private void readChromatograms(File file, IVendorChromatogramWSD chromatogram) {

		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(file));

			JAXBContext context;

			context = JAXBContext.newInstance(ChromatogramType.class);

			Unmarshaller chromatogramUnmarshaller = context.createUnmarshaller();

			while(reader.hasNext()) {
				int event = reader.next();
				if(event == XMLStreamConstants.START_ELEMENT && "chromatogram".equals(reader.getLocalName())) {
					JAXBElement<ChromatogramType> element = chromatogramUnmarshaller.unmarshal(reader, ChromatogramType.class);
					readSingleWavelengthSignal(element.getValue(), chromatogram);
				}
			}
			reader.close();
		} catch(JAXBException | FileNotFoundException | XMLStreamException e) {
			logger.error(e);
		}
	}

	private void readSpectra(File file, MzMLType mzML, IVendorChromatogramWSD chromatogram, IProgressMonitor monitor) {

		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(file));

			JAXBContext context = JAXBContext.newInstance(SpectrumType.class);
			Unmarshaller spectrumUnmarshaller = context.createUnmarshaller();

			while(reader.hasNext()) {
				if(monitor.isCanceled()) {
					return;
				}
				if(reader.isStartElement()) {
					String localName = reader.getLocalName();
					if("spectrumList".equals(localName)) {
						int spectrumCount = IProgressMonitor.UNKNOWN;
						String countAttr = reader.getAttributeValue(null, "count");
						if(countAttr != null) {
							try {
								spectrumCount = Integer.parseInt(countAttr);
							} catch(NumberFormatException e) {
								logger.warn(e);
							}
						}
						monitor.beginTask(ConverterMessages.readScans, spectrumCount);
						reader.next();
						continue;
					}
					if("spectrum".equals(localName)) {
						JAXBElement<SpectrumType> element = spectrumUnmarshaller.unmarshal(reader, SpectrumType.class);
						SpectrumType spectrum = element.getValue();
						if(spectrum.getCvParam().stream().anyMatch(n -> n.getAccession().equals("MS:1000806") && n.getName().equals("absorption spectrum"))) {
							readSpectrum(spectrum, chromatogram);
						}
						monitor.worked(1);
						// no reader.next() as unmarshal already advanced past </spectrum>
						continue;
					}
				}
				reader.next();
			}
			reader.close();
		} catch(XMLStreamException e) {
			logger.error(e);
		} catch(JAXBException e) {
			logger.error(e);
		} catch(FileNotFoundException e) {
			logger.error(e);
		}
	}

	private void readSpectrum(SpectrumType spectrum, IVendorChromatogramWSD chromatogram) {

		double[] wavelengths = new double[0];
		double[] intensities = new double[0];

		try {
			for(BinaryDataArrayType binaryDataArrayType : spectrum.getBinaryDataArrayList().getBinaryDataArray()) {
				Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
				if(binaryData.getKey().equals("wavelength")) {
					wavelengths = binaryData.getValue();
				} else if(binaryData.getKey().equals("intensity")) {
					intensities = binaryData.getValue();
				}
			}
		} catch(DataFormatException e) {
			logger.error(e);
		}

		if(!chromatogram.getScans().isEmpty()) {
			for(IScan scan : chromatogram.getScans()) {
				if(scan instanceof IVendorScanWSD scanWSD) {
					scanWSD.deleteScanSignals(); // otherwise the total signal is added upon
					addSpectrum(wavelengths, intensities, scanWSD);
				}
			}
		}
	}

	private void readSingleWavelengthSignal(ChromatogramType chromatogramType, IVendorChromatogramWSD chromatogram) {

		double[] retentionTimes = new double[0];
		double[] intensities = new double[0];

		float lowestWavelength = 0f;
		float highestWavelength = 0f;

		try {
			for(CVParamType cvParam : chromatogramType.getCvParam()) {
				if(cvParam.getAccession().equals("MS:1000618") && cvParam.getName().equals("highest observed wavelength")) {
					highestWavelength = Float.parseFloat(cvParam.getValue());
				} else if(cvParam.getAccession().equals("MS:1000619") && cvParam.getName().equals("lowest observed wavelength")) {
					lowestWavelength = Float.parseFloat(cvParam.getValue());
				} else if(cvParam.getAccession().equals("MS:1000812") && cvParam.getName().equals("absorption chromatogram")) {
					for(BinaryDataArrayType binaryDataArrayType : chromatogramType.getBinaryDataArrayList().getBinaryDataArray()) {
						Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
						if(binaryData.getKey().equals("time")) {
							retentionTimes = binaryData.getValue();
						} else if(binaryData.getKey().equals("intensity")) {
							intensities = binaryData.getValue();
						}
					}
				}
			}
			if(lowestWavelength != highestWavelength) {
				logger.warn("Not a single wavelength chromatogram.");
			}
			float wavelength = Math.max(lowestWavelength, highestWavelength);
			addScans(wavelength, intensities, retentionTimes, chromatogram);
		} catch(DataFormatException e) {
			logger.warn(e);
		}
	}

	private void addScans(float wavelength, double[] intensities, double[] retentionTimes, IVendorChromatogramWSD chromatogram) {

		int rt = Math.min(retentionTimes.length, intensities.length);
		for(int i = 0; i < rt; i++) {
			IVendorScanWSD scan = new VendorScanWSD();
			int retentionTime = (int)(retentionTimes[i]);
			scan.setRetentionTime(retentionTime);
			float intensity = (float)intensities[i];
			IVendorScanSignal signal = new VendorScanSignal();
			signal.setAbsorbance(intensity);
			signal.setWavelength(wavelength);
			scan.addScanSignal(signal);
			chromatogram.addScan(scan);
		}
	}

	private void addSpectrum(double[] wavelengths, double[] intensities, IVendorScanWSD scan) {

		int max = Math.min(wavelengths.length, intensities.length);
		for(int i = 0; i < max; i++) {
			IVendorScanSignal signal = new VendorScanSignal();
			signal.setAbsorbance((float)intensities[i]);
			signal.setWavelength((float)wavelengths[i]);
			scan.addScanSignal(signal);
		}
	}
}
