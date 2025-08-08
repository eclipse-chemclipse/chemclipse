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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.multiplier.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.multiplier.settings.MultiplierSettings;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MultiplierFilter_1_Test extends ChromatogramImporterTestCase {

	private IChromatogramFilter chromatogramFilter;
	private MultiplierSettings multiplierFilterSettings;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		chromatogramFilter = new MultiplierChromatogramFilter();
		multiplierFilterSettings = new MultiplierSettings();
		multiplierFilterSettings.setMultiplier(0.01f);
	}

	@Test
	public void testApplyFilter_1() {

		float totalSignal;

		totalSignal = chromatogram.getScan(1).getTotalSignal();
		assertEquals("scan totalSignal", 67864.0f, totalSignal, 0);
		totalSignal = chromatogram.getScan(5726).getTotalSignal();
		assertEquals("scan totalSignal", 152824.0f, totalSignal, 0);
		totalSignal = chromatogram.getScan(238).getTotalSignal();
		assertEquals("scan totalSignal", 94184.0f, totalSignal, 0);
		totalSignal = chromatogram.getScan(628).getTotalSignal();
		assertEquals("scan totalSignal", 2747568.0f, totalSignal, 0);

		chromatogramFilter.applyFilter(chromatogramSelection, multiplierFilterSettings, new NullProgressMonitor());

		totalSignal = chromatogram.getScan(1).getTotalSignal();
		assertEquals("scan totalSignal", 678.640f, totalSignal, 10E-2);
		totalSignal = chromatogram.getScan(5726).getTotalSignal();
		assertEquals("scan totalSignal", 1528.240f, totalSignal, 10E-2);
		totalSignal = chromatogram.getScan(238).getTotalSignal();
		assertEquals("scan totalSignal", 941.840f, totalSignal, 10E-2);
		totalSignal = chromatogram.getScan(628).getTotalSignal();
		assertEquals("scan totalSignal", 27475.680f, totalSignal, 10E-2);
	}
}
