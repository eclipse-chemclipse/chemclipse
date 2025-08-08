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
 * Janos Binder - adjusted test cases
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.unitsumnormalizer.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.unitsumnormalizer.settings.FilterSettings;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class UnitSumNormalizerFilter_1_Test extends ChromatogramImporterTestCase {

	private IChromatogramFilter chromatogramFilter;
	private FilterSettings filterSettings;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		chromatogramFilter = new ChromatogramFilter();
		filterSettings = new FilterSettings();
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

		chromatogramFilter.applyFilter(chromatogramSelection, filterSettings, new NullProgressMonitor());

		totalSignal = chromatogram.getScan(1).getTotalSignal(); // Total signal intensity 1.024242304E9 - with this number, we divide the values above
		assertEquals("scan totalSignal", 6.659335E-5f, totalSignal, 0);
		totalSignal = chromatogram.getScan(5726).getTotalSignal();
		assertEquals("scan totalSignal", 1.499627E-4f, totalSignal, 0);
		totalSignal = chromatogram.getScan(238).getTotalSignal();
		assertEquals("scan totalSignal", 9.242054E-5f, totalSignal, 0);
		totalSignal = chromatogram.getScan(628).getTotalSignal();
		assertEquals("scan totalSignal", 2.6961241E-3f, totalSignal, 0);
	}
}
