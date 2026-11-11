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
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakModelCSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationSupport_2_Test {

	private IIntegrationSupport integrationSupport;

	@BeforeAll
	public void setUp() {

		integrationSupport = new IntegrationSupport();
		integrationSupport.setMinimumPeakWidth(5000);
		integrationSupport.setIntegratorOff(1500, 2500);
		integrationSupport.setMinimumSignalToNoiseRatio(10.0f);
	}

	@Test
	public void testGetMinimumPeakWidth_1() {

		assertEquals(5000, integrationSupport.getMinimumPeakWidth());
	}

	@Test
	public void testIsIntegratorOff_1() {

		assertFalse(integrationSupport.isIntegratorOff(1499));
		assertTrue(integrationSupport.isIntegratorOff(1500));
		assertTrue(integrationSupport.isIntegratorOff(2500));
		assertFalse(integrationSupport.isIntegratorOff(2501));
	}

	@Test
	public void testReport_1() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(1499);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(5000);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		/*
		 * StartRetentionTime integrator on
		 */
		assertTrue(integrationSupport.report(peak));
	}

	@Test
	public void testReport_2() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(1500);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(5000);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		/*
		 * StartRetentionTime integrator off
		 */
		assertFalse(integrationSupport.report(peak));
	}

	@Test
	public void testReport_3() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(2500);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(5000);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		/*
		 * StartRetentionTime integrator off
		 */
		assertFalse(integrationSupport.report(peak));
	}

	@Test
	public void testReport_4() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(2501);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(5000);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		/*
		 * StartRetentionTime integrator on
		 */
		assertTrue(integrationSupport.report(peak));
	}

	@Test
	public void testReport_5() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(2501);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(4999);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		/*
		 * getWidthByInflectionPoints integrator off
		 */
		assertFalse(integrationSupport.report(peak));
	}

	@Test
	public void testReportMSDWithoutPeakModel() {

		assertFalse(integrationSupport.report(createChromatogramPeakMSD(null, 11.0f)));
	}

	@Test
	public void testReportMSDSignalToNoiseBelowMinimum() {

		assertFalse(integrationSupport.report(createChromatogramPeakMSD(createPeakModelMSD(3000, 5000), 9.9f)));
	}

	@Test
	public void testReportMSDSignalToNoiseAtMinimum() {

		assertTrue(integrationSupport.report(createChromatogramPeakMSD(createPeakModelMSD(3000, 5000), 10.0f)));
	}

	@Test
	public void testReportMSDWithoutSignalToNoise() {

		/*
		 * Only chromatogram peaks carry a S/N, so a plain MSD peak passes
		 * on peak width and retention time alone.
		 */
		IPeakMSD peak = EasyMock.createMock(IPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(createPeakModelMSD(3000, 5000));
		EasyMock.replay(peak);
		assertTrue(integrationSupport.report(peak));
	}

	@Test
	public void testReportCSD() {

		assertTrue(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(3000, 5000), 11.0f)));
	}

	@Test
	public void testReportCSDWithoutPeakModel() {

		assertFalse(integrationSupport.report(createChromatogramPeakCSD(null, 11.0f)));
	}

	@Test
	public void testReportCSDPeakWidthBelowMinimum() {

		assertFalse(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(3000, 4999), 11.0f)));
	}

	@Test
	public void testReportCSDIntegratorOff() {

		assertTrue(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(1499, 5000), 11.0f)));
		assertFalse(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(1500, 5000), 11.0f)));
		assertFalse(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(2500, 5000), 11.0f)));
		assertTrue(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(2501, 5000), 11.0f)));
	}

	@Test
	public void testReportCSDSignalToNoiseBelowMinimum() {

		assertFalse(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(3000, 5000), 9.9f)));
	}

	@Test
	public void testReportCSDSignalToNoiseAtMinimum() {

		assertTrue(integrationSupport.report(createChromatogramPeakCSD(createPeakModelCSD(3000, 5000), 10.0f)));
	}

	@Test
	public void testReportCSDWithoutSignalToNoise() {

		IPeakCSD peak = EasyMock.createMock(IPeakCSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(createPeakModelCSD(3000, 5000));
		EasyMock.replay(peak);
		assertTrue(integrationSupport.report(peak));
	}

	private static IPeakModelMSD createPeakModelMSD(int startRetentionTime, int width) {

		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(startRetentionTime);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(width);
		EasyMock.replay(peakModel);
		return peakModel;
	}

	private static IChromatogramPeakMSD createChromatogramPeakMSD(IPeakModelMSD peakModel, float signalToNoiseRatio) {

		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(signalToNoiseRatio);
		EasyMock.replay(peak);
		return peak;
	}

	private static IPeakModelCSD createPeakModelCSD(int startRetentionTime, int width) {

		IPeakModelCSD peakModel = EasyMock.createMock(IPeakModelCSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(startRetentionTime);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(width);
		EasyMock.replay(peakModel);
		return peakModel;
	}

	private static IChromatogramPeakCSD createChromatogramPeakCSD(IPeakModelCSD peakModel, float signalToNoiseRatio) {

		IChromatogramPeakCSD peak = EasyMock.createMock(IChromatogramPeakCSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(signalToNoiseRatio);
		EasyMock.replay(peak);
		return peak;
	}
}
