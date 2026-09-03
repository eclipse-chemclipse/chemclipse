/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;

/**
 * Creates XML parser factories that are hardened against external entity
 * resolution but still able to read the large data files this application deals
 * with.
 */
public class XmlParserFactory {

	private static final String FEATURE_DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String FEATURE_LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	/*
	 * JDK 24 lowered both limits to 100.000. Raising only the general
	 * entity limit moves the failure to the total entity limit.
	 */
	private static final String LIMIT_GENERAL_ENTITY_SIZE = "jdk.xml.maxGeneralEntitySizeLimit";
	private static final String LIMIT_TOTAL_ENTITY_SIZE = "jdk.xml.totalEntitySizeLimit";
	private static final String NO_LIMIT = "0";

	private XmlParserFactory() {

	}

	public static DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		documentBuilderFactory.setFeature(FEATURE_DISALLOW_DOCTYPE, true);
		documentBuilderFactory.setFeature(FEATURE_LOAD_EXTERNAL_DTD, false);
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		documentBuilderFactory.setAttribute(LIMIT_GENERAL_ENTITY_SIZE, NO_LIMIT);
		documentBuilderFactory.setAttribute(LIMIT_TOTAL_ENTITY_SIZE, NO_LIMIT);
		return documentBuilderFactory;
	}

	public static XMLInputFactory createInputFactory() {

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
		xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		xmlInputFactory.setProperty(LIMIT_GENERAL_ENTITY_SIZE, NO_LIMIT);
		xmlInputFactory.setProperty(LIMIT_TOTAL_ENTITY_SIZE, NO_LIMIT);
		return xmlInputFactory;
	}
}
