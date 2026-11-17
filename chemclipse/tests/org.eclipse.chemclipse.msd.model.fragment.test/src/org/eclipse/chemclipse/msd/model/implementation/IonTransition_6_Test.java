/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.junit.jupiter.api.Test;

public class IonTransition_6_Test {

	@Test
	public void test_1() {

		IIonTransition ionTransition = new IonTransition(120.2d, 121.3d, 87.4d, 88.5d, 7.0d, 1.2, 1.3, 1);
		assertEquals(120.75d, ionTransition.getQ1Ion(), 1.0E-4);
		assertEquals(87.95d, ionTransition.getQ3Ion(), 1.0E-4);
	}

	@Test
	public void test_2() {

		IIonTransition ionTransition = new IonTransition(120.1d, 120.9d, 87.2d, 87.7d, 7.0d, 1.2, 1.3, 1);
		assertEquals(120.5d, ionTransition.getQ1Ion(), 1.0E-4);
		assertEquals(87.45d, ionTransition.getQ3Ion(), 1.0E-4);
	}

	@Test
	public void test_3() {

		IIonTransition ionTransition = new IonTransition(119.9d, 120.9d, 87.9d, 88.2d, 7.0d, 1.2, 1.3, 1);
		assertEquals(120.4d, ionTransition.getQ1Ion(), 1.0E-4);
		assertEquals(88.05d, ionTransition.getQ3Ion(), 1.0E-4);
	}
}
