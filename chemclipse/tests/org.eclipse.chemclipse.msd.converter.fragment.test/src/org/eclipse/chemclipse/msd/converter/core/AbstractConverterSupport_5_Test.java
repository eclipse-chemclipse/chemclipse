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

import java.util.List;

import org.eclipse.chemclipse.converter.core.IConverterSupportSetter;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.junit.Before;
import org.junit.Test;

public class AbstractConverterSupport_5_Test extends AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport;
	private List<ISupplier> supplier;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		converterSupport = getConverterSupport();
		supplier = converterSupport.getSupplier();
	}

	@Test
	public void testGetSupplier_1() {

		assertEquals(5, supplier.size());
	}

	@Test
	public void testGetSupplier_2() {

		assertEquals("org.eclipse.chemclipse.msd.converter.supplier.agilent", supplier.get(0).getId());
	}

	@Test
	public void testGetSupplier_3() {

		assertEquals("org.eclipse.chemclipse.msd.converter.supplier.agilent.msd1", supplier.get(1).getId());
	}

	@Test
	public void testGetSupplier_4() {

		assertEquals("net.openchrom.msd.converter.supplier.cdf", supplier.get(2).getId());
	}

	@Test
	public void testGetSupplier_5() {

		assertEquals("org.eclipse.chemclipse.msd.converter.supplier.excel", supplier.get(3).getId());
	}

	@Test
	public void testGetSupplier_6() {

		assertEquals("org.eclipse.chemclipse.msd.converter.supplier.test", supplier.get(4).getId());
	}
}
