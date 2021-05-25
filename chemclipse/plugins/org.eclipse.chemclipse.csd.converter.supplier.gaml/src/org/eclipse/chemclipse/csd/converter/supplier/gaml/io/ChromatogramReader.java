/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.gaml.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.csd.converter.io.AbstractChromatogramCSDReader;
import org.eclipse.chemclipse.csd.converter.io.IChromatogramCSDReader;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.xxd.converter.supplier.gaml.internal.io.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.io.exception.UnknownVersionException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramReader extends AbstractChromatogramCSDReader implements IChromatogramCSDReader {

	public static IChromatogramCSDReader getReader(final File file) throws IOException {

		IChromatogramCSDReader chromatogramReader = null;
		//
		final FileReader fileReader = new FileReader(file);
		final char[] charBuffer = new char[100];
		fileReader.read(charBuffer);
		fileReader.close();
		//
		final String header = new String(charBuffer);
		if(header.contains(IConstants.GAML_V_100)) {
			chromatogramReader = new ChromatogramReaderVersion100(IConstants.CONTEXT_PATH_V_100);
		} else if(header.contains(IConstants.GAML_V_110)) {
			chromatogramReader = new ChromatogramReaderVersion110(IConstants.CONTEXT_PATH_V_110);
		} else if(header.contains(IConstants.GAML_V_120)) {
			chromatogramReader = new ChromatogramReaderVersion120(IConstants.CONTEXT_PATH_V_120);
		} else {
			throw new UnknownVersionException();
		}
		//
		return chromatogramReader;
	}

	@Override
	public IChromatogramCSD read(final File file, final IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		final IChromatogramCSDReader chromatogramReader = getReader(file);
		if(chromatogramReader != null) {
			return chromatogramReader.read(file, monitor);
		} else {
			return null;
		}
	}

	@Override
	public IChromatogramOverview readOverview(final File file, final IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		final IChromatogramCSDReader chromatogramReader = getReader(file);
		if(chromatogramReader != null) {
			return chromatogramReader.readOverview(file, monitor);
		} else {
			return null;
		}
	}
}
