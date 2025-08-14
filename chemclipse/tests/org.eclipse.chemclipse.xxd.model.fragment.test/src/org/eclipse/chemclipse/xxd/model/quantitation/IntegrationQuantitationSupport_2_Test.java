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
package org.eclipse.chemclipse.xxd.model.quantitation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.quantitation.QuantitationSupport;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.junit.Before;
import org.junit.Test;

public class IntegrationQuantitationSupport_2_Test extends QuantitationCalculator_TIC_TestCase {

	private QuantitationSupport support;

	@Override
	@Before
	public void setUp() {

		super.setUp();
		support = new QuantitationSupport(getReferencePeakMSD_XIC_1());
	}

	@Test
	public void testIsTheTotalSignalIntegrated_1() {

		assertFalse(support.isTotalSignalIntegrated());
	}

	@Test
	public void testValidateTIC_1() {

		assertFalse(support.validateTIC());
	}

	@Test
	public void testValidateXIC_1() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(AbstractIon.TIC_ION);
		assertFalse(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testValidateXIC_2() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(104.0d);
		selectedQuantitationIons.add(103.0d);
		assertTrue(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testValidateXIC_3() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(104.0d);
		selectedQuantitationIons.add(103.0d);
		selectedQuantitationIons.add(51.0d);
		selectedQuantitationIons.add(50.0d);
		selectedQuantitationIons.add(78.0d);
		selectedQuantitationIons.add(77.0d);
		selectedQuantitationIons.add(74.0d);
		selectedQuantitationIons.add(105.0d);
		assertTrue(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testValidateXIC_4() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(180.0d); // does not exist
		assertFalse(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testValidateXIC_5() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(104.0d);
		selectedQuantitationIons.add(103.0d);
		selectedQuantitationIons.add(51.0d);
		selectedQuantitationIons.add(50.0d);
		selectedQuantitationIons.add(78.0d);
		selectedQuantitationIons.add(77.0d);
		selectedQuantitationIons.add(74.0d);
		selectedQuantitationIons.add(105.0d);
		selectedQuantitationIons.add(180.0d); // does not exist
		assertFalse(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testGetIntegrationArea_1() {

		assertEquals(2638892.754731409d, support.getIntegrationArea(104.0d), 0);
	}

	@Test
	public void testGetIntegrationArea_2() {

		assertEquals(665459.9120627032d, support.getIntegrationArea(103.0d), 0);
	}

	@Test
	public void testGetIntegrationArea_3() {

		assertEquals(270773.34352896194d, support.getIntegrationArea(78.0d), 0);
	}

	@Test
	public void testGetIntegrationArea_4() {

		assertEquals(0.0d, support.getIntegrationArea(180.0d), 0);
	}
}
