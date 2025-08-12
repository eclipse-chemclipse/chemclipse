/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.nmr.converter.supplier.nmrml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collection;

import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.nmr.converter.supplier.nmrml.converter.ScanImportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MMBBI_10M12_CE01_1a_ITest {

	private Collection<IComplexSignalMeasurement<?>> spectrumNMR;

	@Before
	public void setUp() throws Exception {

		File file = new File(PathResolver.getAbsolutePath(TestPathHelper.MMBBI_10M12_CE01_1a));
		ScanImportConverter importConverter = new ScanImportConverter();
		IProcessingInfo<Collection<IComplexSignalMeasurement<?>>> processingInfo = importConverter.convert(file, new NullProgressMonitor());
		spectrumNMR = processingInfo.getProcessingResult();
	}

	@Test
	public void testLoading() {

		assertNotNull(spectrumNMR);
		assertFalse(spectrumNMR.isEmpty());
	}

	@Test
	public void testMeasuremnt() {

		IComplexSignalMeasurement<?> measurement = spectrumNMR.iterator().next();
		assertEquals("mica", measurement.getOperator());
		assertEquals(8459, measurement.getSignals().size());
	}
}