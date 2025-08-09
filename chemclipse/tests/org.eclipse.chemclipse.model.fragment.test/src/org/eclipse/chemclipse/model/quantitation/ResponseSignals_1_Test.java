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
package org.eclipse.chemclipse.model.quantitation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResponseSignals_1_Test {

	IResponseSignals responseSignals = new ResponseSignals();

	@Test
	public void test1() {

		assertEquals(0.0d, responseSignals.getMinResponseValue(), 0);
	}

	@Test
	public void test2() {

		assertEquals(0.0d, responseSignals.getMaxResponseValue(), 0);
	}

	@Test
	public void test3() {

		assertEquals(0.0d, responseSignals.getMinResponseValue(103), 0);
	}

	@Test
	public void test4() {

		assertEquals(0.0d, responseSignals.getMaxResponseValue(103), 0);
	}

	@Test
	public void test5() {

		assertEquals(0.0d, responseSignals.getMinResponseValue(104), 0);
	}

	@Test
	public void test6() {

		assertEquals(0.0d, responseSignals.getMaxResponseValue(104), 0);
	}
}
