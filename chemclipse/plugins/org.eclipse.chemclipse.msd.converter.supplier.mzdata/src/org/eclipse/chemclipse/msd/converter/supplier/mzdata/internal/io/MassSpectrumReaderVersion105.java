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
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AcqSpecification;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.AdminType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.CvParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.DataProcessingType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Description;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.MzData;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ObjectFactory;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.ParamType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.PersonType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Software;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.Spectrum;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumDescType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.v105.model.SpectrumSettingsType;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.IVendorMassSpectra;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzdata.model.VendorMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class MassSpectrumReaderVersion105 extends AbstractMassSpectraReader {

	public static final String VERSION = "1.05";

	private static final Logger logger = Logger.getLogger(MassSpectrumReaderVersion105.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		IVendorMassSpectra massSpectra = new VendorMassSpectra();
		massSpectra.setName(file.getName());

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(ReaderVersion105.NODE_MZ_DATA);

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			MzData mzData = (MzData)unmarshaller.unmarshal(nodeList.item(0));

			for(Spectrum spectrum : mzData.getSpectrumList().getSpectrum()) {
				IStandaloneMassSpectrum massSpectrum = new StandaloneMassSpectrum();
				massSpectrum.setFile(file);
				massSpectrum.setIdentifier(file.getName());
				massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
				readDescription(mzData, massSpectrum);
				readIons(spectrum, massSpectrum);
				massSpectra.addMassSpectrum(massSpectrum);
			}

		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		}

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

		DataProcessingType dataProcessing = description.getDataProcessing();
		if(dataProcessing == null) {
			return;
		}
		ParamType processingMethod = dataProcessing.getProcessingMethod();
		if(processingMethod == null) {
			return;
		}
		for(Object object : processingMethod.getCvParamOrUserParam()) {
			if(object instanceof CvParamType cvParamType) {
				String action = cvParamType.getName() + ": " + cvParamType.getValue();
				String editor = "";
				Date date = new Date();
				Software software = dataProcessing.getSoftware();
				if(software != null) {
					editor = software.getName() + " " + software.getVersion();
					XMLGregorianCalendar completionTime = software.getCompletionTime();
					if(completionTime != null) {
						date = completionTime.toGregorianCalendar().getTime();
					}
				}
				massSpectrum.getEditHistory().add(new EditInformation(date, action, editor));
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

	private void readIons(Spectrum spectrum, IStandaloneMassSpectrum massSpectrum) {

		readSpectrumDescription(spectrum, massSpectrum);
		double[] mzs = ReaderVersion105.parseData(spectrum.getMzArrayBinary().getData());
		double[] intensities = ReaderVersion105.parseData(spectrum.getIntenArrayBinary().getData());
		int length = Math.min(mzs.length, intensities.length);
		for(int i = 0; i < length; i++) {
			massSpectrum.addIon(new VendorIon(mzs[i], (float)intensities[i]), false);
		}
	}

	private void readSpectrumDescription(Spectrum spectrum, IStandaloneMassSpectrum massSpectrum) {

		SpectrumDescType spectrumDescription = spectrum.getSpectrumDesc();
		if(spectrumDescription == null) {
			return;
		}
		SpectrumSettingsType settings = spectrumDescription.getSpectrumSettings();
		if(settings == null) {
			return;
		}
		AcqSpecification acquisitionSpecification = settings.getAcqSpecification();
		if(acquisitionSpecification == null) {
			return;
		}
		if("discrete".equals(acquisitionSpecification.getSpectrumType())) {
			massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
		} else if("continuous".equals(acquisitionSpecification.getSpectrumType())) {
			massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		}
	}
}
