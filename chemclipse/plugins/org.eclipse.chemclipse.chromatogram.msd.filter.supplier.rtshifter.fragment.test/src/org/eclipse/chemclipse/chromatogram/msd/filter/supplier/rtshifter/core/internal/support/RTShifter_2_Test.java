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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.rtshifter.core.internal.support;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.rtshifter.exceptions.FilterException;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.rtshifter.settings.ISupplierFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.rtshifter.settings.SupplierFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IVendorMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;

public class RTShifter_2_Test extends RTShifterChromatogramTestCase {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		chromatogram = getChromatogram();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testShiftForward_1() {

		IVendorMassSpectrum scan;
		/*
		 * PRE TESTS
		 */
		assertEquals(10, chromatogram.getNumberOfScans());
		assertEquals(1500, chromatogram.getScanDelay());
		scan = chromatogram.getSupplierScan(1);
		assertEquals(1500, scan.getRetentionTime());
		scan = chromatogram.getSupplierScan(10);
		assertEquals(10500, scan.getRetentionTime());
	}

	public void testShiftForward_2() {

		IVendorMassSpectrum scan;
		try {
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings(0, true);
			RTShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(1500, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(1500, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(10500, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_3() {

		IVendorMassSpectrum scan;
		try {
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings(1000, true);
			RTShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(2500, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(2500, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(11500, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_4() {

		IVendorMassSpectrum scan;
		try {
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings(1499, true);
			RTShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(2999, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(2999, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(11999, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_5() {

		IVendorMassSpectrum scan;
		try {
			ISupplierFilterSettings supplierFilterSettings = new SupplierFilterSettings(1500, true);
			RTShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(3000, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(3000, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(12000, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}
}
