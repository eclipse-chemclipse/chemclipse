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

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IIntegrationSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IntegrationSupport;
import org.junit.Test;

import junit.framework.TestCase;

public class IntegrationSupport_1_Test extends TestCase {

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
	public void testReset() {

		integrationSupport.reset();
		assertEquals(IIntegrationSupport.INITIAL_PEAK_WIDTH, integrationSupport.getMinimumPeakWidth());
		assertFalse(integrationSupport.isIntegratorOff(1500));
	}
}
