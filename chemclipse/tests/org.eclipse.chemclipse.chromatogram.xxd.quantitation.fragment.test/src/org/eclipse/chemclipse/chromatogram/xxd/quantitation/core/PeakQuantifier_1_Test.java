/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.exceptions.NoPeakQuantifierAvailableException;

import junit.framework.TestCase;

public class PeakQuantifier_1_Test extends TestCase {

	private IPeakQuantifierSupport support;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		support = PeakQuantifier.getPeakQuantifierSupport();
	}

	@Override
	protected void tearDown() throws Exception {

		support = null;
		super.tearDown();
	}

	public void testGetMassSpectrumComparatorSupport_3() throws NoPeakQuantifierAvailableException {

		List<String> ids = support.getAvailablePeakQuantifierIds();
		List<String> rcs = new ArrayList<String>();
		for(String id : ids) {
			rcs.add(id);
		}
		String id;
		for(int i = 0; i < rcs.size(); i++) {
			id = support.getPeakQuantifierId(i);
			assertEquals("getDetectorId", id, rcs.get(i));
		}
	}

	public void testGetMassSpectrumComparisonSupplier_1() {

		try {
			support.getPeakQuantifierSupplier("");
		} catch(NoPeakQuantifierAvailableException e) {
			assertTrue("NoPeakQuantifierAvailableException", true);
		}
	}

	public void testGetMassSpectrumComparisonSupplier_2() {

		try {
			support.getPeakQuantifierSupplier(null);
		} catch(NoPeakQuantifierAvailableException e) {
			assertTrue("NoPeakQuantifierAvailableException", true);
		}
	}
}
