/*******************************************************************************
 * Copyright (c) 2021, 2024 Lablicate GmbH.
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.AcqDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.AcqDescType.PrecursorList;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.AcqSettingsType.AcqInstrument;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.DataProcessingType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.DataProcessingType.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.MzData.SpectrumList.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.ParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v104.model.PrecursorType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorScan;
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

public class ChromatogramReaderVersion104 extends AbstractChromatogramReader implements IChromatogramMSDReader {

	public static final String VERSION = "1.04";
	//
	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion104.class);

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		try {
			MzData mzData = readMzData(file);
			chromatogram = new VendorChromatogram();
			/*
			 * Metadata
			 */
			AdminType admin = mzData.getDescription().getAdmin();
			chromatogram.setSampleName(admin.getSampleName());
			readOperator(admin, chromatogram);
			chromatogram.setInstrument(mzData.getDescription().getInstrument().getInstrumentName());
			readEditHistory(mzData, chromatogram);
			/*
			 * Mass Spectra
			 */
			for(Spectrum spectrum : mzData.getSpectrumList().getSpectrum()) {
				IVendorScan massSpectrum = new VendorScan();
				AcqInstrument acqInstrument = spectrum.getAcqDesc().getAcqSettings().getAcqInstrument();
				setPolarity(acqInstrument, massSpectrum);
				massSpectrum.setRetentionTime(readRetentionTime(acqInstrument));
				/*
				 * MS/MS
				 */
				massSpectrum.setMassSpectrometer((short)acqInstrument.getMsLevel());
				int cycleNumber = acqInstrument.getMsLevel() > 1 ? 1 : 0;
				if(massSpectrum.getMassSpectrometer() < 2) {
					cycleNumber++;
				}
				if(cycleNumber >= 1) {
					massSpectrum.setCycleNumber(cycleNumber);
				}
				setPrecursor(spectrum.getAcqDesc(), massSpectrum);
				/*
				 * m/z
				 */
				readIons(spectrum, massSpectrum, chromatogram);
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

	private void readIons(Spectrum spectrum, IVendorScan massSpectrum, IVendorChromatogram chromatogram) {

		double[] mz = ReaderVersion104.parseData(spectrum.getMzArrayBinary().getData());
		double[] intensities = ReaderVersion104.parseData(spectrum.getIntenArrayBinary().getData());
		int length = Math.min(mz.length, intensities.length);
		for(int index = 0; index < length; index++) {
			float intensity = (float)intensities[index];
			if(massSpectrum.getPrecursorIon() != 0) {
				double collisionEnergy = getCollisionEnergy(spectrum.getAcqDesc());
				IIonTransition ionTransition = new IonTransition(massSpectrum.getPrecursorIon(), mz[index], collisionEnergy, 1, 1, 0);
				massSpectrum.addIon(new VendorIon(mz[index], intensity, ionTransition), false);
				chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);
			} else {
				massSpectrum.addIon(new VendorIon(mz[index], intensity), false);
			}
		}
		chromatogram.addScan(massSpectrum);
	}

	private double getCollisionEnergy(AcqDescType acqDesc) {

		for(PrecursorType precursor : acqDesc.getPrecursorList().getPrecursor()) {
			for(Object object : precursor.getActivation().getCvParamOrUserParam()) {
				if(object instanceof CvParamType cvParamType) {
					if(cvParamType.getName().equals("energy")) {
						return Double.parseDouble(cvParamType.getValue());
					}
				}
			}
		}
		return 0;
	}

	private void setPrecursor(AcqDescType acqDesc, IVendorScan massSpectrum) {

		PrecursorList precursorList = acqDesc.getPrecursorList();
		if(precursorList == null) {
			return;
		}
		for(PrecursorType precursor : precursorList.getPrecursor()) {
			for(Object object : precursor.getIonSelection().getCvParamOrUserParam()) {
				if(object instanceof CvParamType cvParamType) {
					if(cvParamType.getName().equals("mz")) {
						massSpectrum.setPrecursorIon(Double.parseDouble(cvParamType.getValue()));
					}
				}
			}
		}
	}

	private int readRetentionTime(AcqInstrument acqInstrument) {

		int retentionTime = 0;
		for(Object object : acqInstrument.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				if(cvParamType.getName().equals("time.min")) {
					retentionTime = (int)Math.round(Double.parseDouble(cvParamType.getValue()) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
				}
			}
		}
		return retentionTime;
	}

	private void setPolarity(AcqInstrument acqInstrument, IVendorScan massSpectrum) {

		for(Object object : acqInstrument.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				if(cvParamType.getName().equals("polarity")) {
					if(cvParamType.getValue().equals("+")) {
						massSpectrum.setPolarity(Polarity.POSITIVE);
					} else if(cvParamType.getValue().equals("-")) {
						massSpectrum.setPolarity(Polarity.NEGATIVE);
					}
				}
			}
		}
	}

	private MzData readMzData(File file) throws SAXException, IOException, ParserConfigurationException, JAXBException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		NodeList nodeList = document.getElementsByTagName(ReaderVersion104.NODE_MZ_DATA);
		//
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (MzData)unmarshaller.unmarshal(nodeList.item(0));
	}

	private void readOperator(AdminType admin, IVendorChromatogram chromatogram) {

		for(PersonType contact : admin.getContact()) {
			String contactDetails = String.join(", ", contact.getName(), contact.getInstitution(), contact.getContactInfo());
			if(chromatogram.getOperator().isEmpty()) {
				chromatogram.setOperator(contactDetails);
			} else {
				chromatogram.setOperator(String.join(", ", chromatogram.getOperator(), contactDetails));
			}
		}
	}

	private void readEditHistory(MzData mzData, IVendorChromatogram chromatogram) {

		DataProcessingType dataProcessing = mzData.getDescription().getDataProcessing();
		Software software = dataProcessing.getSoftware();
		ParamType processingMethod = dataProcessing.getProcessingMethod();
		if(processingMethod != null) {
			for(Object object : processingMethod.getCvParamOrUserParam()) {
				if(object instanceof CvParamType cvParamType) {
					chromatogram.getEditHistory().add(new EditInformation(cvParamType.getName(), software.getName() + " " + software.getVersion()));
				}
			}
		}
	}
}
