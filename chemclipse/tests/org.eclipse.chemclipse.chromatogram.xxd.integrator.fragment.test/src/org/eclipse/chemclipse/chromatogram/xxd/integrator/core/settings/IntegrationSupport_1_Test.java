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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IIntegrationSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IntegrationSupport;
import org.eclipse.chemclipse.model.core.IPeak;
import org.junit.jupiter.api.Test;

public class IntegrationSupport_1_Test {

	private IIntegrationSupport integrationSupport = new IntegrationSupport();

	@Test
	public void testGetMinimumPeakWidth_1() {

		assertEquals(IIntegrationSupport.INITIAL_PEAK_WIDTH, integrationSupport.getMinimumPeakWidth());
	}

	@Test
	public void testGetMinimumPeakWidth_2() {

		integrationSupport.setMinimumPeakWidth(9600);
		assertEquals(9600, integrationSupport.getMinimumPeakWidth());
	}

	@Test
	public void testIsIntegratorOff_1() {

		assertFalse(integrationSupport.isIntegratorOff(1500));
	}

	@Test
	public void testIsIntegratorOff_2() {

		integrationSupport.setIntegratorOff(1500, 2500);
		assertFalse(integrationSupport.isIntegratorOff(1499));
		assertTrue(integrationSupport.isIntegratorOff(1500));
		assertTrue(integrationSupport.isIntegratorOff(2500));
		assertFalse(integrationSupport.isIntegratorOff(2501));
	}

	@Test
	public void testIsIntegratorOff_3() {

		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.resetIntegratorOff();
		assertFalse(integrationSupport.isIntegratorOff(1499));
		assertFalse(integrationSupport.isIntegratorOff(1500));
		assertFalse(integrationSupport.isIntegratorOff(2500));
		assertFalse(integrationSupport.isIntegratorOff(2501));
	}

	@Test
	public void testReport_1() {

		assertFalse(integrationSupport.report(null));
	}

	@Test
	public void testReport_2() {

		/*
		 * A peak that is neither MSD nor CSD has no peak model or S/N to check.
		 * The strict mock fails if report() calls any method on it.
		 */
		IPeak peak = EasyMock.createMock(IPeak.class);
		EasyMock.replay(peak);
		assertTrue(integrationSupport.report(peak));
		EasyMock.verify(peak);
	}

	@Test
	public void testSetMinimumPeakWidthZero() {

		integrationSupport.setMinimumPeakWidth(9600);
		integrationSupport.setMinimumPeakWidth(0);
		assertEquals(0, integrationSupport.getMinimumPeakWidth());
	}

	@Test
	public void testSetMinimumPeakWidthRejectsNegative() {

		integrationSupport.setMinimumPeakWidth(9600);
		integrationSupport.setMinimumPeakWidth(-1);
		assertEquals(9600, integrationSupport.getMinimumPeakWidth());
	}

	@Test
	public void testGetMinimumSignalToNoiseRatio() {

		assertEquals(IIntegrationSupport.MIN_SIGNAL_TO_NOISE_RATIO, integrationSupport.getMinimumSignalToNoiseRatio());
	}

	@Test
	public void testSetMinimumSignalToNoiseRatio() {

		integrationSupport.setMinimumSignalToNoiseRatio(12.5f);
		assertEquals(12.5f, integrationSupport.getMinimumSignalToNoiseRatio());
	}

	@Test
	public void testSetMinimumSignalToNoiseRatioZero() {

		integrationSupport.setMinimumSignalToNoiseRatio(12.5f);
		integrationSupport.setMinimumSignalToNoiseRatio(0.0f);
		assertEquals(0.0f, integrationSupport.getMinimumSignalToNoiseRatio());
	}

	@Test
	public void testSetMinimumSignalToNoiseRatioRejectsNegative() {

		integrationSupport.setMinimumSignalToNoiseRatio(12.5f);
		integrationSupport.setMinimumSignalToNoiseRatio(-0.1f);
		assertEquals(12.5f, integrationSupport.getMinimumSignalToNoiseRatio());
	}

	@Test
	public void testIsIntegratorOffMultipleRanges() {

		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.setIntegratorOff(5000, 6000);
		assertTrue(integrationSupport.isIntegratorOff(2000));
		assertFalse(integrationSupport.isIntegratorOff(3000));
		assertTrue(integrationSupport.isIntegratorOff(5000));
		assertTrue(integrationSupport.isIntegratorOff(6000));
		assertFalse(integrationSupport.isIntegratorOff(6001));
	}

	@Test
	public void testIsIntegratorOffOverlappingRanges() {

		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.setIntegratorOff(2000, 3000);
		assertTrue(integrationSupport.isIntegratorOff(1500));
		assertTrue(integrationSupport.isIntegratorOff(2250));
		assertTrue(integrationSupport.isIntegratorOff(3000));
		assertFalse(integrationSupport.isIntegratorOff(3001));
	}

	@Test
	public void testIsIntegratorOffReversedRange() {

		integrationSupport.setIntegratorOff(2500, 1500);
		assertFalse(integrationSupport.isIntegratorOff(1499));
		assertTrue(integrationSupport.isIntegratorOff(1500));
		assertTrue(integrationSupport.isIntegratorOff(2500));
		assertFalse(integrationSupport.isIntegratorOff(2501));
	}

	@Test
	public void testIsIntegratorOffSingleRetentionTime() {

		integrationSupport.setIntegratorOff(1500, 1500);
		assertFalse(integrationSupport.isIntegratorOff(1499));
		assertTrue(integrationSupport.isIntegratorOff(1500));
		assertFalse(integrationSupport.isIntegratorOff(1501));
	}

	@Test
	public void testResetIntegratorOffKeepsThresholds() {

		integrationSupport.setMinimumPeakWidth(9600);
		integrationSupport.setMinimumSignalToNoiseRatio(12.5f);
		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.resetIntegratorOff();
		assertFalse(integrationSupport.isIntegratorOff(2000));
		assertEquals(9600, integrationSupport.getMinimumPeakWidth());
		assertEquals(12.5f, integrationSupport.getMinimumSignalToNoiseRatio());
	}

	@Test
	public void testReset() {

		integrationSupport.setMinimumPeakWidth(9600);
		integrationSupport.setMinimumSignalToNoiseRatio(12.5f);
		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.reset();
		assertEquals(IIntegrationSupport.INITIAL_PEAK_WIDTH, integrationSupport.getMinimumPeakWidth());
		assertEquals(IIntegrationSupport.MIN_SIGNAL_TO_NOISE_RATIO, integrationSupport.getMinimumSignalToNoiseRatio());
		assertFalse(integrationSupport.isIntegratorOff(1500));
	}
}
