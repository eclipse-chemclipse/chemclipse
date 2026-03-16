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

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.DataProcessing;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Maldi;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v20.model.Software;
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

public class MassSpectrumReaderVersion20 extends AbstractMassSpectraReader {

	public static final String VERSION = "mzXML_2.0";

	private static final Logger logger = Logger.getLogger(MassSpectrumReaderVersion20.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		IStandaloneMassSpectrum massSpectrum = null;

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(AbstractReader.NODE_MS_RUN);

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			MsRun msRun = (MsRun)unmarshaller.unmarshal(nodeList.item(0));

			massSpectrum = new StandaloneMassSpectrum();
			massSpectrum.setFile(file);
			massSpectrum.setIdentifier(file.getName());
			readDataProcessing(msRun.getDataProcessing(), massSpectrum);

			List<Scan> scans = msRun.getScan();
			monitor.beginTask(ConverterMessages.readScans, scans.size());
			for(Scan scan : scans) {
				/*
				 * Polarity
				 */
				String polarity = scan.getPolarity();
				if(polarity != null && !polarity.isEmpty()) {
					if(polarity.equals("+")) {
						massSpectrum.setPolarity(Polarity.POSITIVE);
					} else if(polarity.equals("-")) {
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
				/*
				 * Get the ions.
				 */
				Peaks peaks = scan.getPeaks();
				double[] values = ByteReaderVersion2.readValues(peaks.getValue(), peaks.getByteOrder(), peaks.getPrecision());
				for(int peakIndex = 0; peakIndex < values.length - 1; peakIndex += 2) {
					/*
					 * Get m/z and intensity (m/z-int)
					 */
					massSpectrum.addIon(new VendorIon(values[peakIndex], (float)values[peakIndex + 1]), false);
				}
				monitor.worked(1);
			}
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}

		IVendorMassSpectra massSpectra = new VendorMassSpectra();
		massSpectra.setName(file.getName());
		massSpectra.addMassSpectrum(massSpectrum);
		return massSpectra;
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
				Date date = new Date();
				XMLGregorianCalendar completionTime = software.getCompletionTime();
				if(completionTime != null) {
					date = completionTime.toGregorianCalendar().getTime();
				}
				massSpectrum.getEditHistory().add(new EditInformation(date, software.getType(), editor));
			}
		}
	}
}
