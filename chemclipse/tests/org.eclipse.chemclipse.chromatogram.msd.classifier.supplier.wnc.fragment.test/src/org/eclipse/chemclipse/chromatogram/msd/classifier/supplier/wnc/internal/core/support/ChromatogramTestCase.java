/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.internal.core.support;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;

import org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.TestPathHelper;
import org.eclipse.chemclipse.model.settings.Delimiter;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class ChromatogramTestCase {

	protected IChromatogramMSD chromatogram;
	protected IChromatogramSelectionMSD chromatogramSelection;
	protected File chromatogramFile;

	@Before
	public void setUp() throws Exception {

		PreferenceSupplier.setImportDelimiter(Delimiter.SEMICOLON);
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_ZIP))));
		zipInputStream.getNextEntry();
		String inputChromatogramFile = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_FOLDER);
		inputChromatogramFile += File.separator + TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1_NAME;
		chromatogramFile = new File(inputChromatogramFile);
		if(chromatogramFile.exists()) {
			chromatogramFile.delete();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(chromatogramFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 2048);
		int count;
		int buffer = 2048;
		byte data[] = new byte[buffer];
		while((count = zipInputStream.read(data, 0, buffer)) != -1) {
			bufferedOutputStream.write(data, 0, count);
		}
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		zipInputStream.close();
		/*
		 * Read the chromatogram
		 */
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(chromatogramFile, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@After
	public void tearDown() throws Exception {

		PreferenceSupplier.setImportDelimiter(Delimiter.COMMA);

		chromatogramFile.delete();
	}

	public IChromatogramSelectionMSD getChromatogramSelection() {

		return chromatogramSelection;
	}
}
