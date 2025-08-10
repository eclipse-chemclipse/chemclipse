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
package org.eclipse.chemclipse.msd.converter.chromatogram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.converter.chromatogram.IChromatogramConverterSupport;
import org.eclipse.chemclipse.converter.core.IConverterSupport;
import org.junit.Test;

/**
 * Testing the method getChromatogramConverterSupport() in
 * ChromatogramConverter.
 */
public class ChromatogramConverter_2_Test {

	private IChromatogramConverterSupport support = ChromatogramConverterMSD.getInstance().getChromatogramConverterSupport();

	@Test
	public void testFilterNames_1() {

		String[] filterNames = support.getFilterNames(IConverterSupport.ALL_SUPPLIER);
		String result = "";
		for(String name : filterNames) {
			result += name + ";";
		}
		/*
		 * There could be more converter. But these 3 should be there in
		 * every case.
		 */
		assertTrue("Amount Filter Names", 3 <= filterNames.length);
		assertEquals("FilterName", true, result.contains("Open Chromatography Binary (*.ocb)"));
	}
}
