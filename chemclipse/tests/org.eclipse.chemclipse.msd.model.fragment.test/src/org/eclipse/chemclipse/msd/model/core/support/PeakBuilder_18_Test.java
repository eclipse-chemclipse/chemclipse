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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.equations.Equations;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_18_Test extends PeakBuilderTestCase {

	private IMarkedIons excludedIons;
	private ITotalScanSignals totalIonSignals;
	private ITotalScanSignal signal;
	private IPoint p1, p2;
	private LinearEquation backgroundEquation;
	private IPeakMassSpectrum peakMassSpectrum;
	private ITotalScanSignalExtractor totalIonSignalExtractor;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		totalIonSignalExtractor = new TotalScanSignalExtractor(chromatogram);
		totalIonSignals = totalIonSignalExtractor.getTotalScanSignals(2, 16);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		signal = totalIonSignals.getTotalScanSignal(2);
		p1 = new Point(signal.getRetentionTime(), signal.getTotalSignal());
		signal = totalIonSignals.getTotalScanSignal(16);
		p2 = new Point(signal.getRetentionTime(), signal.getTotalSignal());
		backgroundEquation = Equations.createLinearEquation(p1, p2);
		peakMassSpectrum = PeakBuilderMSD.getPeakMassSpectrum(chromatogram, totalIonSignals, backgroundEquation, excludedIons);
	}

	@Test
	public void testGetPeakMassSpectrum_1() {

		assertNotNull(peakMassSpectrum);
	}

	@Test
	public void testGetPeakMassSpectrum_2() {

		assertEquals("Ions", 11, peakMassSpectrum.getIons().size());
	}
}
