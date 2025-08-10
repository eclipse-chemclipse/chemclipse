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

import static org.junit.Assert.assertNotEquals;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.junit.Before;
import org.junit.Test;

/**
 * Equals and hashCode test.
 * 
 * @author Philip Wenig
 */
public class SupplierIon_4_Test {

	private IIon ion1;
	private IIon ion2;

	@Before
	public void setUp() throws Exception {

		ion1 = new Ion(1.0f, 5726.4f);
		ion2 = new Ion(1.0f, 5716.4f);
	}

	@Test
	public void testEquals_1() {

		assertNotEquals("equals", ion1, ion2);
	}

	@Test
	public void testEquals_2() {

		assertNotEquals("equals", ion2, ion1);
	}

	@Test
	public void testHashCode_1() {

		assertNotEquals("hashCode", ion1.hashCode(), ion2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertNotEquals("hashCode", ion2.hashCode(), ion1.hashCode());
	}
}
