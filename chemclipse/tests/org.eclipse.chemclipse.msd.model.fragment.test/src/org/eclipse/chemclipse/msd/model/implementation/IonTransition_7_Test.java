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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.junit.Test;

public class IonTransition_7_Test {

	@Test
	public void test_1() {

		IIonTransition ionTransition = new IonTransition(120.2d, 121.3d, 87.4d, 88.5d, 7.0d, 1.2, 1.3, 1);
		assertEquals(1.09d, ionTransition.getDeltaQ1Ion(), 0.01d);
		assertEquals(1.09d, ionTransition.getDeltaQ3Ion(), 0.01d);
	}

	@Test
	public void test_2() {

		IIonTransition ionTransition = new IonTransition(120.1d, 120.9d, 87.2d, 87.7d, 7.0d, 1.2, 1.3, 1);
		assertEquals(0.8d, ionTransition.getDeltaQ1Ion(), 0.01d);
		assertEquals(0.5d, ionTransition.getDeltaQ3Ion(), 0.01d);
	}

	@Test
	public void test_3() {

		IIonTransition ionTransition = new IonTransition(119.9d, 120.9d, 87.9d, 88.2d, 7.0d, 1.2, 1.3, 1);
		assertEquals(1.0d, ionTransition.getDeltaQ1Ion(), 0.01d);
		assertEquals(0.29d, ionTransition.getDeltaQ3Ion(), 0.01d);
	}

	@Test
	public void test_4() {

		IIonTransition ionTransition = new IonTransition(119.9d, 119.9d, 87.9d, 87.9d, 7.0d, 1.2, 1.3, 1);
		assertEquals(0.0d, ionTransition.getDeltaQ1Ion(), 0);
		assertEquals(0.0d, ionTransition.getDeltaQ3Ion(), 0);
	}

	@Test
	public void test_5() {

		IIonTransition ionTransition = new IonTransition(0.0d, 0.0d, 0.0d, 0.0d, 7.0d, 1.2, 1.3, 1);
		assertEquals(0.0d, ionTransition.getDeltaQ1Ion(), 0);
		assertEquals(0.0d, ionTransition.getDeltaQ3Ion(), 0);
	}
}
