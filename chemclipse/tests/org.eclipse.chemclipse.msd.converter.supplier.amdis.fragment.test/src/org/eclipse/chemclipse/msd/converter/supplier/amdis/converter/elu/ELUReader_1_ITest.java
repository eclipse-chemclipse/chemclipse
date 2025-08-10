/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.elu;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.ELUReader;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ELUReader_1_ITest {

	private ELUReader reader;
	private File file;

	@Before
	public void setUp() throws Exception {

		reader = new ELUReader();
		String pathname = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_1_ELU);
		file = new File(pathname);
	}

	@Test
	public void testRead_1() throws IOException {

		IProcessingInfo<IPeaksMSD> processingInfo = reader.read(file, new NullProgressMonitor());
		assertFalse(processingInfo.hasErrorMessages());
	}
}
