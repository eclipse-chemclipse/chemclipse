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
package org.eclipse.chemclipse.msd.model.internal.xic.comparator;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignalComparator;
import org.junit.Test;

public class TotalIonSignalComparator_1_Test {

	private ITotalScanSignal totalIonSignal1;
	private ITotalScanSignal totalIonSignal2;
	private TotalScanSignalComparator comparator = new TotalScanSignalComparator();

	@Test
	public void testComaparator_1() {

		totalIonSignal1 = new TotalScanSignal(0, 0.0f, 0.0f);
		totalIonSignal2 = new TotalScanSignal(0, 0.0f, 0.0f);
		assertEquals(0, comparator.compare(totalIonSignal1, totalIonSignal2));
	}

	@Test
	public void testComaparator_2() {

		totalIonSignal1 = null;
		totalIonSignal2 = new TotalScanSignal(0, 0.0f, 0.0f);
		assertEquals(0, comparator.compare(totalIonSignal1, totalIonSignal2));
	}

	@Test
	public void testComaparator_3() {

		totalIonSignal1 = new TotalScanSignal(0, 0.0f, 0.0f);
		totalIonSignal2 = null;
		assertEquals(0, comparator.compare(totalIonSignal1, totalIonSignal2));
	}

	@Test
	public void testComaparator_4() {

		totalIonSignal1 = new TotalScanSignal(0, 0.0f, 0.0f);
		totalIonSignal2 = new TotalScanSignal(0, 0.0f, 1.0f);
		assertEquals(-1, comparator.compare(totalIonSignal1, totalIonSignal2));
	}

	@Test
	public void testComaparator_5() {

		totalIonSignal1 = new TotalScanSignal(0, 0.0f, 1.0f);
		totalIonSignal2 = new TotalScanSignal(0, 0.0f, 0.0f);
		assertEquals(1, comparator.compare(totalIonSignal1, totalIonSignal2));
	}
}
