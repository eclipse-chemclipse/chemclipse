/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.massspectrum;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.msd.converter.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

/**
 * This class validates the exceptions thrown by
 * AbstractMassSpectrumImportConverter. Because
 * AbstractMassSpectrumImportConverter is an abstract class,
 * TestMassSpectrumImportConverter is instantiated which extends
 * AbstractMassSpectrumImportConverter.
 */
public class AbstractMassSpectrumImportConverter_1_Test {

	TestMassSpectrumImportConverter importConverter = new TestMassSpectrumImportConverter();

	@Test
	public void testFileNotFoundException_1() {

		File file = new File("");
		IProcessingInfo<IMassSpectra> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testFileIsNotReadableException_1() throws IOException {

		File file = null;
		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MASSSPECTRUM_NOT_READABLE));
		file.setReadable(false);
		IProcessingInfo<IMassSpectra> prcoessingInfo = importConverter.convert(file, new NullProgressMonitor());
		assertTrue(prcoessingInfo.hasErrorMessages());
		if(file != null) {
			file.setReadable(true);
		}
	}

	@Test
	public void testFileIsEmptyException_1() throws IOException {

		File file = null;
		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_MASSSPECTRUM_EMPTY));
		IProcessingInfo<IMassSpectra> prcoessingInfo = importConverter.convert(file, new NullProgressMonitor());
		assertTrue(prcoessingInfo.hasErrorMessages());
	}
}
