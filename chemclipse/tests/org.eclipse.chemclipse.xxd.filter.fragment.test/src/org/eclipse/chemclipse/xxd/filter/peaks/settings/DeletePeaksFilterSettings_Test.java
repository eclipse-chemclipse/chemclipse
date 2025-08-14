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

public class DeletePeaksFilterSettings_Test {

	private DeletePeaksFilterSettings settings = new DeletePeaksFilterSettings();

	@Test
	public void test0() {

		settings.transferToLatestVersion("{\"Delete Peaks\":true}");
		assertEquals(DeletePeakOption.ALL, settings.getDeletePeakOption());
	}

	@Test
	public void test1() {

		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test2() {

		settings.transferToLatestVersion(null);
		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test3() {

		settings.transferToLatestVersion("");
		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test4() {

		settings.transferToLatestVersion(" ");
		assertEquals(DeletePeakOption.NONE, settings.getDeletePeakOption());
	}

	@Test
	public void test5() {

		settings.setDeletePeakOption(DeletePeakOption.INACTIVE);
		settings.transferToLatestVersion(null);
		assertEquals(DeletePeakOption.INACTIVE, settings.getDeletePeakOption());
	}

	@Test
	public void test6() {

		settings.setDeletePeakOption(DeletePeakOption.INACTIVE);
		settings.transferToLatestVersion("");
		assertEquals(DeletePeakOption.INACTIVE, settings.getDeletePeakOption());
	}

	@Test
	public void test7() {

		settings.setDeletePeakOption(DeletePeakOption.INACTIVE);
		settings.transferToLatestVersion(" ");
		assertEquals(DeletePeakOption.INACTIVE, settings.getDeletePeakOption());
	}

	@Test
	public void test8() {

		settings.setDeletePeakOption(DeletePeakOption.INACTIVE);
		settings.transferToLatestVersion("{\"Delete Peakz\":false}"); // Can't match!
		assertEquals(DeletePeakOption.INACTIVE, settings.getDeletePeakOption());
	}
}