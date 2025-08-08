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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.support;

import org.junit.Test;

import junit.framework.TestCase;

public class Segment_2_Test extends TestCase {

	private ISegment segment = new Segment(null, null, null, null);

	@Test
	public void testPoint_1() {

		assertNotNull("ChromatogramBaselinePoint1", segment.getChromatogramBaselinePoint1());
	}

	@Test
	public void testPoint_2() {

		assertNotNull("ChromatogramBaselinePoint2", segment.getChromatogramBaselinePoint2());
	}

	@Test
	public void testPoint_3() {

		assertNotNull("PeakBaselinePoint1", segment.getPeakBaselinePoint1());
	}

	@Test
	public void testPoint_4() {

		assertNotNull("PeakBaselinePoint2", segment.getPeakBaselinePoint2());
	}
}
