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
package org.eclipse.chemclipse.msd.model.xic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignals;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.junit.Test;

public class TotalIonSignals_3_Test {

	private IChromatogramMSD chromatogram = new ChromatogramMSD();

	@Test
	public void testConstruct_1() {

		ITotalScanSignals signals = new TotalScanSignals(10, chromatogram);
		assertNotNull("getChromatogram", signals.getChromatogram());
	}

	@Test
	public void testConstruct_2() {

		ITotalScanSignals signals = new TotalScanSignals(10, null);
		assertNull("getChromatogram", signals.getChromatogram());
	}

	@Test
	public void testConstruct_3() {

		ITotalScanSignals signals = new TotalScanSignals(20, 40, chromatogram);
		assertNotNull("getChromatogram", signals.getChromatogram());
	}

	@Test
	public void testConstruct_4() {

		ITotalScanSignals signals = new TotalScanSignals(20, 40, null);
		assertNull("getChromatogram", signals.getChromatogram());
	}
}
