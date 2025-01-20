/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add new version
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.ocx.io;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.chemclipse.csd.converter.io.AbstractChromatogramCSDReader;
import org.eclipse.chemclipse.csd.converter.io.IChromatogramCSDReader;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1001;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1002;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1003;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1004;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1005;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1006;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1007;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1100;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1300;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1301;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1400;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1500;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1501;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramReader_1502;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.IVendorScan;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.VendorChromatogram;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram.VendorScan;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.converter.supplier.ocx.io.ChromatogramReaderMSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.Format;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.internal.support.ReaderHelper;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramReaderCSD extends AbstractChromatogramCSDReader implements IChromatogramCSDZipReader {

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
		IChromatogramCSDReader chromatogramReader = getChromatogramReader(version);
		if(chromatogramReader != null) {
			try {
				chromatogramOverview = chromatogramReader.readOverview(file, monitor);
			} catch(Exception e) {
				chromatogramOverview = createChromatogramFIDFromMSD(file, monitor);
			}
		} else {
			chromatogramOverview = createChromatogramFIDFromMSD(file, monitor);
		}
		return chromatogramOverview;
	}

	@Override
	public IChromatogramCSD read(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramCSD chromatogramCSD = null;
		ReaderHelper readerHelper = new ReaderHelper();
		String version = readerHelper.getVersion(file);
		/*
		 * It's used to support older versions of
		 * the *.ocb format.
		 */
		IChromatogramCSDReader chromatogramReader = getChromatogramReader(version);
		if(chromatogramReader != null) {
			try {
				chromatogramCSD = chromatogramReader.read(file, monitor);
			} catch(Exception e) {
				chromatogramCSD = createChromatogramFIDFromMSD(file, monitor);
			}
		} else {
			chromatogramCSD = createChromatogramFIDFromMSD(file, monitor);
		}
		return chromatogramCSD;
	}

	@Override
	public IChromatogramCSD read(ZipInputStream zipInputStream, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readChromatogram(zipInputStream, directoryPrefix, monitor);
	}

	@Override
	public IChromatogramCSD read(ZipFile zipFile, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		return readChromatogram(zipFile, directoryPrefix, monitor);
	}

	private IChromatogramCSD readChromatogram(Object object, String directoryPrefix, IProgressMonitor monitor) throws IOException {

		IChromatogramCSDZipReader chromatogramReader = null;
		IChromatogramCSD chromatogramCSD = null;
		ReaderHelper readerHelper = new ReaderHelper();
		//
		String version = readerHelper.getVersion(object, directoryPrefix);
		chromatogramReader = getChromatogramReader(version);
		//
		if(chromatogramReader != null) {
			if(object instanceof ZipInputStream zipInputStream) {
				chromatogramCSD = chromatogramReader.read(zipInputStream, directoryPrefix, monitor);
			} else if(object instanceof ZipFile zipFile) {
				chromatogramCSD = chromatogramReader.read(zipFile, directoryPrefix, monitor);
			}
		}
		//
		return chromatogramCSD;
	}

	private IChromatogramCSDZipReader getChromatogramReader(String version) {

		IChromatogramCSDZipReader chromatogramReader = null;
		//
		if(version.equals(Format.CHROMATOGRAM_VERSION_1001)) {
			chromatogramReader = new ChromatogramReader_1001();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1002)) {
			chromatogramReader = new ChromatogramReader_1002();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1003)) {
			chromatogramReader = new ChromatogramReader_1003();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1004)) {
			chromatogramReader = new ChromatogramReader_1004();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1005)) {
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

	private IChromatogramCSD createChromatogramFIDFromMSD(File file, IProgressMonitor monitor) throws IOException {

		IChromatogramCSD chromatogramFID = null;
		/*
		 * Is the force modus used?
		 */
		if(PreferenceSupplier.isForceLoadAlternateDetector()) {
			ChromatogramReaderMSD chromatogramReaderMSD = new ChromatogramReaderMSD();
			IChromatogramOverview chromatogramOverview = chromatogramReaderMSD.readOverview(file, monitor);
			if(chromatogramOverview instanceof IChromatogram<?> chromatogram) {
				chromatogramFID = new VendorChromatogram();
				for(IScan scan : chromatogram.getScans()) {
					IVendorScan scanCSD = new VendorScan(scan.getRetentionTime(), scan.getTotalSignal());
					scanCSD.setRelativeRetentionTime(scan.getRelativeRetentionTime());
					scanCSD.setRetentionIndex(scan.getRetentionIndex());
					scanCSD.setTimeSegmentId(scan.getTimeSegmentId());
					scanCSD.setCycleNumber(scan.getCycleNumber());
					chromatogramFID.addScan(scanCSD);
				}
				//
				chromatogramFID.setConverterId(Format.CONVERTER_ID_CHROMATOGRAM);
				File fileConverted = new File(file.getAbsolutePath().replace(".ocb", "-fromMSD.ocb"));
				chromatogramFID.setFile(fileConverted);
				// Delay
				int startRetentionTime = chromatogramFID.getStartRetentionTime();
				int scanDelay = startRetentionTime;
				chromatogramFID.setScanDelay(scanDelay);
				// Interval
				int endRetentionTime = chromatogramFID.getStopRetentionTime();
				int scanInterval = endRetentionTime / chromatogramFID.getNumberOfScans();
				chromatogramFID.setScanInterval(scanInterval);
			}
		}
		//
		return chromatogramFID;
	}
}