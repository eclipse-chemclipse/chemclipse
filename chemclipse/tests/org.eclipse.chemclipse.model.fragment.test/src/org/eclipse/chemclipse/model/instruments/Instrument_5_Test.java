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

public class Instrument_5_Test {

	private Instrument instrument;

	@Before
	public void setUp() throws Exception {

		instrument = new Instrument("", "", "");
	}

	@Test
	public void test1() {

		assertEquals("", instrument.getIdentifier());
	}

	@Test
	public void test2() {

		assertEquals("", instrument.getName());
	}

	@Test
	public void test3() {

		assertEquals("", instrument.getDescription());
	}
}
