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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.eclipse.chemclipse.support.xml.XmlParserFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlHelper {

	private XmlHelper() {

	}

	public static <T> T unmarshalElementAtOffset(File file, long byteOffset, Class<T> type) throws JAXBException, XMLStreamException, IOException {

		JAXBContext ctx = JAXBContext.newInstance(type);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();

		XMLInputFactory xmlInputFactory = createInputFactory();
		String wrapperStart = buildWrapperStart(readRootNamespaceDeclarations(file, xmlInputFactory));

		try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
			channel.position(byteOffset);

			InputStream prefix = new ByteArrayInputStream(wrapperStart.getBytes(StandardCharsets.UTF_8));
			InputStream body = Channels.newInputStream(channel); // positioned at byteOffset

			try (InputStream in = new BufferedInputStream(new SequenceInputStream(prefix, body))) {
				XMLStreamReader raw = xmlInputFactory.createXMLStreamReader(in, "UTF-8");

				while(raw.getEventType() != XMLStreamConstants.START_ELEMENT) {
					if(!raw.hasNext()) {
						throw new XMLStreamException("No <frag> at offset " + byteOffset);
					}
					raw.next();
				}
				if(raw.nextTag() != XMLStreamConstants.START_ELEMENT) {
					throw new XMLStreamException("No element inside <frag> at offset " + byteOffset);
				}

				XMLStreamReader reader = new SingleElementReader(raw);
				return unmarshaller.unmarshal(reader, type).getValue();
			}
		}
	}

	/**
	 * Skips parsing the whole XML tree.
	 */
	public static <T> T parseFiltered(File file, Class<T> type, String advanceTo, String stopTag) throws IOException, JAXBException, XMLStreamException {

		try (FileInputStream inputStream = new FileInputStream(file)) {
			XMLInputFactory xmlInputFactory = createInputFactory();
			XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

			if(advanceTo != null) {
				while(reader.hasNext()) {
					int event = reader.next();
					if(event == XMLStreamConstants.START_ELEMENT && advanceTo.equals(reader.getLocalName())) {
						break;
					}
				}
			}

			XMLStreamReader filteredReader = new StreamReaderDelegate(reader) {

				@Override
				public int next() throws XMLStreamException {

					while(true) {
						int event = super.next();

						if(event == XMLStreamConstants.START_ELEMENT && stopTag.equals(getLocalName())) {

							int skipDepth = 1;

							while(skipDepth > 0 && super.hasNext()) {
								event = super.next();

								if(event == XMLStreamConstants.START_ELEMENT) {
									skipDepth++;
								} else if(event == XMLStreamConstants.END_ELEMENT) {
									skipDepth--;
								}
							}

							continue;
						}

						return event;
					}
				}
			};

			JAXBContext context = JAXBContext.newInstance(type);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			return unmarshaller.unmarshal(filteredReader, type).getValue();
		}
	}

	private static XMLInputFactory createInputFactory() {

		XMLInputFactory xmlInputFactory = XmlParserFactory.createInputFactory();
		xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
		return xmlInputFactory;
	}

	private static List<String[]> readRootNamespaceDeclarations(File file, XMLInputFactory xmlInputFactory) throws IOException, XMLStreamException {

		try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
			while(xmlStreamReader.getEventType() != XMLStreamConstants.START_ELEMENT) {
				xmlStreamReader.next();
			}
			int n = xmlStreamReader.getNamespaceCount();
			List<String[]> decls = new ArrayList<>(n);
			for(int i = 0; i < n; i++) {
				decls.add(new String[]{xmlStreamReader.getNamespacePrefix(i), xmlStreamReader.getNamespaceURI(i)}); // prefix may be null
			}
			xmlStreamReader.close();
			return decls;
		}
	}

	private static String buildWrapperStart(List<String[]> declarations) {

		StringBuilder stringBuilder = new StringBuilder("<frag");
		for(String[] declaration : declarations) {
			String prefix = declaration[0];
			String uri = declaration[1];
			if(prefix == null || prefix.isEmpty()) {
				stringBuilder.append(" xmlns=\"").append(uri).append('"');
			} else {
				stringBuilder.append(" xmlns:").append(prefix).append("=\"").append(uri).append('"');
			}
		}
		return stringBuilder.append('>').toString();
	}
}