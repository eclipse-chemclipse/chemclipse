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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.junit.Test;

public class SelectedIons_4_Test {

	private IMarkedIons selectedIons = new MarkedIons(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(5.2f)));
	}

	@Test
	public void testContains_2() {

		selectedIons.add(new MarkedIon(5.2f));
		assertTrue("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(5.3f)));
	}

	@Test
	public void testContains_3() {

		selectedIons.add(new MarkedIon(5.2f));
		selectedIons.remove(new MarkedIon(5.3f));
		assertTrue("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_4() {

		selectedIons.add(new MarkedIon(10.2f));
		selectedIons.add(new MarkedIon(5.3f));
		selectedIons.add(new MarkedIon(20.4f));
		assertTrue("contains", selectedIons.getIonsNominal().contains(AbstractIon.getIon(20.4f)));
	}

	@Test
	public void testSize_9() {

		selectedIons.add(new MarkedIon(58.3f));
		selectedIons.add(new MarkedIon(48.2f));
		selectedIons.add(new MarkedIon(372.4f));
		assertEquals("size", 3, selectedIons.getIonsNominal().size());
	}

	@Test
	public void testSize_10() {

		assertEquals("size", 0, selectedIons.getIonsNominal().size());
	}
}
