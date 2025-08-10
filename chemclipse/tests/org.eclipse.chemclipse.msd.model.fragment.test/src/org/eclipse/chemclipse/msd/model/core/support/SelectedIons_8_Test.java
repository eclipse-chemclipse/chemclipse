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

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.junit.Before;
import org.junit.Test;

public class SelectedIons_8_Test {

	private IMarkedIons selectedIons;

	@Before
	public void setUp() throws Exception {

		selectedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		selectedIons.add(new MarkedIon(28.82849943f));
		selectedIons.add(new MarkedIon(28.787f));
		selectedIons.add(new MarkedIon(29));
		selectedIons.add(new MarkedIon(29.267849f));
		selectedIons.add(new MarkedIon(30.96f));
		selectedIons.add(new MarkedIon(31));
	}

	@Test
	public void testSize_1() {

		assertEquals(6, selectedIons.size());
	}
}
