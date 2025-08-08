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
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TargetTraces_1_Test {

	private TargetTrace targetTrace;
	private TargetTraces targetTraces;

	@Before
	public void setUp() throws Exception {

		targetTraces = new TargetTraces();
		targetTrace = new TargetTrace(18, "Water");
		targetTraces.add(targetTrace);
		targetTrace = new TargetTrace(28, "Nitrogen");
		targetTraces.add(targetTrace);
		targetTrace = new TargetTrace(44, "Carbon dioxide");
		targetTraces.add(targetTrace);
	}

	@Test
	public void testWNCIons_1() {

		assertEquals(3, targetTraces.getKeys().size());
	}

	@Test
	public void testWNCIons_2() {

		targetTrace = targetTraces.getTargetTrace(18);
		assertEquals(18, targetTrace.getIon());
		assertEquals("Water", targetTrace.getName());
	}

	@Test
	public void testWNCIons_3() {

		targetTrace = targetTraces.getTargetTrace(28);
		assertEquals(28, targetTrace.getIon());
		assertEquals("Nitrogen", targetTrace.getName());
	}

	@Test
	public void testWNCIons_4() {

		targetTrace = targetTraces.getTargetTrace(44);
		assertEquals(44, targetTrace.getIon());
		assertEquals("Carbon dioxide", targetTrace.getName());
	}

	@Test
	public void testWNCIons_5() {

		targetTrace = targetTraces.getTargetTrace(45);
		assertNull(targetTrace);
	}

	@Test
	public void testWNCIons_6() {

		targetTraces.remove(18);
		targetTraces.remove(28);
		assertEquals(1, targetTraces.getKeys().size());
		targetTrace = targetTraces.getTargetTrace(44);
		assertEquals(44, targetTrace.getIon());
		assertEquals("Carbon dioxide", targetTrace.getName());
	}

	@Test
	public void testWNCIons_7() {

		targetTraces.remove(18);
		targetTraces.remove(28);
		assertEquals(1, targetTraces.getKeys().size());
		targetTrace = targetTraces.getTargetTrace(18);
		assertNull(targetTrace);
		targetTrace = targetTraces.getTargetTrace(28);
		assertNull(targetTrace);
	}

	@Test
	public void testWNCIons_8() {

		targetTraces.remove(18);
		targetTraces.remove(28);
		targetTraces.remove(44);
		assertEquals(0, targetTraces.getKeys().size());
	}
}