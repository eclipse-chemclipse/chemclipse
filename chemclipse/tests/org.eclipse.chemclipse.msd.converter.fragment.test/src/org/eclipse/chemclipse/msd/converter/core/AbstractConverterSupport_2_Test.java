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
import org.junit.Before;
import org.junit.Test;

public class AbstractConverterSupport_2_Test extends AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		converterSupport = getConverterSupport();
	}

	@Test
	public void testGetConverterIdByName_1() {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.agilent";
		assertEquals(converterId, converterSupport.getSupplier().get(0).getId());
	}

	@Test
	public void testGetConverterIdByName_2() {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.agilent.msd1";
		assertEquals(converterId, converterSupport.getSupplier().get(1).getId());
	}

	@Test
	public void testGetConverterIdByName_3() {

		String converterId = "net.openchrom.msd.converter.supplier.cdf";
		assertEquals(converterId, converterSupport.getSupplier().get(2).getId());
	}

	@Test
	public void testGetConverterIdByName_4() {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.excel";
		assertEquals(converterId, converterSupport.getSupplier().get(3).getId());
	}

	@Test
	public void testGetConverterIdByName_5() {

		String converterId = "org.eclipse.chemclipse.msd.converter.supplier.test";
		assertEquals(converterId, converterSupport.getSupplier().get(4).getId());
	}

	@Test
	public void testGetConverterIdByName_6() {

		assertThrows(IndexOutOfBoundsException.class, () -> converterSupport.getSupplier().get(-1).getId());
	}

	@Test
	public void testGetConverterIdByName_7() {

		assertThrows(IndexOutOfBoundsException.class, () -> converterSupport.getSupplier().get(5).getId());
	}
}
