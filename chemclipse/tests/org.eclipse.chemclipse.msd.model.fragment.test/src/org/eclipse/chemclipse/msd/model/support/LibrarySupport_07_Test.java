/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnFactory;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.RegularLibraryMassSpectrum;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that non-DEFAULT IColumnIndexMarkers from the first entry are
 * copied to the merged result, and that an identical marker contributed by a
 * subsequent entry is deduplicated while a new marker from that entry is added.
 *
 * Entry 1 carries a POLAR marker (RI 1200) and a SEMI_POLAR marker (RI 1100).
 * Entry 2 carries the same POLAR marker (RI 1200, deduplicated) plus a new
 * NON_POLAR marker (RI 1050).
 *
 * Expected column index markers in the merged result:
 * DEFAULT (RI 0), POLAR (RI 1200), SEMI_POLAR (RI 1100), NON_POLAR (RI 1050) = 4.
 */
public class LibrarySupport_07_Test {

	private List<IColumnIndexMarker> markers;

	@BeforeEach
	public void setUp() throws Exception {

		ISeparationColumn polarColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.POLAR);
		ISeparationColumn semiPolarColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.SEMI_POLAR);
		ISeparationColumn nonPolarColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.NON_POLAR);

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(300000);
		entry1.addIon(new Ion(91.0d, 1000.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Toluene");
		info1.add(new ColumnIndexMarker(polarColumn, 1200.0f));
		info1.add(new ColumnIndexMarker(semiPolarColumn, 1100.0f));
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(305000);
		entry2.addIon(new Ion(65.0d, 400.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("Toluene");
		info2.add(new ColumnIndexMarker(polarColumn, 1200.0f));
		info2.add(new ColumnIndexMarker(nonPolarColumn, 1050.0f));
		entry2.setLibraryInformation(info2);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		IRegularLibraryMassSpectrum merged = LibrarySupport.merge(selected);
		markers = merged.getLibraryInformation().getColumnIndexMarkers();
	}

	@Test
	public void testTotalMarkerCount() {

		assertEquals(4, markers.size());
	}

	@Test
	public void testPolarMarkerPresent() {

		assertNotNull(findMarker(SeparationColumnType.POLAR));
	}

	@Test
	public void testPolarMarkerRetentionIndex() {

		IColumnIndexMarker marker = findMarker(SeparationColumnType.POLAR);
		assertNotNull(marker);
		assertEquals(1200.0f, marker.getRetentionIndex(), 0.01f);
	}

	@Test
	public void testPolarMarkerNotDuplicated() {

		assertEquals(1, countMarkers(SeparationColumnType.POLAR));
	}

	@Test
	public void testSemiPolarMarkerPresent() {

		assertNotNull(findMarker(SeparationColumnType.SEMI_POLAR));
	}

	@Test
	public void testSemiPolarMarkerRetentionIndex() {

		IColumnIndexMarker marker = findMarker(SeparationColumnType.SEMI_POLAR);
		assertNotNull(marker);
		assertEquals(1100.0f, marker.getRetentionIndex(), 0.01f);
	}

	@Test
	public void testNonPolarMarkerPresent() {

		assertNotNull(findMarker(SeparationColumnType.NON_POLAR));
	}

	@Test
	public void testNonPolarMarkerRetentionIndex() {

		IColumnIndexMarker marker = findMarker(SeparationColumnType.NON_POLAR);
		assertNotNull(marker);
		assertEquals(1050.0f, marker.getRetentionIndex(), 0.01f);
	}

	private IColumnIndexMarker findMarker(SeparationColumnType type) {

		ISeparationColumn column = SeparationColumnFactory.getSeparationColumn(type);
		return markers.stream()
				.filter(m -> column.equals(m.getSeparationColumn()))
				.findFirst()
				.orElse(null);
	}

	private long countMarkers(SeparationColumnType type) {

		ISeparationColumn column = SeparationColumnFactory.getSeparationColumn(type);
		return markers.stream()
				.filter(m -> column.equals(m.getSeparationColumn()))
				.count();
	}
}
