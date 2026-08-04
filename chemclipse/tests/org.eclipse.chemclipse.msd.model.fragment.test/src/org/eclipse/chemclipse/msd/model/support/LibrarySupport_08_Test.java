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
 * Verifies that non-DEFAULT IColumnIndexMarkers contributed by subsequent
 * entries (not the first) are added to the merged result.
 *
 * Entry 1 carries no non-DEFAULT markers (DEFAULT only).
 * Entry 2 contributes a POLAR marker (RI 1300).
 * Entry 3 contributes a NON_POLAR marker (RI 1250).
 *
 * Expected column index markers in the merged result:
 * DEFAULT (RI 0), POLAR (RI 1300), NON_POLAR (RI 1250) = 3.
 */
public class LibrarySupport_08_Test {

	private List<IColumnIndexMarker> markers;

	@BeforeEach
	public void setUp() throws Exception {

		ISeparationColumn polarColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.POLAR);
		ISeparationColumn nonPolarColumn = SeparationColumnFactory.getSeparationColumn(SeparationColumnType.NON_POLAR);

		RegularLibraryMassSpectrum entry1 = new RegularLibraryMassSpectrum();
		entry1.setRetentionTime(200000);
		entry1.addIon(new Ion(78.0d, 999.0f));
		ILibraryInformation info1 = new LibraryInformation();
		info1.setName("Benzene");
		entry1.setLibraryInformation(info1);

		RegularLibraryMassSpectrum entry2 = new RegularLibraryMassSpectrum();
		entry2.setRetentionTime(205000);
		entry2.addIon(new Ion(51.0d, 500.0f));
		ILibraryInformation info2 = new LibraryInformation();
		info2.setName("Benzene");
		info2.add(new ColumnIndexMarker(polarColumn, 1300.0f));
		entry2.setLibraryInformation(info2);

		RegularLibraryMassSpectrum entry3 = new RegularLibraryMassSpectrum();
		entry3.setRetentionTime(210000);
		entry3.addIon(new Ion(50.0d, 300.0f));
		ILibraryInformation info3 = new LibraryInformation();
		info3.setName("Benzene");
		info3.add(new ColumnIndexMarker(nonPolarColumn, 1250.0f));
		entry3.setLibraryInformation(info3);

		List<IScanMSD> selected = new ArrayList<>();
		selected.add(entry1);
		selected.add(entry2);
		selected.add(entry3);
		IRegularLibraryMassSpectrum merged = LibrarySupport.merge(selected);
		markers = merged.getLibraryInformation().getColumnIndexMarkers();
	}

	@Test
	public void testTotalMarkerCount() {

		assertEquals(3, markers.size());
	}

	@Test
	public void testPolarMarkerPresent() {

		assertNotNull(findMarker(SeparationColumnType.POLAR));
	}

	@Test
	public void testPolarMarkerRetentionIndex() {

		IColumnIndexMarker marker = findMarker(SeparationColumnType.POLAR);
		assertNotNull(marker);
		assertEquals(1300.0f, marker.getRetentionIndex(), 0.01f);
	}

	@Test
	public void testNonPolarMarkerPresent() {

		assertNotNull(findMarker(SeparationColumnType.NON_POLAR));
	}

	@Test
	public void testNonPolarMarkerRetentionIndex() {

		IColumnIndexMarker marker = findMarker(SeparationColumnType.NON_POLAR);
		assertNotNull(marker);
		assertEquals(1250.0f, marker.getRetentionIndex(), 0.01f);
	}

	private IColumnIndexMarker findMarker(SeparationColumnType type) {

		ISeparationColumn column = SeparationColumnFactory.getSeparationColumn(type);
		return markers.stream()
				.filter(m -> column.equals(m.getSeparationColumn()))
				.findFirst()
				.orElse(null);
	}
}
