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
package org.eclipse.chemclipse.chromatogram.alignment.model.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.chemclipse.chromatogram.alignment.model.exceptions.NoRetentionIndexAvailableException;
import org.eclipse.chemclipse.chromatogram.alignment.model.exceptions.RetentionIndexExistsException;
import org.eclipse.chemclipse.chromatogram.alignment.model.exceptions.RetentionIndexValueException;
import org.junit.Test;

public class RetentionIndices_2_Test {

	private RetentionIndices retentionIndices = new RetentionIndices();

	@Test
	public void testPreviousNextByTime_1() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(4500, 1000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(254500, 2000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(505500, 3000.0f));
			assertEquals("getPreviousRetentionIndex", 4500, retentionIndices.getPreviousRetentionIndex(5500).getRetentionTime());
			assertEquals("getNextRetentionIndex", 254500, retentionIndices.getNextRetentionIndex(5500).getRetentionTime());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}

	@Test
	public void testPreviousNextByTime_2() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(4500, 1000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(254500, 2000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(505500, 3000.0f));
			assertEquals("getPreviousRetentionIndex", 254500, retentionIndices.getPreviousRetentionIndex(255500).getRetentionTime());
			assertEquals("getNextRetentionIndex", 505500, retentionIndices.getNextRetentionIndex(255500).getRetentionTime());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}

	@Test
	public void testPreviousNextByIndex_1() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(4500, 1000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(254500, 2000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(505500, 3000.0f));
			assertEquals("getPreviousRetentionIndex", 4500, retentionIndices.getPreviousRetentionIndex(1100.0f).getRetentionTime());
			assertEquals("getNextRetentionIndex", 254500, retentionIndices.getNextRetentionIndex(1100.0f).getRetentionTime());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}

	@Test
	public void testPreviousNextByIndex_2() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(4500, 1000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(254500, 2000.0f));
			retentionIndices.addRetentionIndex(new RetentionIndex(505500, 3000.0f));
			assertEquals("getPreviousRetentionIndex", 254500, retentionIndices.getPreviousRetentionIndex(2100.0f).getRetentionTime());
			assertEquals("getNextRetentionIndex", 505500, retentionIndices.getNextRetentionIndex(2100.0f).getRetentionTime());
			assertEquals("getFirstRetentionIndex", 4500, retentionIndices.getFirstRetentionIndex().getRetentionTime());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}
}
