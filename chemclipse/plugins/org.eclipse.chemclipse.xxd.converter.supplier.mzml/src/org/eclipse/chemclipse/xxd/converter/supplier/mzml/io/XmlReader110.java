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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.support.xml.XmlParserFactory;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ObjectFactory;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ParamGroupType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.PrecursorType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.ProductType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SelectedIonListType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlReader110 {

	public static final String VERSION = "1.1.0";

	private XmlReader110() {

	}

	public static MzMLType getMzML(File file) throws SAXException, IOException, JAXBException, ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = XmlParserFactory.createDocumentBuilderFactory();
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		NodeList topNode = document.getElementsByTagName("mzML");

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (MzMLType)unmarshaller.unmarshal(topNode.item(0));
	}

	public static float getTimeMultiplicator(CVParamType cvParam) {

		float multiplicator = 1f;
		if(cvParam.getUnitAccession().equals("UO:0000028") && cvParam.getUnitName().equals("millisecond")) {
			multiplicator = 1f;
		}
		if(cvParam.getUnitAccession().equals("UO:0000010") && cvParam.getUnitName().equals("second")) {
			multiplicator = (float)IChromatogramOverview.SECOND_CORRELATION_FACTOR;
		}
		if(cvParam.getUnitAccession().equals("UO:0000031") && cvParam.getUnitName().equals("minute")) {
			multiplicator = (float)IChromatogramOverview.MINUTE_CORRELATION_FACTOR;
		}
		return multiplicator;
	}

	public static double getCollisionEnergy(SpectrumType spectrum) {

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

	public static double getCollisionEnergy(PrecursorType precursor) {

		if(precursor == null) {
			return 0;
		}
		for(CVParamType cvParam : precursor.getActivation().getCvParam()) {
			if(cvParam.getAccession().equals("MS:1000045") && cvParam.getName().equals("collision energy")) {
				return Double.parseDouble(cvParam.getValue());
			}
			if(cvParam.getAccession().equals("MS:1000138") && cvParam.getName().equals("normalized collision energy")) {
				return Double.parseDouble(cvParam.getValue()); // TODO: percent of what
			}
		}
		return 0;
	}

	public static double getProductIon(ProductType product) {

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

	public static double getSelectedIon(SpectrumType spectrum) {

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
}
