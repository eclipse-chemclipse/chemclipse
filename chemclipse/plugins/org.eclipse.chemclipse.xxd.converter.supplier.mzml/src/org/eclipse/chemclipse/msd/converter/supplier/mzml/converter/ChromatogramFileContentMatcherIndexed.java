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
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;
import org.eclipse.chemclipse.support.xml.XmlParserFactory;

public class ChromatogramFileContentMatcherIndexed extends AbstractFileContentMatcher {

	private static final String NS = "http://psi.hupo.org/ms/mzml";
	private static final String OFFSET_OPEN = "<indexListOffset>";
	private static final String OFFSET_CLOSE = "</indexListOffset>";
	private static final String INDEX_LIST_OPEN = "<indexList";
	private static final String CHROMATOGRAM = "chromatogram";
	private static final int TAIL_BYTES = 4096;
	private static final int SCAN_CHUNK = 1 << 20; // 1 MiB fallback window
	private static final long SCAN_LIMIT = 64L << 20;

	@Override
	public boolean checkFileFormat(File file) {

		return hasChromatogramOffset(file.toPath());
	}

	private static boolean hasChromatogramOffset(Path path) {

		try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
			long start = locateIndexList(channel);
			if(start < 0) {
				return false;
			}
			channel.position(start);
			/*
			 * The tail is not a document on its own. Prepending a synthetic start tag named
			 * indexedmzML makes the file's own closing tag balance it and restores the default
			 * namespace that would otherwise be lost by seeking past the real root.
			 */
			InputStream prolog = new ByteArrayInputStream(("<indexedmzML xmlns=\"" + NS + "\">").getBytes(StandardCharsets.UTF_8));
			try (InputStream tail = new BufferedInputStream(Channels.newInputStream(channel), 1 << 16)) {
				return scanForChromatogramOffset(new SequenceInputStream(prolog, tail));
			}
		} catch(IOException | XMLStreamException e) {
			return false; // fail silently
		}
	}

	private static boolean scanForChromatogramOffset(InputStream input) throws XMLStreamException {

		XMLInputFactory factory = XmlParserFactory.createInputFactory();
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		XMLStreamReader reader = factory.createXMLStreamReader(input, StandardCharsets.UTF_8.name());
		try {
			while(reader.hasNext()) {
				int event = reader.next();
				if(event == XMLStreamConstants.START_ELEMENT) {
					if("index".equals(reader.getLocalName())) {
						if(CHROMATOGRAM.equals(reader.getAttributeValue(null, "name"))) {
							return containsOffset(reader); // hit or miss, no other chromatogram index follows
						}
						skipElement(reader); // don't walk the spectrum offsets
					}
				} else if(event == XMLStreamConstants.END_ELEMENT && "indexList".equals(reader.getLocalName())) {
					break;
				}
			}
		} finally {
			reader.close();
		}
		return false;
	}

	/*
	 * Positioned on <index name="chromatogram">. Returns on the first non-empty <offset>.
	 */
	private static boolean containsOffset(XMLStreamReader reader) throws XMLStreamException {

		int depth = 1;
		while(reader.hasNext() && depth > 0) {
			int event = reader.next();
			if(event == XMLStreamConstants.START_ELEMENT) {
				if("offset".equals(reader.getLocalName())) {
					if(!reader.getElementText().isBlank()) { // consumes the matching END_ELEMENT
						return true;
					}
				} else {
					depth++;
				}
			} else if(event == XMLStreamConstants.END_ELEMENT) {
				depth--;
			}
		}
		return false;
	}

	private static void skipElement(XMLStreamReader reader) throws XMLStreamException {

		int depth = 1;
		while(reader.hasNext() && depth > 0) {
			int event = reader.next();
			if(event == XMLStreamConstants.START_ELEMENT) {
				depth++;
			} else if(event == XMLStreamConstants.END_ELEMENT) {
				depth--;
			}
		}
	}

	private static long locateIndexList(FileChannel channel) throws IOException {

		long size = channel.size();
		long declared = readDeclaredOffset(channel, size);
		if(declared >= 0 && declared < size && readAscii(channel, declared, 32).stripLeading().startsWith(INDEX_LIST_OPEN)) {
			return declared;
		}
		return scanBackwardsForIndexList(channel, size); // stale or missing indexListOffset
	}

	private static long readDeclaredOffset(FileChannel channel, long size) throws IOException {

		int length = (int)Math.min(TAIL_BYTES, size);
		String tail = readAscii(channel, size - length, length);
		int open = tail.lastIndexOf(OFFSET_OPEN);
		int close = open < 0 ? -1 : tail.indexOf(OFFSET_CLOSE, open);
		if(close < 0) {
			return -1;
		}
		try {
			return Long.parseLong(tail.substring(open + OFFSET_OPEN.length(), close).trim());
		} catch(NumberFormatException e) {
			return -1;
		}
	}

	private static long scanBackwardsForIndexList(FileChannel channel, long size) throws IOException {

		int overlap = INDEX_LIST_OPEN.length();
		long floor = Math.max(0, size - SCAN_LIMIT);
		long position = Math.max(floor, size - SCAN_CHUNK);
		while(true) {
			int length = (int)Math.min(SCAN_CHUNK + overlap, size - position);
			int hit = readAscii(channel, position, length).lastIndexOf(INDEX_LIST_OPEN);
			if(hit >= 0) {
				return position + hit;
			}
			if(position == floor) {
				return -1;
			}
			position = Math.max(floor, position - SCAN_CHUNK);
		}
	}

	/*
	 * ISO-8859-1 keeps one byte per char, so string indices are byte offsets.
	 * Safe because every marker searched for is ASCII.
	 */
	private static String readAscii(FileChannel channel, long position, int length) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(length);
		int read = 0;
		while(buffer.hasRemaining()) {
			int n = channel.read(buffer, position + read);
			if(n < 0) {
				break;
			}
			read += n;
		}
		return new String(buffer.array(), 0, read, StandardCharsets.ISO_8859_1);
	}
}
