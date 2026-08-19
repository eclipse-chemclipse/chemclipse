/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - auto detection for chromatography files
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.converter;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;

public class ChromatogramFileContentMatcher extends AbstractFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean hasRootElement = false;
		boolean hasRetentionTime = false;

		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(file));

			int events = 0;
			while(xmlStreamReader.hasNext() && events < 1000) {
				int event = xmlStreamReader.next();
				if(event == XMLStreamConstants.START_ELEMENT) {
					String localName = xmlStreamReader.getLocalName();
					if("mzXML".equals(localName)) {
						hasRootElement = true;
					} else if("scan".equals(localName)) {
						String retentionTime = xmlStreamReader.getAttributeValue(null, "retentionTime");
						if(retentionTime != null && !retentionTime.isEmpty()) {
							hasRetentionTime = true;
						}
					}
				}
				events++;
			}
		} catch(Exception e) {
			// fail silently
		}

		return hasRootElement && hasRetentionTime;
	}
}
