/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.jcampdx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.converter.supplier.jcampdx.converter.ScanImportConverterNMR;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class BrukerAFFN_ITest {

	private IComplexSignalMeasurement<?> complexSignalMeasurement;

	@Before
	public void setUp() {

		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.REAL_SPECTRUM_ASCII_FREE_FORMAT_NUMERIC));
		ScanImportConverterNMR importConverter = new ScanImportConverterNMR();
		IProcessingInfo<IComplexSignalMeasurement<?>> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		complexSignalMeasurement = processingInfo.getProcessingResult();
	}

	@Test
	public void testLoading() {

		assertNotNull(complexSignalMeasurement);
	}

	@Test
	public void testSignals() {

		assertEquals(16754, complexSignalMeasurement.getSignals().size());
	}
}
