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
import static org.junit.Assert.assertFalse;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Test;

/**
 * Exception test.
 */
public class Ion_6_Test {

	@Test
	public void testException_1() {

		IIon ion = new Ion(IIon.TIC_ION, -1.0f);
		assertEquals(0d, ion.getIon(), 0);
		assertEquals(0f, ion.getAbundance(), 0);
	}

	@Test
	public void testException_2() {

		IIon ion = new Ion(IIon.TIC_ION, 1.0f);
		assertFalse(ion.setAbundance(-1.0f));
	}

	@Test
	public void testException_3() {

		IIon ion = new Ion(-1.0f, 1.0f);
		assertEquals(0d, ion.getIon(), 0);
		assertEquals(1.0f, ion.getAbundance(), 0);
	}

	@Test
	public void testException_4() {

		Ion ion = new Ion(1.0f, 1.0f);
		assertFalse(ion.setIon(-1.0f));
	}
}
