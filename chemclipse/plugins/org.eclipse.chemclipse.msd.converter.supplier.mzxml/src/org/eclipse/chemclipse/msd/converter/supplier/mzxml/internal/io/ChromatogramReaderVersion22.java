/*******************************************************************************
 * Copyright (c) 2015, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.DataProcessing;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.MsInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.PrecursorMz;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v22.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class ChromatogramReaderVersion22 extends AbstractChromatogramReaderVersion implements IChromatogramMSDReader {

	public static final String VERSION = "mzXML_2.2";
	//
	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion22.class);

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		//
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(NODE_MS_RUN);
			//
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			MsRun msrun = (MsRun)unmarshaller.unmarshal(nodeList.item(0));
			//
			chromatogram = new VendorChromatogram();
			boolean isTandemMeasurement = isTandemMeasurement(msrun);
			int cycleNumber = isTandemMeasurement ? 1 : 0;
			//
			MsInstrument instrument = msrun.getMsInstrument();
			if(instrument != null) {
				chromatogram.setInstrument(instrument.getMsManufacturer().getTheValue() + " " + instrument.getMsModel().getTheValue());
				chromatogram.setIonisation(instrument.getMsIonisation().getTheValue());
				chromatogram.setMassAnalyzer(instrument.getMsMassAnalyzer().getTheValue());
				chromatogram.setMassDetector(instrument.getMsDetector().getTheValue());
				Software software = instrument.getSoftware();
				if(software != null) {
					chromatogram.setSoftware(software.getName() + " " + software.getVersion());
				}
			}
			for(DataProcessing processing : msrun.getDataProcessing()) {
				Software software = processing.getSoftware();
				chromatogram.getEditHistory().add(new EditInformation(software.getType(), software.getName() + " " + software.getVersion()));
			}
			List<Scan> scans = msrun.getScan();
			monitor.beginTask(ConverterMessages.readScans, scans.size());
			for(Scan scan : scans) {
				/*
				 * Get the mass spectra.
				 */
				IVendorScan massSpectrum = new VendorScan();
				int retentionTime = scan.getRetentionTime().multiply(1000).getSeconds(); // milliseconds
				massSpectrum.setRetentionTime(retentionTime);
				/*
				 * Polarity
				 */
				String polarity = scan.getPolarity();
				if(polarity != null) {
					if(polarity.equals("+")) {
						massSpectrum.setPolarity(Polarity.POSITIVE);
					} else if(polarity.equals("-")) {
						massSpectrum.setPolarity(Polarity.NEGATIVE);
					}
				}
				// MS, MS/MS
				short msLevel = scan.getMsLevel().shortValue();
				massSpectrum.setMassSpectrometer(msLevel);
				//
				if(!scan.getPrecursorMz().isEmpty()) {
					PrecursorMz precursor = scan.getPrecursorMz().get(0);
					massSpectrum.setPrecursorIon(precursor.getValue());
				}
				//
				if(msLevel < 2) {
					cycleNumber++;
				}
				if(cycleNumber >= 1) {
					massSpectrum.setCycleNumber(cycleNumber);
				}
				massSpectrum.setRetentionTime(retentionTime);
				/*
				 * Get the ions.
				 */
				Peaks peaks = scan.getPeaks();
				ByteBuffer byteBuffer = ByteBuffer.wrap(peaks.getValue());
				/*
				 * Byte Order
				 */
				String byteOrder = peaks.getByteOrder();
				if(byteOrder != null && byteOrder.equals("network")) {
					byteBuffer.order(ByteOrder.BIG_ENDIAN);
				} else {
					byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
				}
				/*
				 * Precision
				 */
				double[] values;
				BigInteger precision = peaks.getPrecision();
				if(precision != null && precision.intValue() == 64) {
					DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
					values = new double[doubleBuffer.capacity()];
					for(int index = 0; index < doubleBuffer.capacity(); index++) {
						values[index] = doubleBuffer.get(index);
					}
				} else {
					FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
					values = new double[floatBuffer.capacity()];
					for(int index = 0; index < floatBuffer.capacity(); index++) {
						values[index] = floatBuffer.get(index);
					}
				}
				for(int peakIndex = 0; peakIndex < values.length - 1; peakIndex += 2) {
					/*
					 * Get m/z and intensity (m/z-int)
					 */
					double mz = values[peakIndex];
					float intensity = (float)values[peakIndex + 1];
					if(msLevel >= 2) {
						IIonTransition ionTransition = new IonTransition(massSpectrum.getPrecursorIon(), mz, scan.getCollisionEnergy(), 1, 1, 0);
						massSpectrum.addIon(new VendorIon(mz, intensity, ionTransition), false);
						chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);
					} else {
						massSpectrum.addIon(new VendorIon(mz, intensity), false);
					}
				}
				chromatogram.addScan(massSpectrum);
				monitor.worked(1);
			}
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}
		chromatogram.setConverterId("");
		chromatogram.setFile(file);
		return chromatogram;
	}

	private boolean isTandemMeasurement(MsRun msrun) {

		for(Scan scan : msrun.getScan()) {
			if(scan.getMsLevel().shortValue() > 1) {
				return true;
			}
		}
		return false;
	}
}
