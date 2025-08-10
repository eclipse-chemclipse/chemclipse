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
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.junit.Test;

public class SelectedIons_2_Test {

	private IMarkedIons selectedIons = new MarkedIons(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(4.9f)));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new MarkedIon(5));
		assertTrue("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(4.9f)));
	}

	@Test
	public void testContains_3() {

		selectedIons.add(new MarkedIon(5));
		selectedIons.remove(new MarkedIon(5));
		assertFalse("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(5.4f)));
	}

	@Test
	public void testContains_4() {

		selectedIons.add(new MarkedIon(10));
		selectedIons.add(new MarkedIon(5));
		selectedIons.add(new MarkedIon(20));
		assertTrue("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(20.2f)));
	}

	@Test
	public void testContains_5() {

		selectedIons.add(50, 60);
		Set<Integer> list = selectedIons.getIonsNominal();
		assertEquals("size", 11, list.size());
		for(int i = 50; i <= 60; i++) {
			assertTrue("ion " + i, list.contains(i));
		}
	}
}
