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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Tests getTotalIonSignal(IExcludedIons excludedIons)
 */
@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrum_21_Test {

	private ScanMSD massSpectrum;
	private IMarkedTraces<ITrace> excludedIons;

	@BeforeAll
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
		excludedIons.add(new TraceNominalMSD(28));
		IScanMSD ms = massSpectrum.getMassSpectrum(excludedIons);
		assertEquals(1089021.0f, ms.getTotalSignal(), 0);
		assertEquals(3, ms.getNumberOfIons());
		assertEquals(78500.2f, ms.getIon(46).getAbundance(), 0);
		assertEquals(890520.4f, ms.getIon(33).getAbundance(), 0);
		assertEquals(120000.4f, ms.getIon(106).getAbundance(), 0);
	}
}
