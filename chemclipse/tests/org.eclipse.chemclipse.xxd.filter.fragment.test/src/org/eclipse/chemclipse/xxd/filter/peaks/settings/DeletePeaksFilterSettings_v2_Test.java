/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.peaks.settings;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.xxd.filter.support.DeletePeakOption;
import org.junit.Test;

public class DeletePeaksFilterSettings_v2_Test {

	private DeletePeaksFilterSettings settings = new DeletePeaksFilterSettings();

	@Test
	public void test0() {

		settings.transferToLatestVersion("{\"Delete Peaks\":true}");
		assertEquals(DeletePeakOption.ALL, settings.getDeletePeakOption());
	}

	@Test
	public void test1() {

		settings.transferToLatestVersion("{\"Delete Peaks\":false,\"Unidentified Only\":false,\"Inactive Only\":false}");
		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test2() {

		settings.transferToLatestVersion("{\"Delete Peaks\":false,\"Unidentified Only\":true,\"Inactive Only\":false}");
		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test3() {

		settings.transferToLatestVersion("{\"Delete Peaks\":true,\"Unidentified Only\":false,\"Inactive Only\":false}");
		assertEquals(DeletePeakOption.ALL, settings.getDeletePeakOption());
	}

	@Test
	public void test4() {

		settings.transferToLatestVersion("{\"Delete Peaks\":true,\"Unidentified Only\":true,\"Inactive Only\":false}");
		assertEquals(DeletePeakOption.UNIDENTIFIED, settings.getDeletePeakOption());
	}

	@Test
	public void test5() {

		settings.transferToLatestVersion("{\"Delete Peaks\":true,\"Unidentified Only\":false,\"Inactive Only\":true}");
		assertEquals(DeletePeakOption.INACTIVE, settings.getDeletePeakOption());
	}
}