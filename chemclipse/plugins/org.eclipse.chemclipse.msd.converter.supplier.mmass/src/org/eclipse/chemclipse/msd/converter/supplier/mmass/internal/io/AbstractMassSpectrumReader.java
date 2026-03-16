/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mmass.internal.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.IVendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mmass.converter.model.VendorIon;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractMassSpectrumReader extends AbstractMassSpectraReader {

	private static final Logger logger = Logger.getLogger(AbstractMassSpectrumReader.class);

	public NodeList getTopNode(File file) throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		// document.getDocumentElement().normalize();
		return document.getElementsByTagName("mSD");
	}

	public void readDescription(Element element, IStandaloneMassSpectrum massSpectrum) {

		NodeList descriptionList = element.getElementsByTagName("description");
		for(int i = 0; i < descriptionList.getLength(); i++) {
			Node node = descriptionList.item(i);
			Element description = (Element)node;
			massSpectrum.setSampleName(description.getElementsByTagName("title").item(0).getTextContent());
			try {
				String date = getAttributeValue(description, "date");
				if(!date.isEmpty()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
					massSpectrum.setDate(dateFormat.parse(date));
				}
			} catch(ParseException e) {
				logger.warn(e);
			}
			String operator = getAttributeValue(description, "operator");
			String contact = getAttributeValue(description, "contact");
			String institution = getAttributeValue(description, "institution");
			massSpectrum.setOperator(operator + " " + contact + " " + institution);
			massSpectrum.setInstrument(getAttributeValue(description, "instrument"));
			massSpectrum.setDescription(getTagTextContent(description, "notes"));
		}
	}

	public String getAttributeValue(Element parent, String tag) {

		NodeList nodes = parent.getElementsByTagName(tag);
		if(nodes != null && nodes.getLength() > 0) {
			Node node = nodes.item(0);
			if(node != null && node.hasAttributes()) {
				Node value = node.getAttributes().getNamedItem("value");
				if(value != null) {
					return value.getTextContent();
				}
			}
		}
		return null;
	}

	public String getTagTextContent(Element parent, String tag) {

		NodeList nodes = parent.getElementsByTagName(tag);
		if(nodes != null && nodes.getLength() > 0) {
			Node node = nodes.item(0);
			if(node != null) {
				return node.getTextContent();
			}
		}
		return null;
	}

	public void readSpectrum(Element element, IStandaloneMassSpectrum massSpectrum, IProgressMonitor monitor) throws DOMException, DataFormatException {

		int points = 0;
		double[] mzs = null;
		double[] intensities = null;

		NodeList spectrumList = element.getElementsByTagName("spectrum");
		int spectrumLength = spectrumList.getLength();
		if(spectrumLength > 0) {
			massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		}
		for(int i = 0; i < spectrumLength; i++) {
			Node node = spectrumList.item(i);
			Element spectrum = (Element)node;

			mzs = getArray(spectrum.getElementsByTagName("mzArray").item(0));
			intensities = getArray(spectrum.getElementsByTagName("intArray").item(0));

			if(spectrum.hasAttribute("points")) {
				points = Integer.parseInt(spectrum.getAttribute("points"));
			} else {
				points = Math.min(mzs.length, intensities.length);
			}
		}

		monitor.beginTask(ConverterMessages.importScan, points);
		for(int i = 0; i < points; i++) {
			IVendorIon ion = new VendorIon(mzs[i], (float)intensities[i]);
			massSpectrum.addIon(ion, false);
			monitor.worked(1);
		}
	}

	public ByteOrder getByteOrder(NamedNodeMap attributes) {

		if(attributes.getNamedItem("endian").getTextContent().equals("little")) {
			return ByteOrder.LITTLE_ENDIAN;
		} else if(attributes.getNamedItem("endian").getTextContent().equals("big")) {
			return ByteOrder.BIG_ENDIAN;
		} else {
			return ByteOrder.nativeOrder();
		}
	}

	public double[] getArray(Node array) throws DOMException, DataFormatException {

		NamedNodeMap attributes = array.getAttributes();
		String precision = attributes.getNamedItem("precision").getTextContent();
		if(precision.equals("32")) {
			return decodeFloatArray(decompress(Base64.getDecoder().decode(array.getTextContent()), attributes));
		} else if(precision.equals("64")) {
			return decodeDoubleArray(decompress(Base64.getDecoder().decode(array.getTextContent()), attributes));
		} else {
			throw new UnsupportedOperationException("Only precision 32 and 64 are supported.");
		}
	}

	public ByteBuffer decompress(byte[] binary, NamedNodeMap attributes) throws DataFormatException {

		if(!attributes.getNamedItem("compression").getTextContent().equals("zlib")) {
			throw new UnsupportedOperationException("Only zlib compression is supported.");
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(binary);
		Inflater inflater = new Inflater();
		inflater.setInput(byteBuffer.array());
		byte[] byteArray = new byte[byteBuffer.capacity() * 10];
		int decodedLength = inflater.inflate(byteArray);
		byteBuffer = ByteBuffer.wrap(byteArray, 0, decodedLength);
		byteBuffer.order(getByteOrder(attributes));
		return byteBuffer;
	}

	public double[] decodeFloatArray(ByteBuffer byteBuffer) {

		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		double[] values = new double[floatBuffer.capacity()];
		for(int index = 0; index < floatBuffer.capacity(); index++) {
			values[index] = floatBuffer.get(index);
		}
		return values;
	}

	public double[] decodeDoubleArray(ByteBuffer byteBuffer) {

		DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
		double[] values = new double[doubleBuffer.capacity()];
		for(int index = 0; index < doubleBuffer.capacity(); index++) {
			values[index] = doubleBuffer.get(index);
		}
		return values;
	}

	public void readPeakList(Element element, IStandaloneMassSpectrum massSpectrum) throws DOMException {

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

	public void readAnnotations(Element element, IStandaloneMassSpectrum massSpectrum) throws DOMException {

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
}
