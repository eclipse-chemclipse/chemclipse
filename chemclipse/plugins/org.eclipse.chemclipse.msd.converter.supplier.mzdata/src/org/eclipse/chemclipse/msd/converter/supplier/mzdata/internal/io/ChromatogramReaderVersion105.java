/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DataProcessingType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PrecursorList;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PrecursorType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumInstrument;
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

public class ChromatogramReaderVersion105 extends AbstractChromatogramReader implements IChromatogramMSDReader {

	public static final String VERSION = "1.05";
	//
	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion105.class);

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		try {
			MzData mzData = readMzData(file);
			/*
			 * Metadata
			 */
			chromatogram = new VendorChromatogram();
			Description description = mzData.getDescription();
			if(description != null) {
				AdminType admin = description.getAdmin();
				chromatogram.setSampleName(admin.getSampleName());
				readOperator(admin, chromatogram);
				chromatogram.setInstrument(description.getInstrument().getInstrumentName());
			}
			readEditHistory(mzData, chromatogram);
			/*
			 * Mass Spectra
			 */
			for(Spectrum spectrum : mzData.getSpectrumList().getSpectrum()) {
				IVendorScan massSpectrum = new VendorScan();
				SpectrumInstrument spectrumInstrument = spectrum.getSpectrumDesc().getSpectrumSettings().getSpectrumInstrument();
				setPolarity(spectrumInstrument, massSpectrum);
				massSpectrum.setRetentionTime(readRetentionTime(spectrumInstrument));
				/*
				 * MS/MS
				 */
				massSpectrum.setMassSpectrometer((short)spectrumInstrument.getMsLevel());
				int cycleNumber = spectrumInstrument.getMsLevel() > 1 ? 1 : 0;
				if(massSpectrum.getMassSpectrometer() < 2) {
					cycleNumber++;
				}
				if(cycleNumber >= 1) {
					massSpectrum.setCycleNumber(cycleNumber);
				}
				setPrecursor(spectrum.getSpectrumDesc(), massSpectrum);
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

	private MzData readMzData(File file) throws SAXException, IOException, ParserConfigurationException, JAXBException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		NodeList nodeList = document.getElementsByTagName(ReaderVersion105.NODE_MZ_DATA);
		//
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (MzData)unmarshaller.unmarshal(nodeList.item(0));
	}

	private void readIons(Spectrum spectrum, IVendorScan massSpectrum, IVendorChromatogram chromatogram) {

		double[] mz = ReaderVersion105.parseData(spectrum.getMzArrayBinary().getData());
		double[] intensities = ReaderVersion105.parseData(spectrum.getIntenArrayBinary().getData());
		int length = Math.min(mz.length, intensities.length);
		for(int index = 0; index < length; index++) {
			float intensity = (float)intensities[index];
			if(massSpectrum.getPrecursorIon() != 0) {
				double collisionEnergy = getCollisionEnergy(spectrum.getSpectrumDesc());
				IIonTransition ionTransition = new IonTransition(massSpectrum.getPrecursorIon(), mz[index], collisionEnergy, 1, 1, 0);
				massSpectrum.addIon(new VendorIon(mz[index], intensity, ionTransition), false);
				chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);
			} else {
				massSpectrum.addIon(new VendorIon(mz[index], intensity), false);
			}
		}
		chromatogram.addScan(massSpectrum);
	}

	private void setPrecursor(SpectrumDescType spectrumDesc, IVendorScan massSpectrum) {

		PrecursorList precursorList = spectrumDesc.getPrecursorList();
		if(precursorList == null) {
			return;
		}
		for(PrecursorType precursor : precursorList.getPrecursor()) {
			for(Object object : precursor.getIonSelection().getCvParamOrUserParam()) {
				if(object instanceof CvParamType cvParamType) {
					if(cvParamType.getName().equals("MassToChargeRatio") && cvParamType.getAccession().equals("PSI:1000040")) {
						massSpectrum.setPrecursorIon(Double.parseDouble(cvParamType.getValue()));
					}
				}
			}
		}
	}

	private double getCollisionEnergy(SpectrumDescType spectrumDesc) {

		for(PrecursorType precursor : spectrumDesc.getPrecursorList().getPrecursor()) {
			for(Object object : precursor.getActivation().getCvParamOrUserParam()) {
				if(object instanceof CvParamType cvParamType) {
					if(cvParamType.getName().equals("CollisionEnergy") && cvParamType.getAccession().equals("PSI:1000045")) {
						return Double.parseDouble(cvParamType.getValue());
					}
				}
			}
		}
		return 0;
	}

	private int readRetentionTime(SpectrumInstrument spectrumInstrument) {

		int retentionTime = 0;
		for(Object object : spectrumInstrument.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				if(cvParamType.getName().equals("TimeInSeconds") && cvParamType.getAccession().equals("PSI:1000039")) {
					retentionTime = (int)Math.round(Double.parseDouble(cvParamType.getValue()) * IChromatogramOverview.SECOND_CORRELATION_FACTOR);
				} else if(cvParamType.getName().equals("TimeInMinutes") && cvParamType.getAccession().equals("PSI:1000038")) {
					retentionTime = (int)Math.round(Double.parseDouble(cvParamType.getValue()) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
				}
			}
		}
		return retentionTime;
	}

	private void setPolarity(SpectrumInstrument spectrumInstrument, IVendorScan massSpectrum) {

		for(Object object : spectrumInstrument.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				if(cvParamType.getName().equals("Polarity") && cvParamType.getAccession().equals("PSI:1000037")) {
					if(cvParamType.getValue().equals("Positive")) {
						massSpectrum.setPolarity(Polarity.POSITIVE);
					} else if(cvParamType.getValue().equals("Negative")) {
						massSpectrum.setPolarity(Polarity.NEGATIVE);
					}
				}
			}
		}
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
