/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.junit.Test;

public class ScanSupport_1_Test {

	@Test
	public void test1() {

		IIon ion = null;
		assertEquals("", ScanSupport.getLabelTandemMS(ion));
	}

	@Test
	public void test2() {

		assertEquals("58.0", ScanSupport.getLabelTandemMS(getIon(58.0d, null)));
	}

	@Test
	public void test3() {

		assertEquals("58.05", ScanSupport.getLabelTandemMS(getIon(58.05d, null)));
	}

	@Test
	public void test4() {

		IIon ion = getIon(58.1d, new IonTransition(168.7, 58.1, 15, 1.0d, 1.0d, 0));
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

	private IIon getIon(double mz, IIonTransition ionTransition) {

		try {
			if(ionTransition == null) {
				return new Ion(mz);
			} else {
				IIon ion = getIon(mz, null);
				return new Ion(ion, ionTransition);
			}
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
}