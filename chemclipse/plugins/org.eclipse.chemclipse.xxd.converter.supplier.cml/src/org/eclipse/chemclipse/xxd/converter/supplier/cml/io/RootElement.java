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
package org.eclipse.chemclipse.xxd.converter.supplier.cml.io;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Cml;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Spectrum;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class RootElement {

	// both <cml><spectrum> and <spectrum> alone are valid
	public static Spectrum getSpectrum(File file) throws SAXException, IOException, JAXBException, ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		JAXBContext jaxbContext = JAXBContext.newInstance(Cml.class, Spectrum.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Object result = unmarshaller.unmarshal(document);
		if(result instanceof JAXBElement) {
			JAXBElement<?> element = (JAXBElement<?>)result;
			if(element.getDeclaredType().equals(Cml.class)) {
				Cml cml = (Cml)element.getValue();
				if(cml != null && cml.getSpectrum() != null) {
					return cml.getSpectrum();
				}
			} else if(element.getDeclaredType().equals(Spectrum.class)) {
				return (Spectrum)element.getValue();
			}
		} else if(result instanceof Cml cml) {
			if(cml.getSpectrum() != null) {
				return cml.getSpectrum();
			}
		} else if(result instanceof Spectrum spectrum) {
			return spectrum;
		}
		throw new IllegalStateException("Unable to unmarshal XML to a Spectrum object.");
	}
}
