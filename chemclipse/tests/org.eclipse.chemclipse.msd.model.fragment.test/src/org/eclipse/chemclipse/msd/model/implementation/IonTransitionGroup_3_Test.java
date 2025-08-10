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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionGroup;
import org.junit.Before;
import org.junit.Test;

public class IonTransitionGroup_3_Test {

	private IIonTransition ionTransition1;
	private IIonTransition ionTransition2;
	private IIonTransition ionTransition3;
	private IIonTransition ionTransition4;
	private IIonTransitionGroup ionTransitionGroup;

	@Before
	public void setUp() throws Exception {

		ionTransition1 = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
		ionTransition2 = new IonTransition(121.2d, 122.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
		ionTransition3 = new IonTransition(122.2d, 123.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
		ionTransition4 = new IonTransition(150.2d, 155.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);

		ionTransitionGroup = new IonTransitionGroup();
		ionTransitionGroup.add(ionTransition1);
		ionTransitionGroup.add(ionTransition2);
		ionTransitionGroup.add(ionTransition3);
		// do not add transition 4
	}

	@Test
	public void testContains_1() {

		assertTrue(ionTransitionGroup.contains(ionTransition1));
	}

	@Test
	public void testContains_2() {

		assertTrue(ionTransitionGroup.contains(ionTransition2));
	}

	@Test
	public void testContains_3() {

		assertTrue(ionTransitionGroup.contains(ionTransition3));
	}

	@Test
	public void testContains_4() {

		assertFalse(ionTransitionGroup.contains(ionTransition4));
	}

	@Test
	public void testContains_5() {

		assertNull(ionTransitionGroup.get(ionTransition4));
	}
}
