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
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.junit.Test;

public class IonTransitionSettings_1_Test {

	private IIonTransitionSettings ionTransitionSettings = new IonTransitionSettings();

	@Test
	public void testGet_1() {

		assertEquals(0, ionTransitionSettings.size());
	}

	@Test
	public void testGet_2() {

		assertNull(ionTransitionSettings.get(0));
	}

	@Test
	public void testGet_3() {

		assertNotNull(ionTransitionSettings.getIonTransitionGroups());
	}
}
