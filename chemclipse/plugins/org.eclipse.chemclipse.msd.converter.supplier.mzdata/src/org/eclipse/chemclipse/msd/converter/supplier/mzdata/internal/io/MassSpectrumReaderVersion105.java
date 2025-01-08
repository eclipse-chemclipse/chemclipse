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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorMassSpectra;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class MassSpectrumReaderVersion105 extends AbstractMassSpectraReader implements IMassSpectraReader {

	public static final String VERSION = "1.05";
	//
	private static final Logger logger = Logger.getLogger(MassSpectrumReaderVersion105.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		IStandaloneMassSpectrum massSpectrum = null;
		//
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(ReaderVersion105.NODE_MZ_DATA);
			//
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			MzData mzData = (MzData)unmarshaller.unmarshal(nodeList.item(0));
			//
			massSpectrum = new StandaloneMassSpectrum();
			massSpectrum.setFile(file);
			massSpectrum.setIdentifier(file.getName());
			massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
			readDescription(mzData, massSpectrum);
			readIons(mzData, massSpectrum);
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}
		//
		IVendorMassSpectra massSpectra = new VendorMassSpectra();
		massSpectra.setName(file.getName());
		massSpectra.addMassSpectrum(massSpectrum);
		return massSpectra;
	}

	private void readDescription(MzData mzData, IStandaloneMassSpectrum massSpectrum) {

		Description description = mzData.getDescription();
		if(description != null) {
			readAdmin(description, massSpectrum);
			massSpectrum.setInstrument(description.getInstrument().getInstrumentName());
			readProcessingMethod(description, massSpectrum);
		}
	}

	private void readProcessingMethod(Description description, IStandaloneMassSpectrum massSpectrum) {

		ParamType processingMethod = description.getDataProcessing().getProcessingMethod();
		if(processingMethod == null) {
			return;
		}
		for(Object object : processingMethod.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				if(cvParamType.getName().equals("peakProcessing")) {
					if(cvParamType.getValue().equals("centroided")) {
						massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
					}
				}
			}
		}
	}

	private void readAdmin(Description description, IStandaloneMassSpectrum massSpectrum) {

		AdminType admin = description.getAdmin();
		if(admin == null) {
			return;
		}
		massSpectrum.setSampleName(admin.getSampleName());
		if(admin.getSampleDescription() != null) {
			massSpectrum.setDescription(admin.getSampleDescription().getComment());
		}
		for(PersonType contact : admin.getContact()) {
			String contactDetails = "";
			if(contact.getContactInfo() != null) {
				contactDetails = String.join(", ", contact.getName(), contact.getInstitution(), contact.getContactInfo());
			} else {
				contactDetails = String.join(", ", contact.getName(), contact.getInstitution());
			}
			if(massSpectrum.getOperator() == null || massSpectrum.getOperator().isEmpty()) {
				massSpectrum.setOperator(contactDetails);
			} else {
				massSpectrum.setOperator(String.join(", ", massSpectrum.getOperator(), contactDetails));
			}
		}
	}

	private void readIons(MzData mzData, IStandaloneMassSpectrum massSpectrum) {

		Spectrum spectrum = mzData.getSpectrumList().getSpectrum().get(0);
		double[] mzs = ReaderVersion105.parseData(spectrum.getMzArrayBinary().getData());
		double[] intensities = ReaderVersion105.parseData(spectrum.getIntenArrayBinary().getData());
		int length = Math.min(mzs.length, intensities.length);
		for(int i = 0; i < length; i++) {
			massSpectrum.addIon(new VendorIon(mzs[i], (float)intensities[i]), false);
		}
	}
}
