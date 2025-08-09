/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.junit.Test;

public class QuantitationEntry_1_Test {

	private IQuantitationEntry quantitationEntry = new QuantitationEntry("Styrene", 10.0d, "mg/kg", 5000.8d);

	@Test
	public void test1() {

		assertEquals("", quantitationEntry.getDescription());
	}

	@Test
	public void test2() {

		quantitationEntry.appendDescription(null);
		assertEquals("", quantitationEntry.getDescription());
	}

	@Test
	public void test3() {

		quantitationEntry.appendDescription("");
		assertEquals("", quantitationEntry.getDescription());
	}

	@Test
	public void test4() {

		quantitationEntry.appendDescription("Test");
		assertEquals("Test", quantitationEntry.getDescription());
	}

	@Test
	public void test5() {

		quantitationEntry.appendDescription("Test");
		quantitationEntry.appendDescription("Test");
		assertEquals("Test", quantitationEntry.getDescription());
	}

	@Test
	public void test6() {

		quantitationEntry.appendDescription("Test");
		quantitationEntry.appendDescription("Demo");
		assertEquals("Test | Demo", quantitationEntry.getDescription());
	}

	@Test
	public void test7() {

		quantitationEntry.appendDescription("Test");
		quantitationEntry.appendDescription("Demo");
		quantitationEntry.appendDescription("Test");
		assertEquals("Test | Demo", quantitationEntry.getDescription());
	}
}
