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
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - adjust failed parsing behaviour
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.UnknownVersionException;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.wsd.converter.io.AbstractChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.io.ChromatogramReaderVersion110;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramReader extends AbstractChromatogramWSDReader {

	public static IChromatogramWSDReader getReader(final File file) throws IOException {

		IChromatogramWSDReader chromatogramReader = null;
		try (final FileReader fileReader = new FileReader(file)) {
			final char[] charBuffer = new char[500];
			fileReader.read(charBuffer);
			final String header = new String(charBuffer);
			if(header.contains(XmlReader110.VERSION)) {
				chromatogramReader = new ChromatogramReaderVersion110();
			} else {
				throw new UnknownVersionException();
			}
		}
		return chromatogramReader;
	}

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramWSDReader reader = getReader(file);
		return reader.read(file, monitor);
	}

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramWSDReader reader = getReader(file);
		return reader.read(file, monitor);
	}
}
