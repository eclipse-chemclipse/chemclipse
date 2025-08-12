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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionGroup;
import org.junit.Before;
import org.junit.Test;

public class IonTransitionGroup_1_Test {

	private IIonTransitionGroup ionTransitionGroup;
	private IIonTransition ionTransition;

	@Before
	public void setUp() throws Exception {

		ionTransitionGroup = new IonTransitionGroup();
		ionTransition = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 1);
	}

	@Test
	public void testGet_1() {

		assertEquals(0, ionTransitionGroup.size());
	}

	@Test
	public void testGet_2() {

		assertNull(ionTransitionGroup.get(0));
	}

	@Test
	public void testGet_3() {

		assertNull(ionTransitionGroup.get(ionTransition));
	}

	@Test
	public void testGet_4() {

		assertFalse(ionTransitionGroup.contains(ionTransition));
	}

	@Test
	public void testGet_5() {

		assertNotNull(ionTransitionGroup.getIonTransitions());
	}
}
