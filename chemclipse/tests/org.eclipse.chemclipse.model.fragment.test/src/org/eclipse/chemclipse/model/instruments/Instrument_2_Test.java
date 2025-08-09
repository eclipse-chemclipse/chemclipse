/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.instruments;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Instrument_2_Test {

	private Instrument instrument;

	@Before
	public void setUp() throws Exception {

		instrument = new Instrument("Instrument1", "GC-MS", "Research and Development");
	}

	@Test
	public void test1() {

		assertEquals("Instrument1", instrument.getIdentifier());
	}

	@Test
	public void test2() {

		assertEquals("GC-MS", instrument.getName());
	}

	@Test
	public void test3() {

		assertEquals("Research and Development", instrument.getDescription());
	}
}
