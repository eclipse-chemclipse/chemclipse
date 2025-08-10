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

public class ExcludedIons_4_Test {

	private IMarkedIons excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);

	@Test
	public void testContains_1() {

		assertFalse("contains", excludedIons.getIonsNominal().contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_2() {

		excludedIons.add(new MarkedIon(5.2f));
		assertTrue("contains", excludedIons.getIonsNominal().contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_3() {

		excludedIons.add(new MarkedIon(5.3f));
		excludedIons.remove(new MarkedIon(5.2f));
		assertTrue("contains", excludedIons.getIonsNominal().contains(AbstractIon.getIon(5.0f)));
	}

	@Test
	public void testContains_4() {

		excludedIons.add(new MarkedIon(10.4f));
		excludedIons.add(new MarkedIon(5.3f));
		excludedIons.add(new MarkedIon(20.2f));
		assertTrue("contains", excludedIons.getIonsNominal().contains(AbstractIon.getIon(20.3f)));
	}

	@Test
	public void testSize_9() {

		excludedIons.add(new MarkedIon(58.4f));
		excludedIons.add(new MarkedIon(48.3f));
		excludedIons.add(new MarkedIon(372.2f));
		assertEquals("size", 3, excludedIons.getIonsNominal().size());
	}

	@Test
	public void testSize_10() {

		assertEquals("size", 0, excludedIons.getIonsNominal().size());
	}
}
