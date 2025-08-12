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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionGroup;
import org.junit.Before;
import org.junit.Test;

public class IonTransitionGroup_2_Test {

	private IIonTransition ionTransition1;
	private IIonTransition ionTransition2;
	private IIonTransition ionTransition3;
	private IIonTransitionGroup ionTransitionGroup;

	@Before
	public void setUp() throws Exception {

		ionTransition1 = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
		ionTransition2 = new IonTransition(121.2d, 122.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
		ionTransition3 = new IonTransition(122.2d, 123.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);

		ionTransitionGroup = new IonTransitionGroup();
		ionTransitionGroup.add(ionTransition1);
		ionTransitionGroup.add(ionTransition2);
		ionTransitionGroup.add(ionTransition3);
	}

	@Test
	public void testGet_1() {

		assertEquals(3, ionTransitionGroup.size());
	}

	@Test
	public void testGet_2() {

		assertNotNull(ionTransitionGroup.get(ionTransition1));
	}

	@Test
	public void testGet_3() {

		assertNotNull(ionTransitionGroup.getIonTransitions());
	}

	@Test
	public void testGet_4() {

		assertTrue(ionTransition1.equals(ionTransitionGroup.get(ionTransition1)));
	}

	@Test
	public void testGet_5() {

		assertTrue(ionTransition2.equals(ionTransitionGroup.get(ionTransition2)));
	}

	@Test
	public void testGet_6() {

		assertTrue(ionTransition3.equals(ionTransitionGroup.get(ionTransition3)));
	}

	@Test
	public void testGet_7() {

		assertTrue(ionTransition1.equals(ionTransitionGroup.get(0)));
	}

	@Test
	public void testGet_8() {

		assertTrue(ionTransition2.equals(ionTransitionGroup.get(1)));
	}

	@Test
	public void testGet_9() {

		assertTrue(ionTransition3.equals(ionTransitionGroup.get(2)));
	}
}
