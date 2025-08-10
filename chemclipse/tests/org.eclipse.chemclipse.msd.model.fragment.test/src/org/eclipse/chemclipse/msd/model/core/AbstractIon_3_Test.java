/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.math.IonRoundMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractIon_3_Test {

	private IonRoundMethod defaultMethod;

	@Before
	public void setUp() throws Exception {

		defaultMethod = IonRoundMethod.getActive();
	}

	@After
	public void tearDown() throws Exception {

		IonRoundMethod.setActive(defaultMethod);
	}

	@Test
	public void test1() {

		IonRoundMethod.setActive(IonRoundMethod.DEFAULT);
		assertEquals(18, AbstractIon.getIon(18.49d));
	}

	@Test
	public void test2() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_00);
		assertEquals(18, AbstractIon.getIon(18.0d));
	}

	@Test
	public void test3() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_01);
		assertEquals(18, AbstractIon.getIon(17.9d));
	}

	@Test
	public void test4() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_02);
		assertEquals(18, AbstractIon.getIon(17.8d));
	}

	@Test
	public void test5() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_03);
		assertEquals(18, AbstractIon.getIon(17.7d));
	}

	@Test
	public void test6() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_04);
		assertEquals(18, AbstractIon.getIon(17.6d));
	}

	@Test
	public void test7() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_05);
		assertEquals(18, AbstractIon.getIon(17.5d));
	}

	@Test
	public void test8() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_06);
		assertEquals(18, AbstractIon.getIon(17.4d));
	}

	@Test
	public void test9() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_07);
		assertEquals(18, AbstractIon.getIon(17.3d));
	}

	@Test
	public void test10() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_08);
		assertEquals(18, AbstractIon.getIon(17.2d));
	}

	@Test
	public void test11() {

		IonRoundMethod.setActive(IonRoundMethod.MINUS_09);
		assertEquals(18, AbstractIon.getIon(17.1d));
	}
}
