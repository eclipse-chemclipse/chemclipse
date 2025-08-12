/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;
import org.junit.Before;
import org.junit.Test;

public class MassSpectrum_27_Test {

	private ScanMSD massSpectrum;

	@Before
	public void setUp() {

		massSpectrum = new ScanMSD();

		IIon nominalIon = new Ion(105.6d, 1500.25f);
		massSpectrum.addIon(nominalIon, false);

		IIonTransition ionTransition = new IonTransition(210.2d, 105.6d, 15.0d, 1.2d, 1.2d, 0);
		IIon tripleQuadIon = new Ion(105.6d, 2789.54f, ionTransition);
		massSpectrum.addIon(tripleQuadIon, false);
	}

	@Test
	public void test1() {

		assertEquals(2, massSpectrum.getNumberOfIons());
	}

	@Test
	public void test2() {

		assertEquals(4289.79f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void test3() {

		IIon ion = getIon(false);
		assertEquals(1500.25f, ion.getAbundance(), 0);
	}

	@Test
	public void test4() {

		IIon ion = getIon(true);
		assertEquals(2789.54f, ion.getAbundance(), 0);
	}

	@Test
	public void test5() {

		IIon nominalIon = new Ion(105.6d, 2000.2f);
		massSpectrum.addIon(false, nominalIon);

		IIon ion = getIon(false);
		assertEquals(2000.2f, ion.getAbundance(), 0);
	}

	@Test
	public void test6() {

		IIon nominalIon = new Ion(105.6d, 2000.2f);
		massSpectrum.addIon(true, nominalIon);

		IIon ion = getIon(false);
		assertEquals(3500.45f, ion.getAbundance(), 0);
	}

	@Test
	public void test7() throws NullPointerException {

		IIonTransition ionTransition = new IonTransition(210.2d, 105.6d, 15.0d, 1.2d, 1.2d, 0);
		IIon tripleQuadIon = new Ion(105.6d, 5000.0f, ionTransition);
		massSpectrum.addIon(false, tripleQuadIon);

		IIon ion = getIon(true);
		assertEquals(5000.0f, ion.getAbundance(), 0);
	}

	@Test
	public void test8() throws NullPointerException {

		IIonTransition ionTransition = new IonTransition(210.2d, 105.6d, 15.0d, 1.2d, 1.2d, 0);
		IIon tripleQuadIon = new Ion(105.6d, 5000.0f, ionTransition);
		massSpectrum.addIon(true, tripleQuadIon);

		IIon ion = getIon(true);
		assertEquals(7789.54f, ion.getAbundance(), 0);
	}

	@Test
	public void test9() {

		IIon nominalIon = new Ion(106.6d, 7800.2f);
		massSpectrum.addIon(false, nominalIon);

		assertEquals(3, massSpectrum.getNumberOfIons());
		assertEquals(12089.99f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void test10() {

		IIonTransition ionTransition = new IonTransition(210.2d, 106.6d, 15.0d, 1.2d, 1.2d, 0);
		IIon tripleQuadIon = new Ion(106.6d, 7800.2f, ionTransition);
		massSpectrum.addIon(false, tripleQuadIon);

		assertEquals(3, massSpectrum.getNumberOfIons());
		assertEquals(12089.99f, massSpectrum.getTotalSignal(), 0);
	}

	private IIon getIon(boolean useTransition) {

		IIon nominalIon = null;
		IIon tripleQuadIon = null;

		for(IIon ion : massSpectrum.getIons()) {
			if(ion.getIonTransition() != null) {
				tripleQuadIon = ion;
			} else {
				nominalIon = ion;
			}
		}

		if(useTransition) {
			return tripleQuadIon;
		} else {
			return nominalIon;
		}
	}
}
