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

public class RetentionTimeWindow_1_Test {

	private RetentionTimeWindow retentionTimeWindow = new RetentionTimeWindow();

	@Test
	public void testGetAllowedNegativeDeviation_1() {

		assertEquals(0.0f, retentionTimeWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_2() {

		retentionTimeWindow.setAllowedNegativeDeviation(-3.4f);
		assertEquals(0.0f, retentionTimeWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_3() {

		retentionTimeWindow.setAllowedNegativeDeviation(0);
		assertEquals(0.0f, retentionTimeWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedNegativeDeviation_4() {

		retentionTimeWindow.setAllowedNegativeDeviation(2.1f);
		assertEquals(2.1f, retentionTimeWindow.getAllowedNegativeDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_1() {

		assertEquals(0.0f, retentionTimeWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_2() {

		retentionTimeWindow.setAllowedPositiveDeviation(5.2f);
		assertEquals(5.2f, retentionTimeWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_3() {

		retentionTimeWindow.setAllowedPositiveDeviation(0);
		assertEquals(0.0f, retentionTimeWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetAllowedPositiveDeviation_4() {

		retentionTimeWindow.setAllowedPositiveDeviation(-2.2f);
		assertEquals(0.0f, retentionTimeWindow.getAllowedPositiveDeviation(), 0);
	}

	@Test
	public void testGetRetentionTime_1() {

		assertEquals(0, retentionTimeWindow.getRetentionTime());
	}

	@Test
	public void testGetRetentionTime_2() {

		retentionTimeWindow.setRetentionTime(3490);
		assertEquals(3490, retentionTimeWindow.getRetentionTime());
	}

	@Test
	public void testGetRetentionTime_3() {

		retentionTimeWindow.setRetentionTime(0);
		assertEquals(0, retentionTimeWindow.getRetentionTime());
	}

	@Test
	public void testGetRetentionTime_4() {

		retentionTimeWindow.setRetentionTime(-90);
		assertEquals(0, retentionTimeWindow.getRetentionTime());
	}

	@Test
	public void testIsUseMilliseconds_1() {

		assertEquals(true, retentionTimeWindow.isUseMilliseconds());
	}

	@Test
	public void testIsUseMilliseconds_2() {

		retentionTimeWindow.setUseMilliseconds(false);
		assertEquals(false, retentionTimeWindow.isUseMilliseconds());
	}
}
