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
package org.eclipse.chemclipse.chromatogram.msd.peak.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.DetectorSlope;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.DetectorSlopes;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IDetectorSlope;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.IDetectorSlopes;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.core.Point;
import org.junit.Before;
import org.junit.Test;

public class DetectorSlopes_4_Test {

	private IDetectorSlope slope;
	private IDetectorSlopes slopes;
	private IPoint p1, p2;
	private int retentionTime;
	private List<Float> abundances;
	private ITotalScanSignals signals;

	@Before
	public void setUp() throws Exception {

		abundances = new ArrayList<Float>();
		abundances.add(21563.38028f);
		abundances.add(21718.30986f);
		abundances.add(21782.39437f);
		abundances.add(21623.94366f);
		abundances.add(21896.47887f);
		abundances.add(21348.59155f);
		abundances.add(22217.60563f);
		abundances.add(27317.60563f);
		abundances.add(45388.02817f);
		abundances.add(84380.98592f);
		abundances.add(127508.4507f);
		abundances.add(153907.7465f);
		abundances.add(153160.5634f);
		abundances.add(133292.9577f);
		abundances.add(109999.2958f);
		abundances.add(90078.16901f);
		abundances.add(75899.29577f);
		abundances.add(61307.04225f);
		abundances.add(50657.04225f);
		abundances.add(42513.38028f);
		abundances.add(37465.49296f);
		abundances.add(32107.74648f);
		abundances.add(29959.15493f);
		abundances.add(27964.78873f);
		abundances.add(26906.33803f);
		abundances.add(24441.5493f);
		abundances.add(23981.69014f);
		signals = EasyMock.createMock(ITotalScanSignals.class);
		EasyMock.expect(signals.getStartScan()).andStubReturn(30);
		EasyMock.expect(signals.getStopScan()).andStubReturn(56);
		EasyMock.replay(signals);
		slopes = new DetectorSlopes(signals);
		for(int i = 1; i < abundances.size(); i++) {
			retentionTime = i * 1000;
			p1 = new Point(retentionTime, abundances.get(i - 1));
			p2 = new Point((i + 1) * 1000, abundances.get(i));
			slope = new DetectorSlope(p1, p2, retentionTime);
			slopes.add(slope);
		}
	}

	@Test
	public void testSize_1() {

		assertEquals("Size", 26, slopes.size());
	}

	@Test
	public void testGetFirstDerivativeSlope_1() {

		slope = slopes.getDetectorSlope(30);
		assertEquals("scan 1 slope", 0.1549296875, slope.getSlope(), 0);
		assertEquals("scan 1 retention time", 1000, slope.getRetentionTime());
	}

	@Test
	public void testGetFirstDerivativeSlope_2() {

		slope = slopes.getDetectorSlope(55);
		assertEquals("scan 26 slope", -0.459859375, slope.getSlope(), 0);
		assertEquals("scan 26 retention time", 26000, slope.getRetentionTime());
	}

	@Test
	public void testGetFirstDerivativeSlope_3() {

		slope = slopes.getDetectorSlope(29);
		assertNull("Slope must be null.", slope);
	}

	@Test
	public void testGetFirstDerivativeSlope_4() {

		slope = slopes.getDetectorSlope(56);
		assertNull("Slope must be null.", slope);
	}
}
