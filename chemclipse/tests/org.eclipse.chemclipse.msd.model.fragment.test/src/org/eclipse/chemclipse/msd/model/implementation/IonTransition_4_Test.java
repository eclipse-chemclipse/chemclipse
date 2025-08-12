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

import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.junit.Before;
import org.junit.Test;

public class IonTransition_4_Test {

	private IIonTransition ionTransition1;
	private IIonTransition ionTransition2;

	@Before
	public void setUp() throws Exception {

		ionTransition1 = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.0, 1.2, 0);
		ionTransition2 = new IonTransition(120.2d, 121.3d, 88.5d, 87.4d, 7.0d, 1.2, 1.3, 0);
	}

	@Test
	public void testEquals_1() {

		assertFalse(ionTransition1.equals(ionTransition2));
	}

	@Test
	public void testEquals_2() {

		assertFalse(ionTransition2.equals(ionTransition1));
	}
}
