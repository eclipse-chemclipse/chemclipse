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
 * Christoph Läubrich - update latest version
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.ocx.io;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1001;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1002;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1003;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1004;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1005;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1006;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1007;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1100;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1300;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1301;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1400;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1500;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1501;
import org.eclipse.chemclipse.csd.converter.supplier.ocx.internal.io.ChromatogramWriter_1502;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.ChromatogramReferencesSupport;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriterCSD extends AbstractChromatogramWriter implements IChromatogramCSDZipWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		writeChromatogram(file, Format.CHROMATOGRAM_VERSION_LATEST, chromatogram, monitor);
	}

	public void writeChromatogram(File file, String version, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		IChromatogramCSDZipWriter chromatogramWriter = getChromatogramWriter(chromatogram, version, monitor);
		chromatogramWriter.writeChromatogram(file, chromatogram, monitor);
		/*
		 * Export References
		 */
		if(PreferenceSupplier.isChromatogramExportReferencesSeparately()) {
			ChromatogramReferencesSupport.exportReferences(file, chromatogram, monitor);
		}
	}

	@Override
	public void writeChromatogram(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramCSD chromatogram, IProgressMonitor monitor) throws IOException {

		IChromatogramCSDZipWriter chromatogramWriter = getChromatogramWriter(chromatogram, Format.CHROMATOGRAM_VERSION_LATEST, monitor);
		chromatogramWriter.writeChromatogram(zipOutputStream, directoryPrefix, chromatogram, monitor);
	}

	private IChromatogramCSDZipWriter getChromatogramWriter(IChromatogramCSD chromatogram, String version, IProgressMonitor monitor) {

		monitor.setTaskName("Open Chromatography Binary");
		monitor.subTask(ConverterMessages.exportChromatogram);
		IChromatogramCSDZipWriter chromatogramWriter = getChromatogramWriter(version);
		//
		return chromatogramWriter;
	}

	private IChromatogramCSDZipWriter getChromatogramWriter(String version) {

		IChromatogramCSDZipWriter chromatogramWriter = null;
		//
		if(version.equals(Format.CHROMATOGRAM_VERSION_1001)) {
			chromatogramWriter = new ChromatogramWriter_1001();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1002)) {
			chromatogramWriter = new ChromatogramWriter_1002();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1003)) {
			chromatogramWriter = new ChromatogramWriter_1003();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1004)) {
			chromatogramWriter = new ChromatogramWriter_1004();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1005)) {
			chromatogramWriter = new ChromatogramWriter_1005();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1006)) {
			chromatogramWriter = new ChromatogramWriter_1006();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1007)) {
			chromatogramWriter = new ChromatogramWriter_1007();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1100)) {
			chromatogramWriter = new ChromatogramWriter_1100();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1300)) {
			chromatogramWriter = new ChromatogramWriter_1300();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1301)) {
			chromatogramWriter = new ChromatogramWriter_1301();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1400)) {
			chromatogramWriter = new ChromatogramWriter_1400();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1500)) {
			chromatogramWriter = new ChromatogramWriter_1500();
		} else if(version.equals(Format.CHROMATOGRAM_VERSION_1501)) {
			chromatogramWriter = new ChromatogramWriter_1501();
		} else {
			chromatogramWriter = new ChromatogramWriter_1502();
		}
		//
		return chromatogramWriter;
	}
}