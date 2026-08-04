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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.internal.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResult;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResults;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.settings.PeakIntegrationSettings;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrapezoidPeakIntegrator_4_Test extends DefaultPeakTestCase {

	private PeakIntegrator integrator;
	private IPeakIntegrationResult result;
	private IPeakIntegrationResults results;
	private PeakIntegrationSettings peakIntegrationSettings;
	private List<IPeakMSD> peaks;

	@Override
	@BeforeEach
	public void setUp() {

		super.setUp();
		integrator = new PeakIntegrator();
		peakIntegrationSettings = new PeakIntegrationSettings();
		peaks = new ArrayList<>();
		for(int i = 1; i <= 100; i++) {
			peaks.add(super.getPeak());
		}
	}

	@Test
	public void testPeak_1() {

		IPeakMassSpectrum ms = super.getPeak().getPeakModel().getPeakMassSpectrum();
		assertEquals(5231.0f, ms.getTotalSignal(), 0);
	}

	@Test
	public void testPeak_2() {

		float abundance = super.getPeak().getPeakModel().getBackgroundAbundance(1500);
		assertEquals(1760.0f, abundance, 0, "Background");
	}

	@Test
	public void testPeak_3() {

		float abundance = super.getPeak().getPeakModel().getBackgroundAbundance(15500);
		assertEquals(1760.0f, abundance, 0, "Background");
	}

	@Test
	public void testPeakList_1() {

		assertEquals(100, peaks.size());
	}

	@Test
	public void testIntegrate_1() {

		// No chromatogram baseline.
		results = integrator.integrate(peaks, peakIntegrationSettings, new NullProgressMonitor());
		assertEquals(2.85326990546264E7d, results.getTotalPeakArea(), 0, "Total Integrated Peak Area");
	}

	@Test
	public void testIntegrate_2() {

		// No chromatogram baseline.
		peakIntegrationSettings.setIncludeBackground(true);
		results = integrator.integrate(peaks, peakIntegrationSettings, new NullProgressMonitor());
		assertEquals(5.314805905462652E7d, results.getTotalPeakArea(), 0, "Total Integrated Peak Area");
	}

	@Test
	public void testIntegrate_3() {

		// No chromatogram baseline.
		results = integrator.integrate(peaks, peakIntegrationSettings, new NullProgressMonitor());
		result = results.getPeakIntegrationResult(0);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0, "Integrated Peak Area");
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
	}

	@Test
	public void testIntegrate_4() {

		// No chromatogram baseline.
		peakIntegrationSettings.setIncludeBackground(true);
		results = integrator.integrate(peaks, peakIntegrationSettings, new NullProgressMonitor());
		result = results.getPeakIntegrationResult(0);
		assertEquals(531480.5905462648d, result.getIntegratedArea(), 0, "Integrated Peak Area");
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
	}
}