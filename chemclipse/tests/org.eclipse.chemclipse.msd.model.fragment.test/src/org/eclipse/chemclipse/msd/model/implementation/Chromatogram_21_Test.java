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

public class Chromatogram_21_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testGetStartIon_1() {

		double startIon = chromatogram.getStartIon();
		assertEquals("startIon", 0.0d, startIon, 0);
	}

	@Test
	public void testGetStopIon_1() {

		double stopIon = chromatogram.getStopIon();
		assertEquals("stopIon", 0.0d, stopIon, 0);
	}
}
