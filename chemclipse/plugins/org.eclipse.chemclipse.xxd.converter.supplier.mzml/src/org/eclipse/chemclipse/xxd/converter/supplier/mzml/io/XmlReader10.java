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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v10.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v10.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v10.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlReader10 {

	public static final String VERSION = "1.0";

	private XmlReader10() {

	}

	public static MzMLType getMzML(File file) throws SAXException, IOException, JAXBException, ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		NodeList topNode = document.getElementsByTagName("mzML");
		//
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (MzMLType)unmarshaller.unmarshal(topNode.item(0));
	}

	public static int getTimeMultiplicator(CVParamType cvParam) {

		int multiplicator = 1;
		if(cvParam.getUnitAccession().equals("UO:0000028") && cvParam.getUnitName().equals("millisecond")) {
			multiplicator = 1;
		}
		if(cvParam.getUnitAccession().equals("UO:0000010") && cvParam.getUnitName().equals("second")) {
			multiplicator = 1000;
		}
		if(cvParam.getUnitAccession().equals("UO:0000031") && cvParam.getUnitName().equals("minute")) {
			multiplicator = 60 * 1000;
		}
		return multiplicator;
	}
}
