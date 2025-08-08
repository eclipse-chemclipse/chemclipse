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

public class RetentionIndices_1_Test {

	private RetentionIndices retentionIndices = new RetentionIndices();

	@Test
	public void testList_1() {

		try {
			retentionIndices.getActualRetentionIndex();
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("There is no retention index available", true);
		}
	}

	@Test
	public void testList_2() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(5758, 1000.0f, "C41"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5760, 2000.0f, "C42"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5770, 3000.0f, "C43"));
			assertEquals("getActualRetentionIndex", "C43", retentionIndices.getActualRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C42", retentionIndices.getPreviousRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C41", retentionIndices.getPreviousRetentionIndex().getName());
			assertEquals("getNextRetentionIndex", "C42", retentionIndices.getNextRetentionIndex().getName());
			assertEquals("getNextRetentionIndex", "C43", retentionIndices.getNextRetentionIndex().getName());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}

	@Test
	public void testList_3() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(5758, 1000.0f, "C41"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5760, 2000.0f, "C42"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5770, 3000.0f, "C43"));
			assertEquals("getActualRetentionIndex", "C43", retentionIndices.getActualRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C42", retentionIndices.getPreviousRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C41", retentionIndices.getPreviousRetentionIndex().getName());
			retentionIndices.getPreviousRetentionIndex();
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", true);
		}
	}

	@Test
	public void testList_4() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(5758, 1000.0f, "C41"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5760, 2000.0f, "C42"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5770, 3000.0f, "C43"));
			retentionIndices.getNextRetentionIndex().getName();
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", true);
		}
	}

	@Test
	public void testList_5() {

		try {
			retentionIndices.addRetentionIndex(new RetentionIndex(5770, 3000.0f, "C43"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5758, 1000.0f, "C41"));
			retentionIndices.addRetentionIndex(new RetentionIndex(5760, 2000.0f, "C42"));
			assertEquals("getActualRetentionIndex", "C43", retentionIndices.getLastRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C42", retentionIndices.getPreviousRetentionIndex().getName());
			assertEquals("getPreviousRetentionIndex", "C41", retentionIndices.getPreviousRetentionIndex().getName());
			assertEquals("getNextRetentionIndex", "C42", retentionIndices.getNextRetentionIndex().getName());
			assertEquals("getNextRetentionIndex", "C43", retentionIndices.getNextRetentionIndex().getName());
		} catch(RetentionIndexExistsException e) {
			assertTrue("This state should never be entered. - RetentionIndexExistsException", false);
		} catch(RetentionIndexValueException e) {
			assertTrue("This state should never be entered. - RetentionIndexValueException", false);
		} catch(NoRetentionIndexAvailableException e) {
			assertTrue("This state should never be entered. - NoRetentionIndexAvailableException", false);
		}
	}
}
