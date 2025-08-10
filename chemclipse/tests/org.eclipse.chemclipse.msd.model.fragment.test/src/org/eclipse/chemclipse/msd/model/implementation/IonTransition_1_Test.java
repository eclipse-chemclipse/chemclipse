/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
import org.junit.Before;
import org.junit.Test;

public class IonTransition_1_Test {

	private IIonTransition ionTransition;

	@Before
	public void setUp() throws Exception {

		ionTransition = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
	}

	@Test
	public void testGetFilter1FirstIon_1() {

		assertEquals(120.2d, ionTransition.getQ1StartIon(), 0);
	}

	@Test
	public void testGetFilter1LastIon_1() {

		assertEquals(121.3d, ionTransition.getQ1StopIon(), 0);
	}

	@Test
	public void testGetFilter3FirstIon_1() {

		assertEquals(88.5d, ionTransition.getQ3StartIon(), 0);
	}

	@Test
	public void testGetFilter3LastIon_1() {

		assertEquals(87.4d, ionTransition.getQ3StopIon(), 0);
	}

	@Test
	public void testGetCollisionEnergy_1() {

		assertEquals(7.0d, ionTransition.getCollisionEnergy(), 0);
	}

	@Test
	public void testGetTransitionGroup_1() {

		assertEquals(1, ionTransition.getTransitionGroup());
	}
}
