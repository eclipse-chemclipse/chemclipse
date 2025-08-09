/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TripleQuadMethod_1_Test {

	private TripleQuadMethod tripleQuadMethod = new TripleQuadMethod();

	@Test
	public void test1() {

		assertEquals("QQQ", tripleQuadMethod.getInstrumentName());
	}

	@Test
	public void test2() {

		assertEquals("EI", tripleQuadMethod.getIonSource());
	}

	@Test
	public void test3() {

		assertEquals("ByChromatographTime", tripleQuadMethod.getStopMode());
	}

	@Test
	public void test4() {

		assertEquals(60000, tripleQuadMethod.getStopTime());
	}

	@Test
	public void test5() {

		assertEquals(360000, tripleQuadMethod.getSolventDelay());
	}

	@Test
	public void test6() {

		assertTrue(tripleQuadMethod.isCollisionGasOn());
	}

	@Test
	public void test7() {

		assertEquals(700, tripleQuadMethod.getTimeFilterPeakWidth());
	}

	@Test
	public void test8() {

		assertEquals(230.0d, tripleQuadMethod.getSourceHeater(), 0);
	}

	@Test
	public void test9() {

		assertEquals(5.0d, tripleQuadMethod.getSamplingRate(), 0);
	}
}