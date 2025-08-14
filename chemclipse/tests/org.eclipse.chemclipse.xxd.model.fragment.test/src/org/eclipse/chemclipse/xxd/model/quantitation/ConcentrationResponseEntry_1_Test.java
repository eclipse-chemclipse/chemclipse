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

import org.eclipse.chemclipse.model.quantitation.IResponseSignal;
import org.eclipse.chemclipse.model.quantitation.ResponseSignal;
import org.junit.Before;
import org.junit.Test;

public class ConcentrationResponseEntry_1_Test {

	private IResponseSignal concentrationResponseEntry;

	@Before
	public void setUp() {

		concentrationResponseEntry = new ResponseSignal(76.2d, 0.7d, 47875);
	}

	@Test
	public void testGetIon_1() {

		assertEquals(76.2d, concentrationResponseEntry.getSignal(), 0);
	}

	@Test
	public void testGetConcenctration_1() {

		assertEquals(0.7d, concentrationResponseEntry.getConcentration(), 0);
	}

	@Test
	public void testGetResponse_1() {

		assertEquals(47875.0d, concentrationResponseEntry.getResponse(), 0);
	}
}
