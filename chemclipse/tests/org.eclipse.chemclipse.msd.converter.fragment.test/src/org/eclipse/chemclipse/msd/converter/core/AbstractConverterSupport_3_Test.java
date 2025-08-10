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

public class AbstractConverterSupport_3_Test extends AbstractConverterTestCase {

	private IConverterSupportSetter converterSupport;
	private String[] filterExtensions;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		converterSupport = getConverterSupport();
		filterExtensions = converterSupport.getFilterExtensions(IConverterSupport.EXPORT_SUPPLIER);
	}

	@Test
	public void testGetExportableFilterExtensions_1() {

		int size = filterExtensions.length;
		assertEquals(3, size); // Important ... otherwise 'Save As...' fails
	}

	@Test
	public void testGetExportableFilterExtensions_2() {

		String extension = filterExtensions[0];
		assertEquals("*.CDF;*.cdf", extension);
	}

	@Test
	public void testGetExportableFilterExtensions_3() {

		String extension = filterExtensions[1];
		assertEquals("*.xlsx;*.XLSX", extension);
	}

	@Test
	public void testGetExportableFilterExtensions_4() {

		String extension = filterExtensions[2];
		assertEquals("*.", extension);
	}
}
