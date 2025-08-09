/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.model.implementation.QuantitationEntry;
import org.junit.Before;
import org.junit.Test;

public class QuantitationEntry_2_Test {

	private Set<IQuantitationEntry> quantitationEntries = new HashSet<>();

	@Before
	public void setUp() throws Exception {

		quantitationEntries.add(new QuantitationEntry("Benzo(a)-pyren", "PAK", 0.25d, "mg/g", 2000.0d));
		quantitationEntries.add(new QuantitationEntry("Phenanthren", "PAK", 0.13d, "mg/g", 1000.0d));
		quantitationEntries.add(new QuantitationEntry("Fluoranthen", "PAK", 0.32d, "mg/g", 3000.0d));
	}

	@Test
	public void test1() {

		assertEquals(3, quantitationEntries.size());
	}

	@Test
	public void test2() {

		IQuantitationEntry quantitationEntry = new QuantitationEntry("Benzo(a)-pyren", "PAK", 0.25d, "mg/g", 2000.0d);
		quantitationEntries.add(quantitationEntry);
		assertEquals(3, quantitationEntries.size());
	}

	@Test
	public void test3() {

		IQuantitationEntry quantitationEntry = new QuantitationEntry("Benzo(a)-pyren", "", 0.25d, "mg/g", 2000.0d);
		quantitationEntries.add(quantitationEntry);
		assertEquals(4, quantitationEntries.size());
	}
}