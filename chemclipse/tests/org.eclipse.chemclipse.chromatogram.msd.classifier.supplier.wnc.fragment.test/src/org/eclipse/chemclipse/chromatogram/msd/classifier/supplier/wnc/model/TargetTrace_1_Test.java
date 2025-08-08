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

import org.junit.Test;

public class TargetTrace_1_Test {

	private TargetTrace targetTrace;

	@Test
	public void testCreateWNCIon_1() {

		String name = "Water";
		int ion = 18;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals(name, targetTrace.getName());
	}

	@Test
	public void testCreateWNCIon_2() {

		String name = "Nitrogen";
		int ion = 28;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals(name, targetTrace.getName());
	}

	@Test
	public void testCreateWNCIon_3() {

		String name = "Nitro;gen";
		int ion = 28;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals("Nitrogen", targetTrace.getName());
	}

	@Test
	public void testCreateWNCIon_4() {

		String name = "Nitro:gen";
		int ion = 28;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals("Nitrogen", targetTrace.getName());
	}

	@Test
	public void testCreateWNCIon_5() {

		String name = "    Ni;tro:gen";
		int ion = 28;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals("Nitrogen", targetTrace.getName());
	}

	@Test
	public void testCreateWNCIon_6() {

		String name = "    Ni;tro:  gen    ";
		int ion = 28;
		targetTrace = new TargetTrace(ion, name);
		assertEquals(ion, targetTrace.getIon());
		assertEquals("Nitro  gen", targetTrace.getName());
	}
}