/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.support;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.columns.SeparationColumn;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Before;
import org.junit.Test;

public class ColumnIndexSupport_1_Test {

	private List<IColumnIndexMarker> columnIndexMarkers = new ArrayList<>();

	@Before
	public void setUp() throws Exception {

		columnIndexMarkers.add(new ColumnIndexMarker(new SeparationColumn("DB 1701", SeparationColumnType.SEMI_POLAR), 1230.8f));
		columnIndexMarkers.add(new ColumnIndexMarker(new SeparationColumn("FFAP X", SeparationColumnType.POLAR), 1456.7f));
		columnIndexMarkers.add(new ColumnIndexMarker(new SeparationColumn("DB 1", SeparationColumnType.NON_POLAR), 1302.8f));
	}

	@Test
	public void test1() {

		assertEquals(1456.7f, ColumnIndexSupport.getRetentionIndex(columnIndexMarkers, "ffap x", false, false), 0);
	}

	@Test
	public void test2() {

		assertEquals(1456.7f, ColumnIndexSupport.getRetentionIndex(columnIndexMarkers, "FFAP X", true, false), 0);
	}

	@Test
	public void test3() {

		assertEquals(1456.7f, ColumnIndexSupport.getRetentionIndex(columnIndexMarkers, "ffapx", false, true), 0);
	}

	@Test
	public void test4() {

		assertEquals(1456.7f, ColumnIndexSupport.getRetentionIndex(columnIndexMarkers, "FFAPX", true, true), 0);
	}
}