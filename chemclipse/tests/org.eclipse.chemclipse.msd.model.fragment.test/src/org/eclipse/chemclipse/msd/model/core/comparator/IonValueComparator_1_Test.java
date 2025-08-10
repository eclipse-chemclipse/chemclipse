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
package org.eclipse.chemclipse.msd.model.core.comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.junit.Test;

public class IonValueComparator_1_Test {

	@Test
	public void testAbundanceComparator_1() {

		IIon ion1 = new Ion(25.5f, 5000.5f);
		IIon ion2 = new Ion(25.5f, 5000.5f);
		IonValueComparator comparator = new IonValueComparator();
		assertEquals("Comparator", 0, comparator.compare(ion1, ion2));
	}

	@Test
	public void testAbundanceComparator_2() {

		assertThrows(NullPointerException.class, () -> {
			IIon ion1 = null;
			IIon ion2 = new Ion(25.5f, 5000.5f);
			IonValueComparator comparator = new IonValueComparator();
			assertEquals("Comparator", 0, comparator.compare(ion1, ion2));
		});
	}

	@Test
	public void testAbundanceComparator_3() {

		assertThrows(NullPointerException.class, () -> {
			IIon ion1 = new Ion(25.5f, 5000.5f);
			IIon ion2 = null;
			IonValueComparator comparator = new IonValueComparator();
			assertEquals("Comparator", 0, comparator.compare(ion1, ion2));
		});
	}

	@Test
	public void testAbundanceComparator_4() {

		IIon ion1 = new Ion(24.5f, 5000.5f);
		IIon ion2 = new Ion(25.5f, 5000.5f);
		IonValueComparator comparator = new IonValueComparator();
		assertEquals("Comparator", -1, comparator.compare(ion1, ion2));
	}

	@Test
	public void testAbundanceComparator_5() {

		IIon ion1 = new Ion(25.5f, 5000.5f);
		IIon ion2 = new Ion(24.5f, 4000.5f);
		IonValueComparator comparator = new IonValueComparator();
		assertEquals("Comparator", 1, comparator.compare(ion1, ion2));
	}
}
