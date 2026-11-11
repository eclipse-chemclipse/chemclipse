/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.jupiter.api.Test;

/**
 * Value validation of {@link Ion}: negative m/z and abundance values are
 * rejected, everything else is accepted.
 */
public class SupplierIon_1_Test {

	@Test
	public void testConstructorStoresValues() {

		IIon ion = new Ion(43.5d, 1200.0f);
		assertEquals(43.5d, ion.getIon());
		assertEquals(1200.0f, ion.getAbundance());
	}

	@Test
	public void testConstructorWithoutAbundance() {

		IIon ion = new Ion(43.5d);
		assertEquals(43.5d, ion.getIon());
		assertEquals(0.0f, ion.getAbundance());
	}

	@Test
	public void testConstructorIgnoresNegativeIon() {

		IIon ion = new Ion(-0.1d, 100.0f);
		assertEquals(0.0d, ion.getIon());
		assertEquals(100.0f, ion.getAbundance());
	}

	@Test
	public void testConstructorIgnoresNegativeAbundance() {

		IIon ion = new Ion(43.5d, -0.1f);
		assertEquals(43.5d, ion.getIon());
		assertEquals(0.0f, ion.getAbundance());
	}

	@Test
	public void testSetIon() {

		IIon ion = new Ion(1.0d);
		assertTrue(ion.setIon(0.4d));
		assertEquals(0.4d, ion.getIon());
	}

	@Test
	public void testSetIonZero() {

		IIon ion = new Ion(1.0d);
		assertTrue(ion.setIon(0.0d));
		assertEquals(0.0d, ion.getIon());
	}

	@Test
	public void testSetIonHasNoUpperLimit() {

		IIon ion = new Ion(1.0d);
		assertTrue(ion.setIon(65535.1d));
		assertEquals(65535.1d, ion.getIon());
	}

	@Test
	public void testSetIonRejectsNegative() {

		IIon ion = new Ion(1.0d);
		assertFalse(ion.setIon(-0.1d));
		assertEquals(1.0d, ion.getIon());
	}

	@Test
	public void testSetAbundance() {

		IIon ion = new Ion(1.0d, 10.0f);
		assertTrue(ion.setAbundance(0.0f));
		assertEquals(0.0f, ion.getAbundance());
	}

	@Test
	public void testSetAbundanceRejectsNegative() {

		IIon ion = new Ion(1.0d, 10.0f);
		assertFalse(ion.setAbundance(-0.1f));
		assertEquals(10.0f, ion.getAbundance());
	}

	@Test
	public void testCopyConstructor() {

		IIon copy = new Ion(new Ion(43.5d, 1200.0f));
		assertEquals(43.5d, copy.getIon());
		assertEquals(1200.0f, copy.getAbundance());
	}

	@Test
	public void testCopyConstructorRejectsNull() {

		assertThrows(IllegalArgumentException.class, () -> new Ion((IIon)null));
	}
}
