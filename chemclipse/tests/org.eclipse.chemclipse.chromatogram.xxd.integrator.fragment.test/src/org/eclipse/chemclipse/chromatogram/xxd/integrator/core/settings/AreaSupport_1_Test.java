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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.AreaSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IAreaSupport;
import org.junit.Test;

public class AreaSupport_1_Test {

	private IAreaSupport areaSupport = new AreaSupport();

	@Test
	public void testGetMinimumArea_1() {

		assertEquals(0.0d, areaSupport.getMinimumArea(), 0);
	}

	@Test
	public void testIsAreaSumOn_1() {

		assertFalse(areaSupport.isAreaSumOn(1500));
	}

	@Test
	public void testReport_1() {

		assertFalse(areaSupport.report(null));
	}

	@Test
	public void testReset_1() {

		areaSupport.reset();
		assertEquals(IAreaSupport.INITIAL_AREA_REJECT, areaSupport.getMinimumArea(), 0);
		assertFalse(areaSupport.isAreaSumOn(1500));
	}

	@Test
	public void testResetAreaSumOn_1() {

		areaSupport.resetAreaSumOn();
		assertFalse(areaSupport.isAreaSumOn(1500));
	}

	@Test
	public void testSetAreaSumOn_1() {

		areaSupport.setAreaSumOn(1500, 2500);
		assertFalse(areaSupport.isAreaSumOn(1499));
		assertTrue(areaSupport.isAreaSumOn(1500));
		assertTrue(areaSupport.isAreaSumOn(2500));
		assertFalse(areaSupport.isAreaSumOn(2501));
	}

	@Test
	public void testSetMinimumArea_1() {

		areaSupport.setMinimumArea(1522);
		assertEquals(1522.0d, areaSupport.getMinimumArea(), 0);
	}
}
