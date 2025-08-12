/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.support.util.TraceSettingUtil;
import org.junit.Test;

public class DenoisingFilterSettings_1_Test {

	private FilterSettings settings = new FilterSettings();

	@Test
	public void testGetIonsToRemove_1() {

		assertNotNull(settings.getIonsToRemove());
	}

	@Test
	public void testGetIonsToRemove_2() {

		TraceSettingUtil settingIon = new TraceSettingUtil();
		assertEquals("IonsToRemove Size", 4, new MarkedIons(settingIon.extractTraces(settingIon.deserialize(settings.getIonsToRemove())), MarkedTraceModus.INCLUDE).getIonsNominal().size());
	}

	@Test
	public void testGetIonsToPreserve_1() {

		assertNotNull(settings.getIonsToPreserve());
	}

	@Test
	public void testGetIonsToPreserve_2() {

		TraceSettingUtil settingIon = new TraceSettingUtil();
		assertEquals("IonsToPreserve Size", 2, new MarkedIons(settingIon.extractTraces(settingIon.deserialize(settings.getIonsToPreserve())), MarkedTraceModus.INCLUDE).getIonsNominal().size());
	}

	@Test
	public void testGetAdjustThresholdTransitions_1() {

		assertTrue(settings.isAdjustThresholdTransitions());
	}

	@Test
	public void testGetAdjustThresholdTransitions_2() {

		settings.setAdjustThresholdTransitions(false);
		assertFalse(settings.isAdjustThresholdTransitions());
	}

	@Test
	public void testGetNumberOfUsedIonsForCoefficient_1() {

		assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
	}

	@Test
	public void testGetNumberOfUsedIonsForCoefficient_2() {

		settings.setNumberOfUsedIonsForCoefficient(5);
		assertEquals("NumberOfUsedIonsForCoefficient", 5, settings.getNumberOfUsedIonsForCoefficient());
	}

	@Test
	public void testGetNumberOfUsedIonsForCoefficient_3() {

		settings.setNumberOfUsedIonsForCoefficient(0);
		assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
	}

	@Test
	public void testGetNumberOfUsedIonsForCoefficient_4() {

		settings.setNumberOfUsedIonsForCoefficient(-1);
		assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
	}
}
