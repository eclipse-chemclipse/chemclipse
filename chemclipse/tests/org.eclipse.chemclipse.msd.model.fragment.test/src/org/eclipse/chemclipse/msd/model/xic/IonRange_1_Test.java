/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the class IonRange.
 */
public class IonRange_1_Test {

	@Test
	public void testSetup_1() {

		IIonRange ionRange = new IonRange(1, 5);
		assertEquals("startIon", 1, ionRange.getStartIon());
		assertEquals("stopIon", 5, ionRange.getStopIon());
	}

	@Test
	public void testSetup_2() {

		IIonRange ionRange = new IonRange(5, 1);
		assertEquals("startIon", 1, ionRange.getStartIon());
		assertEquals("stopIon", 5, ionRange.getStopIon());
	}

	@Test
	public void testSetup_3() {

		IIonRange ionRange = new IonRange(0, 5);
		assertEquals("startIon", IIonRange.MIN_ION, ionRange.getStartIon());
		assertEquals("stopIon", 5, ionRange.getStopIon());
	}

	@Test
	public void testSetup_4() {

		IIonRange ionRange = new IonRange(1, 0);
		assertEquals("startIon", 1, ionRange.getStartIon());
		// Why MIN_Ion? Because 1 is greater than 0 so the values will be
		// swapped. 0 is now the startIon.
		assertEquals("stopIon", IIonRange.MIN_ION, ionRange.getStopIon());
	}

	@Test
	public void testSetup_5() {

		IIonRange ionRange = new IonRange(IIonRange.MAX_ION + 1, 5);
		assertEquals("startIon", 5, ionRange.getStartIon());
		// Why MAX_Ion? Because MAX_Ion+1 is greater than 5 so the values will be
		// swapped. 5 is now the startIon.
		assertEquals("stopIon", IIonRange.MAX_ION, ionRange.getStopIon());
	}

	@Test
	public void testSetup_6() {

		IIonRange ionRange = new IonRange(1, IIonRange.MAX_ION + 1);
		assertEquals("startIon", 1, ionRange.getStartIon());
		assertEquals("stopIon", IIonRange.MAX_ION, ionRange.getStopIon());
	}
}
