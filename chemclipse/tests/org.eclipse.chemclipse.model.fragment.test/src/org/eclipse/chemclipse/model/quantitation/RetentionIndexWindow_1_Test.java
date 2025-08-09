/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RetentionIndexWindow_1_Test {

	private RetentionIndexWindow retentionIndexWindow = new RetentionIndexWindow();

	@Test
	public void testGetAllowedNegativeDeviation_1() {

		assertEquals(0.0f, retentionIndexWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_2() {

		retentionIndexWindow.setAllowedNegativeDeviation(-3.4f);
		assertEquals(0.0f, retentionIndexWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_3() {

		retentionIndexWindow.setAllowedNegativeDeviation(0);
		assertEquals(0.0f, retentionIndexWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_4() {

		retentionIndexWindow.setAllowedNegativeDeviation(2.1f);
		assertEquals(2.1f, retentionIndexWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_1() {

		assertEquals(0.0f, retentionIndexWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_2() {

		retentionIndexWindow.setAllowedPositiveDeviation(5.2f);
		assertEquals(5.2f, retentionIndexWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_3() {

		retentionIndexWindow.setAllowedPositiveDeviation(0);
		assertEquals(0.0f, retentionIndexWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_4() {

		retentionIndexWindow.setAllowedPositiveDeviation(-2.2f);
		assertEquals(0.0f, retentionIndexWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetRetentionIndex_1() {

		assertEquals(0.0f, retentionIndexWindow.getRetentionIndex(), 0);
	}

	@Test
	public void testGetRetentionIndex_2() {

		retentionIndexWindow.setRetentionIndex(895.2f);
		assertEquals(895.2f, retentionIndexWindow.getRetentionIndex(), 0);
	}

	@Test
	public void testGetRetentionIndex_3() {

		retentionIndexWindow.setRetentionIndex(0);
		assertEquals(0.0f, retentionIndexWindow.getRetentionIndex(), 0);
	}

	@Test
	public void testGetRetentionIndex_4() {

		retentionIndexWindow.setRetentionIndex(-90);
		assertEquals(0.0f, retentionIndexWindow.getRetentionIndex(), 0);
	}
}
