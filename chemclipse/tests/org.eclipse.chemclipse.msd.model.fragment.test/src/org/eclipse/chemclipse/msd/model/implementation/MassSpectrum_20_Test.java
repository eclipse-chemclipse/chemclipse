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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests getTotalIonSignal(IExcludedIons excludedIons)
 */
public class MassSpectrum_20_Test {

	private ScanMSD massSpectrum;
	private Ion ion;
	private IMarkedIons excludedIons;

	@Before
	public void setUp() {

		massSpectrum = new ScanMSD();
		ion = new Ion(45.5f, 78500.2f);
		massSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		massSpectrum.addIon(ion);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignal_2() {

		excludedIons.add(new MarkedIon(104));
		assertEquals("getTotalSignal", 1122021.5f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_3() {

		excludedIons.add(new MarkedIon(104));
		excludedIons.add(new MarkedIon(86));
		assertEquals("getTotalSignal", 1122021.5f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_4() {

		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_5() {

		assertEquals("getTotalSignal", 1242021.9f, massSpectrum.getTotalSignal(null), 0);
	}

	@Test
	public void testGetTotalIonSignal_6() {

		excludedIons.add(new MarkedIon(45));
		excludedIons.add(new MarkedIon(104));
		excludedIons.add(new MarkedIon(32));
		excludedIons.add(new MarkedIon(105));
		excludedIons.add(new MarkedIon(28));
		/*
		 * Why do we have a total ion signal here? Because the float ion value
		 * will be rounded Math.round() and so only 104.1 and 28.2 will be
		 * kicked.
		 */
		assertEquals("getTotalSignal", 1089021.0f, massSpectrum.getTotalSignal(excludedIons), 0);
	}
}
