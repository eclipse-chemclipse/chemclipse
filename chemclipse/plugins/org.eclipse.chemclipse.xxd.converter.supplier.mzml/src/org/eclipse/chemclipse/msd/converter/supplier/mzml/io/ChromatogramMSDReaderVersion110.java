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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramReader;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorIonMSn;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorChromatogramMSD;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorIonMSn;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.core.Polarity;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.msd.model.implementation.RegularMassSpectrum;
import org.eclipse.chemclipse.support.history.EditInformation;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.BinaryReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.MetadataReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ChromatogramType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.DataProcessingType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.PrecursorType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ProcessingMethodType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ProductType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ReferenceableParamGroupRefType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ReferenceableParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ScanType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SelectedIonListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SoftwareType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.UserParamType;
import org.eclipse.core.runtime.IProgressMonitor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class ChromatogramMSDReaderVersion110 extends AbstractChromatogramReader implements IChromatogramMSDReader {

	private static final Logger logger = Logger.getLogger(ChromatogramMSDReaderVersion110.class);

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramMSD chromatogram = null;
		try {
			MzMLType mzMLwithoutRun = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			chromatogram = new VendorChromatogramMSD();
			MetadataReader110.readMetadata(mzMLwithoutRun, chromatogram);
		} catch(IOException | JAXBException | XMLStreamException e) {
			logger.error(e);
		}

		readChromatograms(file, chromatogram, true);
		return chromatogram;
	}

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws IOException {

		IVendorChromatogramMSD chromatogram = null;
		MzMLType mzMLwithoutRun = null;
		try {
			mzMLwithoutRun = XmlHelper.parseFiltered(file, MzMLType.class, "mzML", "run");
			chromatogram = new VendorChromatogramMSD();
			chromatogram.setFile(file);
			MetadataReader110.readMetadata(mzMLwithoutRun, chromatogram);
			readEditHistory(mzMLwithoutRun, chromatogram);
		} catch(IOException | JAXBException | XMLStreamException e) {
			logger.error(e);
		}

		if(mzMLwithoutRun == null || chromatogram == null) {
			return chromatogram;
		}

		readSpectra(file, mzMLwithoutRun, chromatogram, monitor);
		if(chromatogram.getNumberOfScans() == 0) {
			readChromatograms(file, chromatogram, false);
		}

		return chromatogram;
	}

	private void readSpectra(File file, MzMLType mzML, IVendorChromatogramMSD chromatogram, IProgressMonitor monitor) {

		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(file));

			JAXBContext context = JAXBContext.newInstance(SpectrumType.class);
			Unmarshaller spectrumUnmarshaller = context.createUnmarshaller();

			int cycleNumber = isMultiStageMassSpectrum(mzML) ? 1 : 0;
			Map<String, ReferenceableParamGroupType> paramGroups = MetadataReader110.buildParamGroupMap(mzML);

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
						if(spectrum.getCvParam().stream().noneMatch(n -> n.getAccession().equals("MS:1000806") && n.getName().equals("absorption spectrum"))) {
							readSpectrum(element.getValue(), cycleNumber, chromatogram, paramGroups);
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

	private void readChromatograms(File file, IVendorChromatogramMSD chromatogram, boolean overview) {

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
					readTIC(element.getValue(), chromatogram);
					if(!overview) {
						readSRM(element.getValue(), chromatogram);
					}
				}
			}
			reader.close();
		} catch(JAXBException | FileNotFoundException | XMLStreamException e) {
			logger.error(e);
		}
	}

	private void readTIC(ChromatogramType chromatogramType, IVendorChromatogramMSD chromatogram) {

		if(chromatogramType.getCvParam().stream().anyMatch(n -> //
		n.getAccession().equals("MS:1000235") && n.getName().equals("total ion current chromatogram"))) {
			chromatogram.setDataName(chromatogramType.getId());
			Pair<double[], double[]> binaryData = readBinaryData(chromatogramType);
			XmlChromatogramReader.addTotalSignals(binaryData.getValue(), binaryData.getKey(), chromatogram);
		}
	}

	private void readSpectrum(SpectrumType spectrum, int cycleNumber, IVendorChromatogramMSD chromatogram, Map<String, ReferenceableParamGroupType> paramGroups) {

		IRegularMassSpectrum massSpectrum = readMassSpectrum(spectrum);
		if(massSpectrum.getMassSpectrometer() < 2) {
			cycleNumber++;
		}
		if(cycleNumber >= 1) {
			massSpectrum.setCycleNumber(cycleNumber);
		}
		setRetentionTime(spectrum, massSpectrum);
		massSpectrum.setIdentifier(spectrum.getId());
		setReferencedPolarity(spectrum, massSpectrum, paramGroups);
		readIons(spectrum, massSpectrum, chromatogram);
		chromatogram.addScan(massSpectrum);
	}

	public static void setRetentionTime(SpectrumType spectrum, IRegularMassSpectrum massSpectrum) {

		if(spectrum.getScanList() == null) {
			return;
		}
		for(ScanType scanType : spectrum.getScanList().getScan()) {
			for(CVParamType cvParam : scanType.getCvParam()) {
				if(cvParam.getAccession().equals("MS:1000016") && cvParam.getName().equals("scan start time")) {
					float multiplicator = XmlReader110.getTimeMultiplicator(cvParam);
					int retentionTime = Math.round(Float.parseFloat(cvParam.getValue()) * multiplicator);
					massSpectrum.setRetentionTime(retentionTime);
				}
			}
		}
	}

	public static void setTotalIonCurrent(SpectrumType spectrum, IRegularMassSpectrum massSpectrum) {

		for(ScanType scanType : spectrum.getScanList().getScan()) {
			for(CVParamType cvParam : scanType.getCvParam()) {
				if(cvParam.getAccession().equals("MS:1000285") && cvParam.getName().equals("total ion current")) {
					massSpectrum.adjustTotalSignal(Float.parseFloat(cvParam.getValue()));
				}
			}
		}
	}

	private void readSRM(ChromatogramType chromatogramType, IVendorChromatogramMSD chromatogram) {

		if(chromatogramType.getCvParam().stream().anyMatch(n -> //
		n.getAccession().equals("MS:1001473") && n.getName().equals("selected reaction monitoring chromatogram"))) {
			IVendorChromatogramMSD referencedChromatogram = new VendorChromatogramMSD();
			referencedChromatogram.setDataName(chromatogramType.getId());

			Pair<double[], double[]> binaryData = readBinaryData(chromatogramType);

			double precursorIon = getPrecursorIon(chromatogramType);
			double productIon = getProductIon(chromatogramType);
			double collisionEnergy = getCollisionEnergy(chromatogramType);
			IIonTransition ionTransition = new IonTransition(precursorIon, productIon, collisionEnergy, 1, 1, 0);
			chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);

			addIonSRM(binaryData.getValue(), binaryData.getKey(), ionTransition, referencedChromatogram);

			for(CVParamType cvParam : chromatogramType.getCvParam()) {
				for(IScan scan : referencedChromatogram.getScans()) {
					if(scan instanceof IRegularMassSpectrum massSpectrum) {
						setPolarity(cvParam, massSpectrum);
					}
				}
			}

			chromatogram.getReferencedChromatograms().add(referencedChromatogram);
		}
	}

	public static void addIonSRM(double[] intensities, double[] retentionTimes, IIonTransition ionTransition, IVendorChromatogramMSD chromatogram) {

		int tic = Math.min(retentionTimes.length, intensities.length);
		for(int i = 0; i < tic; i++) {
			IRegularMassSpectrum massSpectrum = new RegularMassSpectrum();
			int retentionTime = (int)(retentionTimes[i]);
			massSpectrum.setRetentionTime(retentionTime);
			float intensity = (float)intensities[i];
			IVendorIonMSn ion = new VendorIonMSn(ionTransition.getQ3Ion(), intensity, ionTransition);
			massSpectrum.addIon(ion, false);
			chromatogram.addScan(massSpectrum);
		}
	}

	private double getPrecursorIon(ChromatogramType chromatogram) {

		PrecursorType precursor = chromatogram.getPrecursor();
		if(precursor == null) {
			return 0;
		}
		for(CVParamType cvParam : precursor.getIsolationWindow().getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000827") && cvParam.getName().equals("isolation window target m/z")) {
				return Double.parseDouble(cvParam.getValue());
			}
		}
		return 0;
	}

	private double getCollisionEnergy(ChromatogramType chromatogram) {

		PrecursorType precursor = chromatogram.getPrecursor();
		if(precursor == null) {
			return 0;
		}
		for(CVParamType cvParam : precursor.getActivation().getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000045") && cvParam.getName().equals("collision energy")) {
				return Double.parseDouble(cvParam.getValue());
			}
		}
		return 0;
	}

	private double getProductIon(ChromatogramType chromatogram) {

		ProductType product = chromatogram.getProduct();
		if(product == null) {
			return 0;
		}
		for(CVParamType cvParam : product.getIsolationWindow().getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000827") && cvParam.getName().equals("isolation window target m/z")) {
				return Double.parseDouble(cvParam.getValue());
			}
		}
		return 0;
	}

	private void readEditHistory(MzMLType mzML, IVendorChromatogramMSD chromatogram) {

		DataProcessingListType dataProcessinglist = mzML.getDataProcessingList();
		if(dataProcessinglist == null) {
			return;
		}
		for(DataProcessingType dataProcessing : dataProcessinglist.getDataProcessing()) {
			for(ProcessingMethodType processingMethod : dataProcessing.getProcessingMethod()) {
				SoftwareType software = (SoftwareType)processingMethod.getSoftwareRef();
				for(CVParamType cvParam : processingMethod.getCvParam()) {
					String operation = cvParam.getName();
					String editor = software.getId();
					chromatogram.getEditHistory().add(new EditInformation(operation, editor));
				}
				for(UserParamType userParam : processingMethod.getUserParam()) {
					String operation = userParam.getName();
					String editor = software.getId();
					chromatogram.getEditHistory().add(new EditInformation(operation, editor));
				}
			}
		}
	}

	private static double getSelectedIon(SpectrumType spectrum) {

		if(spectrum.getPrecursorList() != null) {
			for(PrecursorType precursorType : spectrum.getPrecursorList().getPrecursor()) {
				SelectedIonListType selectedIonList = precursorType.getSelectedIonList();
				if(selectedIonList != null) {
					for(ParamGroupType paramGroupType : selectedIonList.getSelectedIon()) {
						for(CVParamType cvParam : paramGroupType.getCvParam()) {
							if(cvParam.getAccession().equals("MS:1000744") && cvParam.getName().equals("selected ion m/z")) {
								return Double.parseDouble(cvParam.getValue());
							}
						}
					}
				}
			}
		}
		return 0;
	}

	private static double getSelectedIonPeakIntensity(SpectrumType spectrum) {

		if(spectrum.getPrecursorList() != null) {
			for(PrecursorType precursorType : spectrum.getPrecursorList().getPrecursor()) {
				SelectedIonListType selectedIonList = precursorType.getSelectedIonList();
				if(selectedIonList != null) {
					for(ParamGroupType paramGroupType : selectedIonList.getSelectedIon()) {
						for(CVParamType cvParam : paramGroupType.getCvParam()) {
							if(cvParam.getAccession().equals("MS:1000042") && cvParam.getName().equals("peak intensity")) {
								return Double.parseDouble(cvParam.getValue());
							}
						}
					}
				}
			}
		}
		return 0;
	}

	private static double getCollisionEnergy(SpectrumType spectrum) {

		if(spectrum.getPrecursorList() != null) {
			for(PrecursorType precursorType : spectrum.getPrecursorList().getPrecursor()) {
				for(CVParamType cvParam : precursorType.getActivation().getCvParam()) {
					if(cvParam.getAccession().equals("MS:1000045") && cvParam.getName().equals("collision energy")) {
						return Double.parseDouble(cvParam.getValue());
					}
				}
			}
		}
		return 0;
	}

	public static void readIons(SpectrumType spectrum, IRegularMassSpectrum massSpectrum, IVendorChromatogramMSD chromatogram) {

		double[] intensities = null;
		double[] mzs = null;
		for(BinaryDataArrayType binaryDataArrayType : spectrum.getBinaryDataArrayList().getBinaryDataArray()) {
			try {
				Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
				if(binaryData.getKey().equals("m/z")) {
					mzs = binaryData.getValue();
				} else if(binaryData.getKey().equals("intensity")) {
					intensities = binaryData.getValue();
				}
			} catch(DataFormatException e) {
				logger.error(e);
			}
		}
		if(mzs == null || intensities == null) {
			logger.warn("Ignoring spectrum " + spectrum.getIndex() + " " + spectrum.getId());
			return;
		}
		double selectedIon = getSelectedIon(spectrum);
		massSpectrum.setPrecursorIon(selectedIon);
		massSpectrum.setPrecursorBasePeak(getSelectedIonPeakIntensity(spectrum));
		double collisionEnergy = getCollisionEnergy(spectrum);
		int ions = Math.min(mzs.length, intensities.length);
		for(int i = 0; i < ions; i++) {
			if(selectedIon != 0) {
				IIonTransition ionTransition = new IonTransition(selectedIon, mzs[i], collisionEnergy, 1, 1, 0);
				massSpectrum.addIon(new VendorIonMSn(mzs[i], (float)intensities[i], ionTransition), false);
				chromatogram.getIonTransitionSettings().getIonTransitions().add(ionTransition);
			} else {
				massSpectrum.addIon(new VendorIon(mzs[i], (float)intensities[i]), false);
			}
		}
	}

	private static void setReferencedPolarity(SpectrumType spectrum, IRegularMassSpectrum massSpectrum, Map<String, ReferenceableParamGroupType> paramGroups) {

		if(spectrum.getReferenceableParamGroupRef() == null) {
			return;
		}
		for(ReferenceableParamGroupRefType groupType : spectrum.getReferenceableParamGroupRef()) {
			ReferenceableParamGroupType group = paramGroups.get(groupType.getRef());
			if(group == null) {
				continue;
			}
			for(CVParamType cvParam : group.getCvParam()) {
				setPolarity(cvParam, massSpectrum);
			}
		}
	}

	public static IRegularMassSpectrum readMassSpectrum(SpectrumType spectrum) {

		IRegularMassSpectrum massSpectrum = new RegularMassSpectrum();
		for(CVParamType cvParam : spectrum.getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000127") && cvParam.getName().equals("centroid spectrum")) {
				massSpectrum.setMassSpectrumType(MassSpectrumType.CENTROID);
			} else if(cvParam.getAccession().equals("MS:1000128") && cvParam.getName().equals("profile spectrum")) {
				massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
			}
			if(cvParam.getAccession().equals("MS:1000129") && cvParam.getName().equals("negative scan")) {
				massSpectrum.setPolarity(Polarity.NEGATIVE);
			} else if(cvParam.getAccession().equals("MS:1000130") && cvParam.getName().equals("positive scan")) {
				massSpectrum.setPolarity(Polarity.POSITIVE);
			}
			if(cvParam.getAccession().equals("MS:1000511") && cvParam.getName().equals("ms level")) {
				short msLevel = Short.parseShort(cvParam.getValue());
				massSpectrum.setMassSpectrometer(msLevel);
			}
		}
		return massSpectrum;
	}

	private boolean isMultiStageMassSpectrum(MzMLType mzML) {

		for(CVParamType cvParam : mzML.getFileDescription().getFileContent().getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000580") && cvParam.getName().equals("MSn spectrum")) {
				return true;
			}
		}
		return false;
	}

	public static Pair<double[], double[]> readBinaryData(ChromatogramType chromatogramType) {

		double[] retentionTimes = null;
		double[] intensities = null;
		for(BinaryDataArrayType binaryDataArrayType : chromatogramType.getBinaryDataArrayList().getBinaryDataArray()) {
			try {
				Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
				if(binaryData.getKey().equals("time")) {
					retentionTimes = binaryData.getValue();
				} else if(binaryData.getKey().equals("intensity")) {
					intensities = binaryData.getValue();
				}
			} catch(DataFormatException e) {
				logger.warn(e);
			}
		}

		return new ImmutablePair<>(retentionTimes, intensities);
	}

	private static void setPolarity(CVParamType cvParam, IRegularMassSpectrum massSpectrum) {

		if(cvParam.getAccession().equals("MS:1000129") && cvParam.getName().equals("negative scan")) {
			massSpectrum.setPolarity(Polarity.NEGATIVE);
		} else if(cvParam.getAccession().equals("MS:1000130") && cvParam.getName().equals("positive scan")) {
			massSpectrum.setPolarity(Polarity.POSITIVE);
		}
	}
}
