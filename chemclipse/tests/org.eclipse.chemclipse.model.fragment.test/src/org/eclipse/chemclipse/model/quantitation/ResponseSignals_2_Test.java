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

import org.eclipse.chemclipse.model.core.ISignal;
import org.junit.Before;
import org.junit.Test;

public class ResponseSignals_2_Test {

	IResponseSignals responseSignals = new ResponseSignals();

	@Before
	public void setUp() throws Exception {

		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 0.5d, 500));
		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 1.0d, 1000));
		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 2.5d, 2500));
		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 5.0d, 5000));
		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 7.5d, 7500));
		responseSignals.add(new ResponseSignal(ISignal.TOTAL_INTENSITY, 10.0d, 10000));
	}

	@Test
	public void test1() {

		assertEquals(500.0d, responseSignals.getMinResponseValue(), 0);
	}

	@Test
	public void test2() {

		assertEquals(10000.0d, responseSignals.getMaxResponseValue(), 0);
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
