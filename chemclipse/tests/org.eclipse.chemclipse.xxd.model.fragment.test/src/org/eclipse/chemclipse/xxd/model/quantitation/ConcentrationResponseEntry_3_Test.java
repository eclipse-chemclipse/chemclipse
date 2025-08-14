/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.model.quantitation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.quantitation.IResponseSignal;
import org.eclipse.chemclipse.model.quantitation.ResponseSignal;
import org.junit.Before;
import org.junit.Test;

public class ConcentrationResponseEntry_3_Test {

	private IResponseSignal concentrationResponseEntry1;
	private IResponseSignal concentrationResponseEntry2;

	@Before
	public void setUp() {

		concentrationResponseEntry1 = new ResponseSignal(76.2d, 0.7d, 47875);
		concentrationResponseEntry2 = new ResponseSignal(75.2d, 0.7d, 47875);
	}

	@Test
	public void testEquals_1() {

		assertFalse(concentrationResponseEntry1.equals(concentrationResponseEntry2));
	}

	@Test
	public void testEquals_2() {

		assertFalse(concentrationResponseEntry2.equals(concentrationResponseEntry1));
	}

	@Test
	public void testHashCode_1() {

		assertTrue(concentrationResponseEntry1.hashCode() != concentrationResponseEntry2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertTrue(concentrationResponseEntry2.hashCode() != concentrationResponseEntry1.hashCode());
	}

	@Test
	public void testToString_1() {

		assertTrue(concentrationResponseEntry1.toString() != concentrationResponseEntry2.toString());
	}

	@Test
	public void testToString_2() {

		assertTrue(concentrationResponseEntry2.toString() != concentrationResponseEntry1.toString());
	}
}
