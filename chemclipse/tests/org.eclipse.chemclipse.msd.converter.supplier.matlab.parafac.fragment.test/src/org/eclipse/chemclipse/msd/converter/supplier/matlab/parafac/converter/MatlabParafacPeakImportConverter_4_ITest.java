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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.converter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

public class MatlabParafacPeakImportConverter_4_ITest {

	private MatlabParafacPeakImportConverter converter = new MatlabParafacPeakImportConverter();

	@Test
	public void testImport_1() {

		assertThrows(NullPointerException.class, () -> {
			File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_EMPTY));
			converter.convert(file, new NullProgressMonitor());
		});
	}

	@Test
	public void testImport_2() {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_NOT_READABLE));
		file.setReadable(false);
		IProcessingInfo<IPeaksMSD> processingInfo = converter.convert(file, new NullProgressMonitor());
		assertNull(processingInfo.getProcessingResult());
		file.setReadable(true);
	}

	@Test
	public void testImport_3() {

		assertThrows(NullPointerException.class, () -> {
			File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS));
			converter.convert(file, new NullProgressMonitor());
		});
	}

	@Test
	public void testImport_4() {

		assertThrows(NullPointerException.class, () -> {
			File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_EXTENSION));
			converter.convert(file, new NullProgressMonitor());
		});
	}
}
