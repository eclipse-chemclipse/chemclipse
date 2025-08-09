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
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.Test;

public class LibraryInformation_6_Test {

	private ILibraryInformation libraryInformation = new LibraryInformation();
	private ISeparationColumn db5 = new SeparationColumn("DB-5", SeparationColumnType.NON_POLAR);
	private ISeparationColumn db1701 = new SeparationColumn("DB-1701", SeparationColumnType.SEMI_POLAR);
	private ISeparationColumn ffap = new SeparationColumn("FFAP", SeparationColumnType.POLAR);

	@Test
	public void test_1() {

		assertEquals(0.0f, libraryInformation.getRetentionIndex(), 0);
	}

	@Test
	public void test_2() {

		libraryInformation.setRetentionIndex(725.5f);
		assertEquals(725.5f, libraryInformation.getRetentionIndex(), 0);
	}

	@Test
	public void test_3() {

		assertEquals(1, libraryInformation.getColumnIndexMarkers().size());
	}

	@Test
	public void test_4() {

		libraryInformation.setRetentionIndex(725.5f);
		libraryInformation.add(new ColumnIndexMarker(db5, 715.4f));
		libraryInformation.add(new ColumnIndexMarker(db1701, 729.3f));
		libraryInformation.add(new ColumnIndexMarker(ffap, 717.2f));
		assertEquals(725.5f, libraryInformation.getRetentionIndex(), 0);
		assertEquals(4, libraryInformation.getColumnIndexMarkers().size());
	}

	@Test
	public void test_5() {

		IColumnIndexMarker columnIndexMarker = null;
		libraryInformation.delete(columnIndexMarker);
		assertEquals(1, libraryInformation.getColumnIndexMarkers().size());
	}

	@Test
	public void test_6() {

		libraryInformation.setRetentionIndex(725.5f);

		IColumnIndexMarker markerDefault = null;
		IColumnIndexMarker markerDB5 = new ColumnIndexMarker(db5, 715.4f);
		IColumnIndexMarker markerDB1701 = new ColumnIndexMarker(db1701, 729.3f);
		IColumnIndexMarker markerFFAP = new ColumnIndexMarker(ffap, 717.2f);

		libraryInformation.add(markerDB5);
		libraryInformation.add(markerDB1701);
		libraryInformation.add(markerFFAP);

		for(IColumnIndexMarker columnIndexMarker : libraryInformation.getColumnIndexMarkers()) {
			if(columnIndexMarker.getSeparationColumn().getSeparationColumnType().equals(SeparationColumnType.DEFAULT)) {
				markerDefault = columnIndexMarker;
			}
		}

		assertEquals(4, libraryInformation.getColumnIndexMarkers().size());
		libraryInformation.delete(markerDefault);
		assertEquals(4, libraryInformation.getColumnIndexMarkers().size());
		libraryInformation.delete(markerDB5);
		assertEquals(3, libraryInformation.getColumnIndexMarkers().size());
		libraryInformation.delete(markerDB1701);
		assertEquals(2, libraryInformation.getColumnIndexMarkers().size());
		libraryInformation.delete(markerFFAP);
		assertEquals(1, libraryInformation.getColumnIndexMarkers().size());
	}
}