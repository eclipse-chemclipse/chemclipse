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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PeakIntegrationResults_2_Test {

	private IPeakIntegrationResults results;
	private IPeakIntegrationResult result;

	@Before
	public void setUp() throws Exception {

		results = new PeakIntegrationResults();

		result = new PeakIntegrationResult();
		result.addIntegratedTrace(55);
		result.setIntegratedArea(593992.4d);
		results.add(result);

		result = new PeakIntegrationResult();
		result.addIntegratedTrace(48);
		result.setIntegratedArea(474.4d);
		results.add(result);

		result = new PeakIntegrationResult();
		result.addIntegratedTrace(78);
		result.setIntegratedArea(2393.4d);
		results.add(result);

		result = new PeakIntegrationResult();
		result.addIntegratedTrace(55);
		result.addIntegratedTrace(78);
		result.setIntegratedArea(942224.4d);
		results.add(result);
	}

	@Test
	public void testGetPeakIntegrationResult_1() {

		result = results.getPeakIntegrationResult(0);
		assertNotNull(result);
		assertEquals("Area", 593992.4d, result.getIntegratedArea(), 0);
	}

	@Test
	public void testGetPeakIntegrationResult_2() {

		result = results.getPeakIntegrationResult(3);
		assertNotNull(result);
		assertEquals("Area", 942224.4d, result.getIntegratedArea(), 0);
	}

	@Test
	public void testGetPeakIntegrationResult_3() {

		result = results.getPeakIntegrationResult(-1);
		assertNull(result);
	}

	@Test
	public void testGetPeakIntegrationResult_4() {

		result = results.getPeakIntegrationResult(4);
		assertNull(result);
	}

	@Test
	public void testGetPeakIntegrationResultList_1() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultList(55);
		assertEquals("size", 1, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 593992.4d, area, 0);
	}

	@Test
	public void testGetPeakIntegrationResultList_2() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultList(48);
		assertEquals("size", 1, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 474.4d, area, 0);
	}

	@Test
	public void testGetPeakIntegrationResultList_3() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultList(78);
		assertEquals("size", 1, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 2393.4d, area, 0);
	}

	@Test
	public void testGetPeakIntegrationResultListThatContains_1() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultThatContains(55);
		assertEquals("size", 2, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 593992.4d + 942224.4d, area, 0);
	}

	@Test
	public void testGetPeakIntegrationResultListThatContains_2() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultThatContains(48);
		assertEquals("size", 1, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 474.4d, area, 0);
	}

	@Test
	public void testGetPeakIntegrationResultListThatContains_3() {

		List<IPeakIntegrationResult> presults = results.getPeakIntegrationResultThatContains(78);
		assertEquals("size", 2, presults.size());
		double area = 0.0d;
		for(IPeakIntegrationResult r : presults) {
			area += r.getIntegratedArea();
		}
		assertEquals("SUM IntegratedArea", 942224.4d + 2393.4d, area, 0);
	}

	@Test
	public void testGetSize_1() {

		assertEquals("size", 4, results.size());
	}

	@Test
	public void testGetTotalIntegratedArea_1() {

		assertEquals("Total Integrated Area", 1539084.6d, results.getTotalPeakArea(), 0);
	}
}
