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

import org.junit.Test;

public class RetentionIndex_2_Test {

	@Test
	public void testRetentionTime_1() {

		RetentionIndex retentionIndex = new RetentionIndex();
		assertEquals("RetentionTime", 0, retentionIndex.getRetentionTime());
		assertEquals("Index", 0.0f, retentionIndex.getIndex(), 0);
		assertEquals("Name", "", retentionIndex.getName());
	}

	@Test
	public void testRetentionTime_2() {

		RetentionIndex retentionIndex = new RetentionIndex(7500, 8000.2f);
		assertEquals("RetentionTime", 7500, retentionIndex.getRetentionTime());
		assertEquals("Index", 8000.2f, retentionIndex.getIndex(), 0);
		assertEquals("Name", "", retentionIndex.getName());
	}

	@Test
	public void testRetentionTime_3() {

		RetentionIndex retentionIndex = new RetentionIndex(7500, 8000.2f, "C41");
		assertEquals("RetentionTime", 7500, retentionIndex.getRetentionTime());
		assertEquals("Index", 8000.2f, retentionIndex.getIndex(), 0);
		assertEquals("Name", "C41", retentionIndex.getName());
	}

	@Test
	public void testRetentionTime_4() {

		RetentionIndex retentionIndex = new RetentionIndex(-7500, -8000.2f, "C41");
		assertEquals("RetentionTime", 0, retentionIndex.getRetentionTime());
		assertEquals("Index", 0.0f, retentionIndex.getIndex(), 0);
		assertEquals("Name", "C41", retentionIndex.getName());
	}
}
