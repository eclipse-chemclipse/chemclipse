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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.ISettingStatus;
import org.junit.Test;

public class SettingStatus_1_Test {

	private ISettingStatus settingStatus;

	@Test
	public void testStatus_1() {

		settingStatus = new SettingStatus(true, true);
		assertTrue("Report", settingStatus.report());
		assertTrue("SumOn", settingStatus.sumOn());
	}

	@Test
	public void testStatus_2() {

		settingStatus = new SettingStatus(true, false);
		assertTrue("Report", settingStatus.report());
		assertFalse("SumOn", settingStatus.sumOn());
	}

	@Test
	public void testStatus_3() {

		settingStatus = new SettingStatus(false, true);
		assertFalse("Report", settingStatus.report());
		assertTrue("SumOn", settingStatus.sumOn());
	}

	@Test
	public void testStatus_4() {

		settingStatus = new SettingStatus(false, false);
		assertFalse("Report", settingStatus.report());
		assertFalse("SumOn", settingStatus.sumOn());
	}
}
