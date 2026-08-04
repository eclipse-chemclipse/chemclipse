/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests getTotalIonSignal(IExcludedIons excludedIons)
 */
public class MassSpectrum_20_Test {

	private IScanMSD massSpectrum;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeEach
	public void setUp() {

		massSpectrum = new ScanMSD();
		IIon ion = new Ion(45.5f, 78500.2f);
		massSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		massSpectrum.addIon(ion);
		excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
	}

	@Test
	public void testGetTotalSignal_1() {

		assertEquals(1242021.9f, massSpectrum.getTotalSignal(), 0);
	}

	@Test
	public void testGetTotalIonSignal_2() {

		excludedIons.add(new TraceNominalMSD(104));
		assertEquals(1122021.5f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_3() {

		excludedIons.add(new TraceNominalMSD(104));
		excludedIons.add(new TraceNominalMSD(86));
		assertEquals(1122021.5f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_4() {

		assertEquals(1242021.9f, massSpectrum.getTotalSignal(excludedIons), 0);
	}

	@Test
	public void testGetTotalIonSignal_5() {

		assertEquals(1242021.9f, massSpectrum.getTotalSignal(null), 0);
	}

	@Test
	public void testGetTotalIonSignal_6() {

		excludedIons.add(new TraceNominalMSD(45));
		excludedIons.add(new TraceNominalMSD(104));
		excludedIons.add(new TraceNominalMSD(32));
		excludedIons.add(new TraceNominalMSD(105));
		excludedIons.add(new TraceNominalMSD(28));
		/*
		 * Why do we have a total ion signal here? Because the float ion value
		 * will be rounded Math.round() and so only 104.1 and 28.2 will be
		 * kicked.
		 */
		assertEquals(1089021.0f, massSpectrum.getTotalSignal(excludedIons), 0);
	}
}
