/*******************************************************************************
 * Copyright (c) 2011, 2016 Philip (eselmeister) Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.rtshifter.settings;

import junit.framework.TestCase;

public class RTShifterFilterSettings_1_Test extends TestCase {

	private SupplierFilterSettings rtShifterFilterSettings;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testCreateSettings_1() {

		int millisecondsToShift = 0;
		rtShifterFilterSettings = new SupplierFilterSettings(millisecondsToShift, true);
		assertEquals(millisecondsToShift, rtShifterFilterSettings.getMillisecondsToShift());
	}

	public void testCreateSettings_2() {

		int millisecondsToShift = 1;
		rtShifterFilterSettings = new SupplierFilterSettings(millisecondsToShift, true);
		assertEquals(millisecondsToShift, rtShifterFilterSettings.getMillisecondsToShift());
	}

	public void testCreateSettings_3() {

		int millisecondsToShift = -1;
		rtShifterFilterSettings = new SupplierFilterSettings(millisecondsToShift, true);
		assertEquals(millisecondsToShift, rtShifterFilterSettings.getMillisecondsToShift());
	}

	public void testCreateSettings_4() {

		int millisecondsToShift = 1500;
		rtShifterFilterSettings = new SupplierFilterSettings(millisecondsToShift, true);
		assertEquals(millisecondsToShift, rtShifterFilterSettings.getMillisecondsToShift());
	}

	public void testCreateSettings_5() {

		int millisecondsToShift = -1500;
		rtShifterFilterSettings = new SupplierFilterSettings(millisecondsToShift, true);
		assertEquals(millisecondsToShift, rtShifterFilterSettings.getMillisecondsToShift());
	}
}
