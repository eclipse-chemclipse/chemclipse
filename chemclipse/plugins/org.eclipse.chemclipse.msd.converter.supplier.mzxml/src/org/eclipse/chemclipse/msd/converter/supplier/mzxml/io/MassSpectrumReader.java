/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adapted for MALDI
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.UnknownVersionException;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion20;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion21;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion22;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion30;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion31;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io.MassSpectrumReaderVersion32;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.core.runtime.IProgressMonitor;

public class MassSpectrumReader extends AbstractMassSpectraReader {

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		final IMassSpectraReader massSpectraReader = getReader(file);
		return massSpectraReader.read(file, monitor);
	}

	public static IMassSpectraReader getReader(final File file) throws IOException {

		IMassSpectraReader massSpectraReader = null;
		final char[] charBuffer = new char[350];
		try (final FileReader fileReader = new FileReader(file)) {
			fileReader.read(charBuffer);
		}
		final String header = new String(charBuffer);
		if(header.contains(MassSpectrumReaderVersion20.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion20();
		} else if(header.contains(MassSpectrumReaderVersion21.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion21();
		} else if(header.contains(MassSpectrumReaderVersion22.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion22();
		} else if(header.contains(MassSpectrumReaderVersion30.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion30();
		} else if(header.contains(MassSpectrumReaderVersion31.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion31();
		} else if(header.contains(MassSpectrumReaderVersion32.VERSION)) {
			massSpectraReader = new MassSpectrumReaderVersion32();
		} else {
			throw new UnknownVersionException();
		}
		return massSpectraReader;
	}
}
