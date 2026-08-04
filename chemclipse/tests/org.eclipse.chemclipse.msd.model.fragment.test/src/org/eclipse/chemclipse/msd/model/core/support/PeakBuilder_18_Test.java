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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.equations.Equations;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test the peak exceptions.
 */
@TestInstance(Lifecycle.PER_CLASS)
public class PeakBuilder_18_Test extends PeakBuilderTestCase {

	private IPeakMassSpectrum peakMassSpectrum;

	@Override
	@BeforeAll
	public void setUp() {

		super.setUp();
		ITotalScanSignalExtractor totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		ITotalScanSignals totalIonSignals = totalIonSignalExtractor.getTotalScanSignals(2, 16);
		IMarkedTraces<ITrace> excludedIons = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ITotalScanSignal signal = totalIonSignals.getTotalScanSignal(2);
		IPoint p1 = new Point(signal.getRetentionTime(), signal.getTotalSignal());
		signal = totalIonSignals.getTotalScanSignal(16);
		IPoint p2 = new Point(signal.getRetentionTime(), signal.getTotalSignal());
		LinearEquation backgroundEquation = Equations.createLinearEquation(p1, p2);
		peakMassSpectrum = PeakBuilderMSD.getPeakMassSpectrum(chromatogram, totalIonSignals, backgroundEquation, excludedIons);
	}

	@Test
	public void testGetPeakMassSpectrum_1() {

		assertNotNull(peakMassSpectrum);
	}

	@Test
	public void testGetPeakMassSpectrum_2() {

		assertEquals(11, peakMassSpectrum.getIons().size());
	}
}