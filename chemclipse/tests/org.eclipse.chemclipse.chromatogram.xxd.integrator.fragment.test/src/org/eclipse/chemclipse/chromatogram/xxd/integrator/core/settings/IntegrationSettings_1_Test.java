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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IPeakIntegrationSettings;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.junit.Before;
import org.junit.Test;

public class IntegrationSettings_1_Test {

	private IPeakIntegrationSettings settings;

	@Before
	public void setUp() throws Exception {

		settings = new PeakIntegrationSettings();
		// settings.addReportDecider(null);
		// settings.removeReportDecider(null);
	}

	@Test
	public void testGetAreaSupport_1() {

		assertNotNull(settings.getAreaSupport());
	}

	@Test
	public void testGetBaselineSupport_1() {

		assertNotNull(settings.getBaselineSupport());
	}

	@Test
	public void testGetIntegrationSupport_1() {

		assertNotNull(settings.getIntegrationSupport());
	}

	@Test
	public void testGetSelectedIons_1() {

		assertNotNull(settings.getMarkedTraces());
	}

	@Test
	public void testGetSettingStatus_1() {

		/*
		 * Peak model and peak mocks.
		 */
		IPeakModelMSD peakModel = EasyMock.createMock(IPeakModelMSD.class);
		EasyMock.expect(peakModel.getStartRetentionTime()).andStubReturn(4500);
		EasyMock.expect(peakModel.getWidthByInflectionPoints()).andStubReturn(1500);
		EasyMock.replay(peakModel);
		IChromatogramPeakMSD peak = EasyMock.createMock(IChromatogramPeakMSD.class);
		EasyMock.expect(peak.getPeakModel()).andStubReturn(peakModel);
		EasyMock.expect(peak.getIntegratedArea()).andStubReturn(4500.0d);
		EasyMock.expect(peak.getSignalToNoiseRatio()).andStubReturn(11.0f);
		EasyMock.replay(peak);
		assertNotNull(settings.getSettingStatus(peak));
	}
}
