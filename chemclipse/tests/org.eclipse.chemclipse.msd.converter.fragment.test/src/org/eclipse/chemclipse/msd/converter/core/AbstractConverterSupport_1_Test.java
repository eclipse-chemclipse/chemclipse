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
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.converter.core.IConverterSupportSetter;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.junit.Before;
import org.junit.Test;

public class AbstractConverterSupport_1_Test extends AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		converterSupport = getConverterSupport();
	}

	@Test
	public void testGetConverterIdByName_1() throws NoConverterAvailableException {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.agilent";
		String converterName = "Agilent Chromatogram (*.D/DATA.MS)";
		assertEquals(converterId, converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_2() throws NoConverterAvailableException {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.agilent.msd1";
		String converterName = "Agilent Chromatogram (*.D/MSD1.MS)";
		assertEquals(converterId, converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_3() throws NoConverterAvailableException {

		String converterId = "net.openchrom.msd.converter.supplier.cdf";
		String converterName = "ANDI/AIA CDF Chromatogram (*.CDF)";
		assertEquals(converterId, converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_4() throws NoConverterAvailableException {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.excel";
		String converterName = "Excel Chromatogram (*.xlsx)";
		assertEquals(converterId, converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_5() throws NoConverterAvailableException {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.test";
		String converterName = "Test Chromatogram (*.C/CHROM.MS)";
		assertEquals(converterId, converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_6() {

		String converterName = "org.eclipse.chemclipse.msd.converter";
		assertThrows(NoConverterAvailableException.class, () -> converterSupport.getConverterId(converterName, false));
	}

	@Test
	public void testGetConverterIdByName_7() {

		String converterName = "";
		assertThrows(NoConverterAvailableException.class, () -> converterSupport.getConverterId(converterName, false));
	}
}
