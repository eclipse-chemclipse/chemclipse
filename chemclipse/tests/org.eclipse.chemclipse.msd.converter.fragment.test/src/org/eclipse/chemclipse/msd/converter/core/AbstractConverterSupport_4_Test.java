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
package org.eclipse.chemclipse.msd.converter.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.eclipse.chemclipse.converter.core.IConverterSupportSetter;
import org.junit.Before;
import org.junit.Test;

public class AbstractConverterSupport_4_Test extends AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport;
	private String[] filterNames;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		converterSupport = getConverterSupport();
		filterNames = converterSupport.getFilterNames(IConverterSupport.EXPORT_SUPPLIER);
	}

	@Test
	public void testGetExportableFilterNames_1() {

		int size = filterNames.length;
		assertEquals(3, size);
	}

	@Test
	public void testGetExportableFilterNames_2() {

		String extension = filterNames[0];
		assertEquals("ANDI/AIA CDF Chromatogram (*.CDF)", extension);
	}

	@Test
	public void testGetExportableFilterNames_3() {

		String extension = filterNames[1];
		assertEquals("Excel Chromatogram (*.xlsx)", extension);
	}

	@Test
	public void testGetExportableFilterNames_4() {

		String extension = filterNames[2];
		assertEquals("Test Chromatogram (*.C/CHROM.MS)", extension);
	}
}
