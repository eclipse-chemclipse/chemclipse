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
 * Christoph Läubrich - add generics
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramImportConverter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.io.ChromatogramMSDReaderVersion10;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.io.ChromatogramMSDReaderVersion110;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.wsd.converter.io.IChromatogramWSDReader;
import org.eclipse.chemclipse.wsd.converter.supplier.mzml.converter.io.ChromatogramWSDReaderVersion110;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader10;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramImportConverter extends AbstractChromatogramImportConverter<IChromatogram> {

	private static final Logger logger = Logger.getLogger(ChromatogramImportConverter.class);
	private static final String DESCRIPTION = "mzML Import Converter";
	private static final String IMPORT_CHROMATOGRAM = "Import mzML Chromatogram";
	private static final String IMPORT_CHROMATOGRAM_OVERVIEW = "Import mzML Chromatogram Overview";
	private static final String ERROR = "Error reading file: ";

	@Override
	public IProcessingInfo<IChromatogram> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogram> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			String version = getVersion(file, processingInfo);
			if(XmlReader110.VERSION.equals(version)) {
				IChromatogramMSDReader chromatogramReaderMSD = new ChromatogramMSDReaderVersion110();
				importChromatogramMSD(chromatogramReaderMSD, file, monitor, processingInfo);
				IChromatogramWSDReader chromatogramReaderWSD = new ChromatogramWSDReaderVersion110();
				importChromatogramWSD(chromatogramReaderWSD, file, monitor, processingInfo);
			} else if(XmlReader10.VERSION.equals(version)) {
				IChromatogramMSDReader chromatogramReaderMSD = new ChromatogramMSDReaderVersion10();
				importChromatogramMSD(chromatogramReaderMSD, file, monitor, processingInfo);
			}
		}
		return processingInfo;
	}

	private String getVersion(File file, IProcessingInfo<?> processingInfo) {

		try (final FileReader fileReader = new FileReader(file)) {
			final char[] charBuffer = new char[500];
			fileReader.read(charBuffer);
			final String header = new String(charBuffer);
			if(header.contains(XmlReader110.VERSION)) {
				return XmlReader110.VERSION;
			} else if(header.contains(XmlReader10.VERSION)) {
				return XmlReader10.VERSION;
			} else {
				processingInfo.addErrorMessage(DESCRIPTION, "Unknown version");
			}
		} catch(FileNotFoundException e) {
			logger.error(e);
		} catch(IOException e) {
			logger.error(e);
		}
		return null;
	}

	private void importChromatogramMSD(IChromatogramMSDReader chromatogramReaderMSD, File file, IProgressMonitor monitor, IProcessingInfo<IChromatogram> processingInfo) {

		monitor.subTask(IMPORT_CHROMATOGRAM);
		try {
			IChromatogramMSD chromatogram = chromatogramReaderMSD.read(file, monitor);
			if(chromatogram.getNumberOfScans() == 0) {
				return;
			}
			if(processingInfo.getProcessingResult() == null) {
				processingInfo.setProcessingResult(chromatogram);
			} else {
				processingInfo.getProcessingResult().addReferencedChromatogram(chromatogram);
			}
		} catch(IOException e) {
			logger.warn(e);
			processingInfo.addErrorMessage(DESCRIPTION, ERROR + file.getAbsolutePath());
		} catch(InterruptedException e) {
			logger.warn(e);
			processingInfo.addErrorMessage(DESCRIPTION, ERROR + file.getAbsolutePath());
			Thread.currentThread().interrupt();
		}
	}

	private void importChromatogramWSD(IChromatogramWSDReader chromatogramReaderWSD, File file, IProgressMonitor monitor, IProcessingInfo<IChromatogram> processingInfo) {

		monitor.subTask(IMPORT_CHROMATOGRAM);
		try {
			IChromatogramWSD chromatogram = chromatogramReaderWSD.read(file, monitor);
			if(chromatogram.getNumberOfScans() == 0) {
				return;
			}
			if(processingInfo.getProcessingResult() == null) {
				processingInfo.setProcessingResult(chromatogram);
			} else {
				processingInfo.getProcessingResult().addReferencedChromatogram(chromatogram);
			}
		} catch(IOException e) {
			logger.warn(e);
			processingInfo.addErrorMessage(DESCRIPTION, ERROR + file.getAbsolutePath());
		}
	}

	@Override
	public IProcessingInfo<IChromatogramOverview> convertOverview(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramOverview> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			String version = getVersion(file, processingInfo);
			if(XmlReader110.VERSION.equals(version)) {
				IChromatogramMSDReader chromatogramReaderMSD = new ChromatogramMSDReaderVersion110();
				readOverviewMSD(file, chromatogramReaderMSD, monitor, processingInfo);
			} else if(XmlReader10.VERSION.equals(version)) {
				IChromatogramMSDReader chromatogramReaderMSD = new ChromatogramMSDReaderVersion10();
				readOverviewMSD(file, chromatogramReaderMSD, monitor, processingInfo);
			}
		}
		return processingInfo;
	}

	private void readOverviewMSD(File file, IChromatogramMSDReader chromatogramMSDReader, IProgressMonitor monitor, IProcessingInfo<IChromatogramOverview> processingInfo) {

		monitor.subTask(IMPORT_CHROMATOGRAM_OVERVIEW);
		try {
			IChromatogramOverview chromatogramOverview = chromatogramMSDReader.readOverview(file, monitor);
			processingInfo.setProcessingResult(chromatogramOverview);
		} catch(IOException e) {
			logger.warn(e);
			processingInfo.addErrorMessage(DESCRIPTION, ERROR + file.getAbsolutePath());
		}
	}
}
