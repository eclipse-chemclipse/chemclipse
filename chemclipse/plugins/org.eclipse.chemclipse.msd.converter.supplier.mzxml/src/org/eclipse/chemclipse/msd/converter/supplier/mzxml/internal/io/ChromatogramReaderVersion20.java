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
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.DataProcessing;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.MsInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.PrecursorMz;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
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

public class ChromatogramReaderVersion20 extends AbstractChromatogramReader {

	public static final String VERSION = "mzXML_2.0";

	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion20.class);

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		MsRun msRun = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(AbstractReader.NODE_MS_RUN);

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			msRun = (MsRun)unmarshaller.unmarshal(nodeList.item(0));
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}

		chromatogram = new VendorChromatogram();
		boolean isTandemMeasurement = isTandemMeasurement(msRun);
		int cycleNumber = isTandemMeasurement ? 1 : 0;

		MsInstrument instrument = msRun.getMsInstrument();
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

		for(DataProcessing processing : msRun.getDataProcessing()) {
			Software software = processing.getSoftware();
			chromatogram.getEditHistory().add(new EditInformation(software.getType(), software.getName() + " " + software.getVersion()));
		}

		readScans(msRun, cycleNumber, chromatogram, monitor);

		chromatogram.setConverterId("");
		chromatogram.setFile(file);

		return chromatogram;
	}

	private void readScans(MsRun msRun, int cycleNumber, IChromatogramMSD chromatogram, IProgressMonitor monitor) {

		List<Scan> scans = msRun.getScan();
		monitor.beginTask(ConverterMessages.readScans, scans.size());
		for(Scan scan : scans) {
			IVendorScan massSpectrum = readScan(scan, chromatogram);
			massSpectrum.setCycleNumber(cycleNumber);
			chromatogram.addScan(massSpectrum);
			for(Scan embeddedScan : scan.getScan()) {
				IVendorScan embeddedMassSpectrum = readScan(embeddedScan, chromatogram);
				embeddedMassSpectrum.setCycleNumber(cycleNumber);
				chromatogram.addScan(embeddedMassSpectrum);
			}
			cycleNumber++;
			monitor.worked(1);
		}
	}

	private IVendorScan readScan(Scan scan, IChromatogramMSD chromatogram) {

		IVendorScan massSpectrum = new VendorScan();

		setRetentionTime(scan, massSpectrum);
		setPolarity(scan, massSpectrum);

		// MS, MS/MS
		massSpectrum.setMassSpectrometer(scan.getMsLevel().shortValue());

		if(!scan.getPrecursorMz().isEmpty()) {
			PrecursorMz precursor = scan.getPrecursorMz().get(0);
			massSpectrum.setPrecursorIon(precursor.getValue());
		}

		Peaks peaks = scan.getPeaks();
		if(peaks == null) {
			if(scan.getTotIonCurrent() != 0) {
				massSpectrum.addIon(new VendorIon(IIon.TIC_ION, scan.getTotIonCurrent()), false);
			}
			return massSpectrum;
		}
		/*
		 * Get the ions.
		 */
		double[] values = ByteReaderVersion2.readValues(peaks.getValue(), peaks.getByteOrder(), peaks.getPrecision());
		readIons(values, scan, massSpectrum, chromatogram);

		return massSpectrum;
	}

	private void readIons(double[] values, Scan scan, IVendorScan massSpectrum, IChromatogramMSD chromatogram) {

		for(int peakIndex = 0; peakIndex < values.length - 1; peakIndex += 2) {
			/*
			 * Get m/z and intensity (m/z-int)
			 */
			double mz = values[peakIndex];
			if(mz == 0) {
				continue;
			}
			float intensity = (float)values[peakIndex + 1];
			if(intensity == 0) {
				continue;
			}
			if(massSpectrum.getMassSpectrometer() >= 2) {
				float collisionEnergy = scan.getCollisionEnergy() != null ? scan.getCollisionEnergy().floatValue() : 0f;
				IIonTransition ionTransition = new IonTransition(massSpectrum.getPrecursorIon(), mz, collisionEnergy, 1, 1, 0);
				massSpectrum.addIon(new VendorIon(mz, intensity, ionTransition), false);
				chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);
			} else {
				massSpectrum.addIon(new VendorIon(mz, intensity), false);
			}
		}
	}

	private void setRetentionTime(Scan scan, IVendorScan massSpectrum) {

		int retentionTime = scan.getRetentionTime().multiply(1000).getSeconds(); // milliseconds
		massSpectrum.setRetentionTime(retentionTime);
	}

	private void setPolarity(Scan scan, IVendorScan massSpectrum) {

		String polarity = scan.getPolarity();
		if(polarity != null) {
			if(polarity.equals("+")) {
				massSpectrum.setPolarity(Polarity.POSITIVE);
			} else if(polarity.equals("-")) {
				massSpectrum.setPolarity(Polarity.NEGATIVE);
			}
		}
	}

	private boolean isTandemMeasurement(MsRun msRun) {

		for(Scan scan : msRun.getScan()) {
			if(scan.getMsLevel().shortValue() > 1) {
				return true;
			}
		}
		return false;
	}
}
