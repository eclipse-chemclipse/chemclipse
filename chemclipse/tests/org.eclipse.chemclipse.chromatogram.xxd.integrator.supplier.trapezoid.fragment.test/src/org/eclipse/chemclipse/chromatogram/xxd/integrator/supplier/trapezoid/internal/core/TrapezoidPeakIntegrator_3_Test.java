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

import org.eclipse.chemclipse.chromatogram.xxd.integrator.result.IPeakIntegrationResult;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.processor.PeakIntegrator;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.settings.PeakIntegrationSettings;
import org.eclipse.chemclipse.model.support.IntegrationConstraint;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.util.MarkedTracesSupportMSD;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrapezoidPeakIntegrator_3_Test extends DefaultPeakTestCase {

	private PeakIntegrator integrator;
	private IPeakIntegrationResult result;
	private PeakIntegrationSettings peakIntegrationSettings;
	private String INTEGRATOR = "Trapezoid";

	@Override
	@BeforeEach
	public void setUp() {

		super.setUp();
		integrator = new PeakIntegrator();
		peakIntegrationSettings = new PeakIntegrationSettings();
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
	public void testIntegrate_1a() {

		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_1b() {

		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(531480.5905462648d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_2a() {

		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_2b() {

		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_3a() {

		/*
		 * Chromatogram baseline at an abundance level of 4000.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 4000.0f, 4000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_3b() {

		/*
		 * Chromatogram baseline at an abundance level of 4000.0f;
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 4000.0f, 4000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(85739.27804626466d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_4a() {

		/*
		 * Chromatogram baseline at an abundance level of 6991.0f. That's
		 * exactly the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 6991.0f, 6991.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_4b() {

		/*
		 * Chromatogram baseline at an abundance level of 6991.0f. That's
		 * exactly the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 6991.0f, 6991.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(0.0d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_5a() {

		/*
		 * Chromatogram baseline at an abundance level of 8000.0f. That's above
		 * the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 8000.0f, 8000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_5b() {

		/*
		 * Chromatogram baseline at an abundance level of 8000.0f. That's above
		 * the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 8000.0f, 8000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(0.0d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_6a() {

		/*
		 * Chromatogram baseline at an abundance level of 8000.0f. That's above
		 * the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 0.0f, 8000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_6b() {

		/*
		 * Chromatogram baseline at an abundance level of 8000.0f. That's above
		 * the total peak height (peak background and peak intensity).
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 0.0f, 8000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(83859.96051452635d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_7a() {

		/*
		 * Chromatogram baseline at an abundance level of 4000.0f but leave peak
		 * as it is.
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 4000.0f, 4000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		super.getPeak().getIntegrationConstraints().add(IntegrationConstraint.LEAVE_PEAK_AS_IT_IS);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_7b() {

		/*
		 * Chromatogram baseline at an abundance level of 4000.0f but leave peak
		 * as it is.
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 4000.0f, 4000.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		peakIntegrationSettings.setIncludeBackground(true);
		super.getPeak().getIntegrationConstraints().add(IntegrationConstraint.LEAVE_PEAK_AS_IT_IS);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_8a() {

		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(59181.76195989684d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_8b() {

		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(110238.28392748673d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_9a() {

		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(59181.76195989684d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_9b() {

		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(59181.76195989684d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_10a() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285327.0001125974d, result.getIntegratedArea(), 1);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_10b() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(531480.6083655402d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_11a() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285327.0001125974d, result.getIntegratedArea(), 1);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_11b() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285327.0001125974d, result.getIntegratedArea(), 1);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_12a() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(IIon.TIC_ION));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_12b() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(IIon.TIC_ION));
		/*
		 * No chromatogram baseline.
		 * > peakIntegrationSettings.setIncludeBackground(true);
		 */
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(531480.5905462648d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_13a() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(IIon.TIC_ION));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * > peakIntegrationSettings.setIncludeBackground(false);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(false);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}

	@Test
	public void testIntegrate_13() {

		/*
		 * Add all peak ions
		 */
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(104));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(103));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(51));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(50));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(78));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(77));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(74));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(105));
		peakIntegrationSettings.getMarkedTraces().add(new TraceNominalMSD(AbstractIon.getIon(IIon.TIC_ION)));
		/*
		 * Chromatogram baseline at an abundance level of 1760.0f;
		 * peakIntegrationSettings.setIncludeBackground(true);
		 */
		super.getChromatogram().getBaselineModel().addBaseline(500, 16500, 1760.0f, 1760.0f, true);
		peakIntegrationSettings.getBaselineSupport().setBaselineModel(super.getChromatogram().getBaselineModel());
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(500), 0);
		assertEquals(1760.0f, peakIntegrationSettings.getBaselineSupport().getBackgroundAbundance(16500), 0);
		peakIntegrationSettings.setIncludeBackground(true);
		result = integrator.integrate(super.getPeak(), peakIntegrationSettings);
		assertEquals(285326.9905462646d, result.getIntegratedArea(), 0);
		assertEquals(INTEGRATOR, result.getIntegratorType());
		assertTrue(result.getIntegratedTraces().containsAll(MarkedTracesSupportMSD.getTracesAsInteger(peakIntegrationSettings.getMarkedTraces())));
		String description = super.getPeak().getIntegratorDescription();
		assertEquals(INTEGRATOR, description);
	}
}
