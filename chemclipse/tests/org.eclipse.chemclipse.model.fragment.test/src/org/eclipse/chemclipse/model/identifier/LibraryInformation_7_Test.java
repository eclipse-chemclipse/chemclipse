/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.identifier;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Test;

public class LibraryInformation_7_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();;

	private ISeparationColumn separationColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.DEFAULT);
	private ColumnIndexMarker columnIndexMarkerDefault = new ColumnIndexMarker(separationColumn);

	private ISeparationColumn db5 = new SeparationColumn("DB-5", SeparationColumnType.NON_POLAR);
	private ColumnIndexMarker columnIndexMarkerDB5 = new ColumnIndexMarker(db5);

	@Test
	public void test_1() {

		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
		columnIndexMarkerDefault.setRetentionIndex(20.5f);
		libraryInformation.add(columnIndexMarkerDefault);
		assertEquals(1, libraryInformation.getColumnIndexMarkers().size());
		assertEquals(20.5f, libraryInformation.getRetentionIndex(), 0);
	}

	@Test
	public void test_2() {

		libraryInformation.setRetentionIndex(12.1f);
		assertEquals(12.1f, libraryInformation.getRetentionIndex(), 0);
		columnIndexMarkerDefault.setRetentionIndex(0.0f);
		libraryInformation.add(columnIndexMarkerDefault);
		assertEquals(1, libraryInformation.getColumnIndexMarkers().size());
		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
	}

	@Test
	public void test_3() {

		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
		columnIndexMarkerDB5.setRetentionIndex(20.5f);
		libraryInformation.add(columnIndexMarkerDB5);
		assertEquals(2, libraryInformation.getColumnIndexMarkers().size());
		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
		assertEquals(20.5f, libraryInformation.getColumnIndexMarkers().get(1).getRetentionIndex(), 0);
	}
}