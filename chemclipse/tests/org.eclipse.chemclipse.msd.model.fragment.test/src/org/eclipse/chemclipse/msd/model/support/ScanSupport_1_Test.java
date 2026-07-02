/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ScanSupport_1_Test {

	@Test
	public void test1() {

		IIonMSn ion = null;
		assertEquals("", ScanSupport.getLabelTandemMS(ion));
	}

	@Test
	public void test2() {

		assertEquals("58.0", ScanSupport.getLabelTandemMS(new Ion(58.0d)));
	}

	@Test
	public void test3() {

		assertEquals("58.05", ScanSupport.getLabelTandemMS(new Ion(58.05d)));
	}

	@Test
	public void test4() {

		IIonMSn ion = new IonMSn(new Ion(58.1d), new IonTransition(168.7, 58.1, 15, 1.0d, 1.0d, 0));
		assertEquals("169 > 58.1 @15", ScanSupport.getLabelTandemMS(ion));
	}

	@Test
	public void test5() {

		IIonTransition ionTransition = null;
		assertEquals("", ScanSupport.getLabelTandemMS(ionTransition));
	}

	@Test
	public void test6() {

		IIonTransition ionTransition = new IonTransition(168.7, 58.1, 15, 1.0d, 1.0d, 0);
		assertEquals("169 > 58.1 @15", ScanSupport.getLabelTandemMS(ionTransition));
	}

	@Test
	public void test7() {

		IIonTransition ionTransition = new IonTransition(168.7, 169.7, 56.3, 57.3, 15, 1.0d, 1.0d, 0);
		assertEquals("169 > 56.8 @15", ScanSupport.getLabelTandemMS(ionTransition));
	}
}