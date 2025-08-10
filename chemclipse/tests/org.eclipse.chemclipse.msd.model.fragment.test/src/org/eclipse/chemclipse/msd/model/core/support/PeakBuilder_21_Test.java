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
package org.eclipse.chemclipse.msd.model.core.support;

import static org.junit.Assert.assertThrows;

import org.easymock.EasyMock;
import org.eclipse.chemclipse.model.exceptions.PeakException;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the peak exceptions.
 */
public class PeakBuilder_21_Test {

	private IChromatogramMSD chromatogram;
	private IScanRange scanRange;

	@Before
	public void setUp() throws Exception {

		chromatogram = EasyMock.createMock(IChromatogramMSD.class);
		EasyMock.expect(chromatogram.getNumberOfScans()).andStubReturn(20);
		EasyMock.replay(chromatogram);
	}

	@Test
	public void testCheckScanRange_1() {

		scanRange = new ScanRange(10, 20);
		PeakBuilderMSD.checkScanRange(chromatogram, scanRange);
	}

	@Test
	public void testCheckScanRange_2() {

		scanRange = new ScanRange(0, 20);
		PeakBuilderMSD.checkScanRange(chromatogram, scanRange);
	}

	@Test
	public void testCheckScanRange_3() {

		scanRange = new ScanRange(10, 22);
		assertThrows(PeakException.class, () -> {
			PeakBuilderMSD.checkScanRange(chromatogram, scanRange);
		});
	}
}
