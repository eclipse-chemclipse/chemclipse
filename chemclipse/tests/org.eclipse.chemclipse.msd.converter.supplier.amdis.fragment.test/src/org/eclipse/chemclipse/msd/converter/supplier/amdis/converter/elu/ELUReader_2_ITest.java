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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.elu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.PathResolver;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.ELUReader;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class ELUReader_2_ITest {

	private ELUReader reader;
	private IProcessingInfo<IPeaksMSD> processingInfo;

	@Before
	public void setUp() throws Exception {

		reader = new ELUReader();
		String pathname = PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_1_ELU);
		File file = new File(pathname);
		processingInfo = reader.read(file, new NullProgressMonitor());
	}

	@Test
	public void testRead_1() {

		try {
			IPeaksMSD peaks = processingInfo.getProcessingResult();
			assertEquals(1154, peaks.getPeaks().size());
			assertEquals(1132, peaks.getPeaks().stream().filter(p -> p.getPeakModel().isStrictModel()).toList().size());
		} catch(TypeCastException e) {
			assertTrue(false);
		}
	}
}
