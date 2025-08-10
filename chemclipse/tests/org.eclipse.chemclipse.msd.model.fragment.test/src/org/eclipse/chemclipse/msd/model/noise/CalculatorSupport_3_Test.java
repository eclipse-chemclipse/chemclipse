/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.noise;

import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.junit.Test;

public class CalculatorSupport_3_Test {

	private CalculatorSupport calculatorSupport = new CalculatorSupport();

	@Test
	public void testGetScanRange_1() {

		assertThrows(FilterException.class, () -> {
			calculatorSupport.checkScanRange(null, 13);
		});
	}

	@Test
	public void testGetScanRange_2() throws FilterException {

		assertThrows(FilterException.class, () -> {
			ScanRange scanRange = new ScanRange(1, 13);
			calculatorSupport.checkScanRange(scanRange, 13);
		});
	}

	@Test
	public void testGetScanRange_3() {

		assertThrows(FilterException.class, () -> {
			ScanRange scanRange = new ScanRange(1, 12);
			calculatorSupport.checkScanRange(scanRange, 13);
		});
	}
}
