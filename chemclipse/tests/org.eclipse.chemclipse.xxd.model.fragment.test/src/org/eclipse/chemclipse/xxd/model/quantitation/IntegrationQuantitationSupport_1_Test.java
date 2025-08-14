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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.quantitation.QuantitationSupport;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.junit.Before;
import org.junit.Test;

public class IntegrationQuantitationSupport_1_Test extends QuantitationCalculator_TIC_TestCase {

	private QuantitationSupport support;

	@Override
	@Before
	public void setUp() {

		super.setUp();
		support = new QuantitationSupport(getReferencePeakMSD_TIC_1());
	}

	@Test
	public void testIsTheTotalSignalIntegrated_1() {

		assertTrue(support.isTotalSignalIntegrated());
	}

	@Test
	public void testValidateTIC_1() {

		assertTrue(support.validateTIC());
	}

	@Test
	public void testValidateXIC_1() {

		List<Double> selectedQuantitationIons = new ArrayList<Double>();
		selectedQuantitationIons.add(AbstractIon.TIC_ION);
		assertTrue(support.validateXIC(selectedQuantitationIons));
	}

	@Test
	public void testGetIntegrationArea_1() {

		assertEquals(750220.0d, support.getIntegrationArea(AbstractIon.TIC_ION), 0);
	}
}
