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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Test;

/**
 * Constructor test.
 */
public class Ion_7_Test {

	@Test
	public void testConstructor_1() {

		IIon ion = new Ion(5.5f);
		assertEquals(5.5d, ion.getIon(), 0);
	}

	@Test
	public void testConstructor_2() {

		IIon ion = new Ion(-0.1f, 2593.5f);
		assertEquals(0d, ion.getIon(), 0);
		assertEquals(2593.5f, ion.getAbundance(), 0);
	}

	@Test
	public void testConstructor_3() {

		IIon ion = new Ion(-0.1f);
		assertEquals(0d, ion.getIon(), 0);
	}

	@Test
	public void testConstructor_4() {

		IIon ion = new Ion(1.0f, -0.1f);
		assertEquals(1d, ion.getIon(), 0);
		assertEquals(0f, ion.getAbundance(), 0);
	}
}
