/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mmass.internal.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.IVendorMassSpectra;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.VendorMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MassSpectrumReaderVersion22 extends AbstractMassSpectraReader implements IMassSpectraReader {

	private static final Logger logger = Logger.getLogger(MassSpectrumReaderVersion22.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		IStandaloneMassSpectrum massSpectrum = null;
		try {
			massSpectrum = new StandaloneMassSpectrum();
			massSpectrum.setFile(file);
			massSpectrum.setIdentifier(file.getName());
			massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
			//
			NodeList nodeList = getTopNode(file);
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)node;
					readDescription(element, massSpectrum);
					readSpectrum(element, massSpectrum, monitor);
					readPeakList(element, massSpectrum);
					readAnnotations(element, massSpectrum);
					// TODO: sequences
				}
			}
		} catch(SAXException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		} catch(DOMException e) {
			logger.warn(e);
		} catch(DataFormatException e) {
			logger.warn(e);
		}
		//
		IVendorMassSpectra massSpectra = new VendorMassSpectra();
		massSpectra.setName(file.getName());
		massSpectra.addMassSpectrum(massSpectrum);
		return massSpectra;
	}

	private NodeList getTopNode(File file) throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		// document.getDocumentElement().normalize();
		return document.getElementsByTagName("mSD");
	}

	private void readDescription(Element element, IStandaloneMassSpectrum massSpectrum) {

		NodeList descriptionList = element.getElementsByTagName("description");
		for(int i = 0; i < descriptionList.getLength(); i++) {
			Node node = descriptionList.item(i);
			Element description = (Element)node;
			massSpectrum.setSampleName(description.getElementsByTagName("title").item(0).getTextContent());
			try {
				String date = description.getElementsByTagName("date").item(0).getAttributes().getNamedItem("value").getTextContent();
				if(!date.isEmpty()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
					massSpectrum.setDate(dateFormat.parse(date));
				}
			} catch(ParseException e) {
				logger.warn(e);
			}
			String operator = description.getElementsByTagName("operator").item(0).getAttributes().getNamedItem("value").getTextContent();
			String contact = description.getElementsByTagName("contact").item(0).getAttributes().getNamedItem("value").getTextContent();
			String institution = description.getElementsByTagName("institution").item(0).getAttributes().getNamedItem("value").getTextContent();
			massSpectrum.setOperator(operator + " " + contact + " " + institution);
			massSpectrum.setInstrument(description.getElementsByTagName("instrument").item(0).getAttributes().getNamedItem("value").getTextContent());
			massSpectrum.setDescription(description.getElementsByTagName("notes").item(0).getTextContent());
		}
	}

	private void readSpectrum(Element element, IStandaloneMassSpectrum massSpectrum, IProgressMonitor monitor) throws DOMException, DataFormatException {

		int points = 0;
		float[] mzs = null;
		float[] intensities = null;
		NodeList spectrumList = element.getElementsByTagName("spectrum");
		for(int i = 0; i < spectrumList.getLength(); i++) {
			Node node = spectrumList.item(i);
			Element spectrum = (Element)node;
			points = Integer.parseInt(spectrum.getAttribute("points"));
			monitor.beginTask(ConverterMessages.importScan, points);
			Node mzArray = spectrum.getElementsByTagName("mzArray").item(0);
			checkArray(mzArray.getAttributes());
			mzs = decodeFloatArray(decompress(Base64.decodeBase64(mzArray.getTextContent())));
			if(mzs.length != points) {
				throw new IllegalArgumentException("Spectrum points does not match uncompressed mz array.");
			}
			Node intArray = spectrum.getElementsByTagName("intArray").item(0);
			checkArray(intArray.getAttributes());
			intensities = decodeFloatArray(decompress(Base64.decodeBase64(intArray.getTextContent())));
			if(intensities.length != points) {
				throw new IllegalArgumentException("Spectrum points does not match uncompressed intensities array.");
			}
		}
		for(int i = 0; i < points; i++) {
			IVendorIon ion = new VendorIon(mzs[i], intensities[i]);
			massSpectrum.addIon(ion, false);
			monitor.worked(1);
		}
	}

	private void readPeakList(Element element, IStandaloneMassSpectrum massSpectrum) throws DOMException {

		NodeList peakList = element.getElementsByTagName("peaklist");
		for(int i = 0; i < peakList.getLength(); i++) {
			Node node = peakList.item(i);
			Element peakListElement = (Element)node;
			NodeList peakNodeList = peakListElement.getElementsByTagName("peak");
			for(int n = 0; n < peakNodeList.getLength(); n++) {
				Node peak = peakNodeList.item(n);
				Element peakElement = (Element)peak;
				MassSpectrumPeak massSpectrumPeak = new MassSpectrumPeak();
				String mz = peakElement.getAttribute("mz");
				massSpectrumPeak.setIon(Double.parseDouble(mz));
				String intensity = peakElement.getAttribute("intensity");
				massSpectrumPeak.setAbundance(Double.parseDouble(intensity));
				String sn = peakElement.getAttribute("sn");
				massSpectrumPeak.setSignalToNoise(Double.parseDouble(sn));
				massSpectrum.getPeaks().add(massSpectrumPeak);
			}
		}
	}

	private void readAnnotations(Element element, IStandaloneMassSpectrum massSpectrum) throws DOMException {

		NodeList annotationsList = element.getElementsByTagName("annotations");
		for(int i = 0; i < annotationsList.getLength(); i++) {
			Node node = annotationsList.item(i);
			Element annotationsElement = (Element)node;
			NodeList annotationList = annotationsElement.getElementsByTagName("annotation");
			for(int n = 0; n < annotationList.getLength(); n++) {
				Node annotationNode = annotationList.item(n);
				Element annotationElement = (Element)annotationNode;
				String peakMZ = annotationElement.getAttribute("peakMZ");
				Optional<IMassSpectrumPeak> nearestPeak = massSpectrum.getPeaks().stream().filter(p -> p.getIon() == Double.parseDouble(peakMZ)).findFirst();
				if(nearestPeak.isPresent()) {
					ILibraryInformation libraryInformation = new LibraryInformation();
					String calcMZ = annotationElement.getAttribute("calcMZ");
					if(!calcMZ.isEmpty()) {
						libraryInformation.setMolWeight(Double.parseDouble(calcMZ));
					}
					libraryInformation.setName(annotationElement.getTextContent());
					ComparisonResult comparisionResult = new ComparisonResult(1f, 1f, 1f, 1f);
					IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisionResult);
					identificationTarget.setIdentifier("mMass annotation");
					nearestPeak.get().getTargets().add(identificationTarget);
				}
			}
		}
	}

	private void checkArray(NamedNodeMap attributes) {

		if(!attributes.getNamedItem("compression").getTextContent().equals("zlib")) {
			throw new UnsupportedOperationException("Only zlib compression is supported.");
		}
		if(!attributes.getNamedItem("endian").getTextContent().equals("little")) {
			throw new UnsupportedOperationException("Only Little Endian is supported.");
		}
		if(!attributes.getNamedItem("precision").getTextContent().equals("32")) {
			throw new UnsupportedOperationException("Only 32-bit precision is supported.");
		}
	}

	private ByteBuffer decompress(byte[] binary) throws DataFormatException {

		ByteBuffer byteBuffer = ByteBuffer.wrap(binary);
		Inflater inflater = new Inflater();
		inflater.setInput(byteBuffer.array());
		byte[] byteArray = new byte[byteBuffer.capacity() * 10];
		int decodedLength = inflater.inflate(byteArray);
		byteBuffer = ByteBuffer.wrap(byteArray, 0, decodedLength);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		return byteBuffer;
	}

	private float[] decodeFloatArray(ByteBuffer byteBuffer) {

		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		float[] values = new float[floatBuffer.capacity()];
		for(int index = 0; index < floatBuffer.capacity(); index++) {
			values[index] = floatBuffer.get(index);
		}
		return values;
	}
}
