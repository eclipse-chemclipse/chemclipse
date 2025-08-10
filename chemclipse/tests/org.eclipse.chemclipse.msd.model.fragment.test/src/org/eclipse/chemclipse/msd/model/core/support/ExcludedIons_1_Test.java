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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.junit.Test;

public class ExcludedIons_1_Test {

	private IMarkedIons excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse("contains", excludedIons.getIonsNominal().contains(5));
	}

	@Test
	public void testContains_2() {

		excludedIons.add(new MarkedIon(5));
		assertTrue("contains", excludedIons.getIonsNominal().contains(5));
	}

	@Test
	public void testContains_3() {

		excludedIons.add(new MarkedIon(5));
		excludedIons.remove(new MarkedIon(5));
		assertFalse("contains", excludedIons.getIonsNominal().contains(5));
	}

	@Test
	public void testContains_4() {

		excludedIons.add(new MarkedIon(10));
		excludedIons.add(new MarkedIon(5));
		excludedIons.add(new MarkedIon(20));
		assertTrue("contains", excludedIons.getIonsNominal().contains(20));
	}

	@Test
	public void testContains_5() {

		excludedIons.add(10, 12);
		Set<Integer> excludedIonsNominal = excludedIons.getIonsNominal();
		assertFalse("contains", excludedIonsNominal.contains(20));
		assertTrue("contains", excludedIonsNominal.contains(10));
		assertTrue("contains", excludedIonsNominal.contains(11));
		assertTrue("contains", excludedIonsNominal.contains(12));
	}

	@Test
	public void testContains_6() {

		excludedIons.add(12, 10);
		Set<Integer> excludedIonsNominal = excludedIons.getIonsNominal();
		assertFalse("contains", excludedIonsNominal.contains(20));
		assertTrue("contains", excludedIonsNominal.contains(10));
		assertTrue("contains", excludedIonsNominal.contains(11));
		assertTrue("contains", excludedIonsNominal.contains(12));
	}

	@Test
	public void testContains_7() {

		excludedIons.add(12, 12);
		Set<Integer> excludedIonsNominal = excludedIons.getIonsNominal();
		assertFalse("contains", excludedIonsNominal.contains(20));
		assertFalse("contains", excludedIonsNominal.contains(10));
		assertFalse("contains", excludedIonsNominal.contains(11));
		assertTrue("contains", excludedIonsNominal.contains(12));
	}

	@Test
	public void testSize_8() {

		excludedIons.add(12, 12);
		assertEquals("size", 1, excludedIons.getIonsNominal().size());
	}

	@Test
	public void testSize_9() {

		excludedIons.add(new MarkedIon(58));
		excludedIons.add(new MarkedIon(48));
		excludedIons.add(new MarkedIon(372));
		assertEquals("size", 3, excludedIons.getIonsNominal().size());
	}

	@Test
	public void testSize_10() {

		assertEquals("size", 0, excludedIons.getIonsNominal().size());
	}
}
