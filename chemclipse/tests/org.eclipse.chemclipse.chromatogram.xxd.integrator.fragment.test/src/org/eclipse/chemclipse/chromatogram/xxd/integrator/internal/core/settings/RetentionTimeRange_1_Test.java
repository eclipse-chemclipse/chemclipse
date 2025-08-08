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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.internal.core.settings;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.support.RetentionTimeRange;
import org.junit.Test;

public class RetentionTimeRange_1_Test {

	@Test
	public void testGetRetentionTime_1() {

		RetentionTimeRange retentionTimeRange = new RetentionTimeRange(0, 0);
		assertEquals("Start", 0, retentionTimeRange.getStartRetentionTime());
		assertEquals("Stop", 0, retentionTimeRange.getStopRetentionTime());
	}

	@Test
	public void testGetRetentionTime_2() {

		RetentionTimeRange retentionTimeRange = new RetentionTimeRange(1500, 2500);
		assertEquals("Start", 1500, retentionTimeRange.getStartRetentionTime());
		assertEquals("Stop", 2500, retentionTimeRange.getStopRetentionTime());
	}

	@Test
	public void testGetRetentionTime_3() {

		RetentionTimeRange retentionTimeRange = new RetentionTimeRange(-1, 2500);
		assertEquals("Start", 0, retentionTimeRange.getStartRetentionTime());
		assertEquals("Stop", 2500, retentionTimeRange.getStopRetentionTime());
	}

	@Test
	public void testGetRetentionTime_4() {

		RetentionTimeRange retentionTimeRange = new RetentionTimeRange(1500, -1);
		assertEquals("Start", 0, retentionTimeRange.getStartRetentionTime());
		assertEquals("Stop", 1500, retentionTimeRange.getStopRetentionTime());
	}

	@Test
	public void testGetRetentionTime_5() {

		RetentionTimeRange retentionTimeRange = new RetentionTimeRange(2500, 1500);
		assertEquals("Start", 1500, retentionTimeRange.getStartRetentionTime());
		assertEquals("Stop", 2500, retentionTimeRange.getStopRetentionTime());
	}
}
