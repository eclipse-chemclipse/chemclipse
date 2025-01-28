/*******************************************************************************
 * Copyright (c) 2015, 2024 Michael Chang.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Michael Chang - initial API and implementation
 * Philip Wenig - improvements
 * Christoph Läubrich - add new version
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.ocx.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.wsd.converter.io.AbstractChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1005;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1006;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1007;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1100;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1300;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1301;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1400;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1500;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1501;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramReader_1502;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.ReaderHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramReaderWSD extends AbstractChromatogramWSDReader implements IChromatogramWSDZipReader {

	@Override
	public IChromatogramOverview readOverview(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramOverview chromatogramOverview = null;
		ReaderHelper readerHelper = new ReaderHelper();
		String version = readerHelper.getVersion(file);
		/*
		 * It's used to support older versions of
		 * the *.ocb format.
		 * TODO Optimize
		 */
		IChromatogramWSDReader chromatogramReader = getChromatogramReader(version);
		if(chromatogramReader != null) {
			try {
				chromatogramOverview = chromatogramReader.readOverview(file, monitor);
			} catch(Exception e) {
				//
			}
		}
		return chromatogramOverview;
	}

	@Override
	public IChromatogramWSD read(File file, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		IChromatogramWSD chromatogramWSD = null;
		ReaderHelper readerHelper = new ReaderHelper();
		String version = readerHelper.getVersion(file);
		/*
		 * It's used to support older versions of
		 * the *.ocb format.
		 */
		IChromatogramWSDReader chromatogramReader = getChromatogramReader(version);
		if(chromatogramReader != null) {
			try {
				chromatogramWSD = chromatogramReader.read(file, monitor);
			} catch(Exception e) {
				//
			}
		}
		return chromatogramWSD;
	}

	@Override
	public IChromatogramWSD read(ZipInputStream zipInputStream, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readChromatogram(zipInputStream, directoryPrefix, monitor);
	}

	@Override
	public IChromatogramWSD read(ZipFile zipFile, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readChromatogram(zipFile, directoryPrefix, monitor);
	}

	private IChromatogramWSD readChromatogram(Object object, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		IChromatogramWSDZipReader chromatogramReader = null;
		IChromatogramWSD chromatogramWSD = null;
		ReaderHelper readerHelper = new ReaderHelper();
		//
		String version = readerHelper.getVersion(object, directoryPrefix);
		chromatogramReader = getChromatogramReader(version);
		//
		if(chromatogramReader != null) {
			if(object instanceof ZipInputStream zipInputStream) {
				chromatogramWSD = chromatogramReader.read(zipInputStream, directoryPrefix, monitor);
			} else if(object instanceof ZipFile zipFile) {
				chromatogramWSD = chromatogramReader.read(zipFile, directoryPrefix, monitor);
			}
		}
		//
		return chromatogramWSD;
	}

	private IChromatogramWSDZipReader getChromatogramReader(String version) {

		IChromatogramWSDZipReader chromatogramReader = null;
		//
		if(version.equals(Format.CHROMATOGRAM_VERSION_1005)) {
			chromatogramReader = new ChromatogramReader_1005();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1006)) {
			chromatogramReader = new ChromatogramReader_1006();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1007)) {
			chromatogramReader = new ChromatogramReader_1007();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1100)) {
			chromatogramReader = new ChromatogramReader_1100();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1300)) {
			chromatogramReader = new ChromatogramReader_1300();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1301)) {
			chromatogramReader = new ChromatogramReader_1301();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1400)) {
			chromatogramReader = new ChromatogramReader_1400();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1500)) {
			chromatogramReader = new ChromatogramReader_1500();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1501)) {
			chromatogramReader = new ChromatogramReader_1501();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1502)) {
			chromatogramReader = new ChromatogramReader_1502();
		}
		//
		return chromatogramReader;
	}
}