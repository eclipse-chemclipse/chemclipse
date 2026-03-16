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
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.DataProcessing;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.Maldi;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.MsInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v30.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorMassSpectra;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class MassSpectrumReaderVersion30 extends AbstractMassSpectraReader {

	public static final String VERSION = "mzXML_3.0";

	private static final Logger logger = Logger.getLogger(MassSpectrumReaderVersion30.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

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
		IVendorMassSpectra massSpectra = createMassSpectra(file, msRun, monitor);
		massSpectra.setName(file.getName());
		return massSpectra;
	}

	private IVendorMassSpectra createMassSpectra(File file, MsRun msRun, IProgressMonitor monitor) {

		IVendorMassSpectra massSpectra = new VendorMassSpectra();

		List<Scan> scans = msRun.getScan();
		monitor.beginTask(ConverterMessages.readScans, scans.size());

		for(Scan scan : scans) {
			IStandaloneMassSpectrum massSpectrum = new StandaloneMassSpectrum();
			readDataProcessing(msRun.getDataProcessing(), massSpectrum);
			for(MsInstrument instrument : msRun.getMsInstrument()) {
				massSpectrum.setInstrument(instrument.getMsManufacturer().getTheValue() + " " + instrument.getMsModel().getTheValue());
			}
			massSpectrum.setFile(file);
			massSpectrum.setIdentifier(file.getName());
			/*
			 * Polarity
			 */
			if(scan.getPolarity() != null) {
				if(scan.getPolarity().equals("+")) {
					massSpectrum.setPolarity(Polarity.POSITIVE);
				} else if(scan.getPolarity().equals("-")) {
					massSpectrum.setPolarity(Polarity.NEGATIVE);
				}
			}
			/*
			 * Plate
			 */
			Maldi maldi = scan.getMaldi();
			if(maldi != null) {
				massSpectrum.setPlate(maldi.getPlateID());
				massSpectrum.setPosition(maldi.getSpotID());
			}
			for(Peaks peaks : scan.getPeaks()) {
				readIons(peaks, massSpectrum);
			}
			massSpectra.addMassSpectrum(massSpectrum);
		}
		return massSpectra;
	}

	private void readIons(Peaks peaks, IStandaloneMassSpectrum massSpectrum) {

		double[] values = null;
		try {
			values = ByteReaderVersion3.readValues(peaks.getValue(), peaks.getByteOrder(), peaks.getPrecision(), peaks.getCompressionType());
		} catch(DataFormatException e) {
			logger.error(e);
		}
		if(values == null) {
			return;
		}
		for(int peakIndex = 0; peakIndex < values.length - 1; peakIndex += 2) {
			/*
			 * Get m/z and intensity (m/z-int)
			 */
			massSpectrum.addIon(new VendorIon(values[peakIndex], (float)values[peakIndex + 1]), false);
		}
	}

	private void readDataProcessing(List<DataProcessing> dataProcessings, IStandaloneMassSpectrum massSpectrum) {

		for(DataProcessing dataProcessing : dataProcessings) {
			if(Boolean.TRUE.equals(dataProcessing.isCentroided())) {
				massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
			} else {
				massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
			}
			Software software = dataProcessing.getSoftware();
			if(software != null) {
				String editor = software.getName() + " " + software.getVersion();
				Date date = software.getCompletionTime().toGregorianCalendar().getTime();
				massSpectrum.getEditHistory().add(new EditInformation(date, software.getType(), editor));
			}
		}
	}
}
