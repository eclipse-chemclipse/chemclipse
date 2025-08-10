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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Test;

public class Chromatogram_22_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testGetConverterId_1() {

		assertEquals("", chromatogram.getConverterId());
	}

	@Test
	public void testGetConverterId_2() {

		chromatogram.setConverterId(null);
		assertEquals(null, chromatogram.getConverterId());
	}

	@Test
	public void testGetConverterId_3() {

		String id = "org.eclipse.chemclipse.msd.converter.supplier.test";
		chromatogram.setConverterId(id);
		assertEquals(id, chromatogram.getConverterId());
	}
}
