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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.TotalScanSignal;
import org.junit.Before;
import org.junit.Test;

/**
 * HashCode and equals test.
 * 
 * @author Philip Wenig
 */
public class TotalIonSignal_5_Test {

	private ITotalScanSignal totalIonSignal1;
	private ITotalScanSignal totalIonSignal2;

	@Before
	public void setUp() {

		totalIonSignal1 = new TotalScanSignal(5720, 1245.5f, 25476.45f);
		totalIonSignal2 = new TotalScanSignal(5725, 1245.5f, 25476.45f);
	}

	@Test
	public void testHashCode_1() {

		assertNotEquals("hashCode", totalIonSignal1.hashCode(), totalIonSignal2.hashCode());
	}

	@Test
	public void testHashCode_2() {

		assertNotEquals("hashCode", totalIonSignal2.hashCode(), totalIonSignal1.hashCode());
	}

	@Test
	public void testEquals_1() {

		assertNotEquals("equals", totalIonSignal1, totalIonSignal2);
	}

	@Test
	public void testEquals_2() {

		assertNotEquals("equals", totalIonSignal2, totalIonSignal1);
	}

	@Test
	public void testEquals_3() {

		assertNotNull("equals", totalIonSignal1);
	}

	@Test
	public void testEquals_4() {

		assertNotNull("equals", totalIonSignal2);
	}

	@Test
	public void testEquals_5() {

		assertEquals("equals", totalIonSignal1, totalIonSignal1);
	}

	@Test
	public void testEquals_6() {

		assertEquals("equals", totalIonSignal2, totalIonSignal2);
	}

	@Test
	public void testEquals_7() {

		assertNotEquals("equals", totalIonSignal1, new Object());
	}

	@Test
	public void testEquals_8() {

		assertNotEquals("equals", totalIonSignal2, new Object());
	}
}
