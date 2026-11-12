/*******************************************************************************
 * Copyright (c) 2015, 2026 Michael Chang.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Michael Chang - initial API and implementation
 * Philip Wenig - improvements
 * Christoph Läubrich - update latest version
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.ocx.io;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.converter.l10n.ConverterMessages;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1005;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1006;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1007;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1100;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1300;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1301;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1400;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1500;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1501;
import org.eclipse.chemclipse.wsd.converter.supplier.ocx.internal.io.ChromatogramWriter_1502;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.ChromatogramReferencesSupport;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class ChromatogramWriterWSD extends AbstractChromatogramWriter implements IChromatogramWSDZipWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramWSD chromatogram, IProgressMonitor monitor) throws FileIsNotWriteableException, IOException {

		boolean exportReferences = PreferenceSupplier.isChromatogramExportReferencesSeparately();
		SubMonitor subMonitor = SubMonitor.convert(monitor, exportReferences ? 2 : 1);
		IChromatogramWSDZipWriter chromatogramWriter = getChromatogramWriter(monitor);
		chromatogramWriter.writeChromatogram(file, chromatogram, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
		/*
		 * Export References
		 */
		if(exportReferences) {
			ChromatogramReferencesSupport.exportReferences(file, chromatogram, subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE));
		}
	}

	@Override
	public void writeChromatogram(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramWSD chromatogram, IProgressMonitor monitor) throws IOException {

		IChromatogramWSDZipWriter chromatogramWriter = getChromatogramWriter(monitor);
		chromatogramWriter.writeChromatogram(zipOutputStream, directoryPrefix, chromatogram, monitor);
	}

	private IChromatogramWSDZipWriter getChromatogramWriter(IProgressMonitor monitor) {

		monitor.setTaskName("Open Chromatography Binary");
		monitor.subTask(ConverterMessages.exportChromatogram);
		IChromatogramWSDZipWriter chromatogramWriter = getChromatogramWriter(Format.CHROMATOGRAM_VERSION_LATEST);

		return chromatogramWriter;
	}

	private IChromatogramWSDZipWriter getChromatogramWriter(String version) {

		IChromatogramWSDZipWriter chromatogramWriter = null;

		if(version.equals(Format.CHROMATOGRAM_VERSION_1005)) {
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

		return chromatogramWriter;
	}
}
