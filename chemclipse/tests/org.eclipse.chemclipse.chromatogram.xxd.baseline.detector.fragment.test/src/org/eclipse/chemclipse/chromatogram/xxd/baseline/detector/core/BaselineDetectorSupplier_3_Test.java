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
package org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BaselineDetectorSupplier_3_Test {

	private BaselineDetectorSupplier supplier1;
	private BaselineDetectorSupplier supplier2;

	@Before
	public void setUp() throws Exception {

		supplier1 = new BaselineDetectorSupplier();
		supplier1.setId("id");
		supplier1.setDescription("description");
		supplier1.setDetectorName("detectorName");

		supplier2 = new BaselineDetectorSupplier();
		supplier2.setId("id");
		supplier2.setDescription("description");
		supplier2.setDetectorName("detectorName");
	}

	@Test
	public void testEquals_1() {

		assertEquals("equals", supplier1, supplier2);
	}

	@Test
	public void testEquals_2() {

		assertEquals("equals", supplier2, supplier1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", supplier1);
	}

	@Test
	public void testEquals_4() {

		assertNotEquals("equals", supplier1, new Object());
	}

	@Test
	public void testHashCode_1() {

		assertEquals("hashCode", supplier1.hashCode(), supplier2.hashCode());
	}
}
