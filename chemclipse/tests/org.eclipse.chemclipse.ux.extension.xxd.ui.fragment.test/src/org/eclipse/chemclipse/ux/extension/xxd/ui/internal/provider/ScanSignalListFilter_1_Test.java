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
 * Aleksandar Kurtakov - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.junit.jupiter.api.Test;

public class ScanSignalListFilter_1_Test {

	private final ScanSignalListFilter filter = new ScanSignalListFilter();

	@Test
	public void testEmptySearchKeepsAll() {

		filter.setSearchText("", false);
		assertTrue(filter.select(null, null, new Ion(123.4d, 1000f)));
	}

	@Test
	public void testPlainIon() {

		filter.setSearchText("123", false);
		assertTrue(filter.select(null, null, new Ion(123.4d, 1000f)));
		assertFalse(filter.select(null, null, new Ion(456.7d, 1000f)));
	}

	@Test
	public void testIonMSnWithTransition() {

		IonTransition ionTransition = new IonTransition(250.1d, 123.2d, 15.0d, 1.0d, 1.0d, 1);
		filter.setSearchText("250", false);
		assertTrue(filter.select(null, null, new IonMSn(123.2d, 1000f, ionTransition)));
		filter.setSearchText("999", false);
		assertFalse(filter.select(null, null, new IonMSn(123.2d, 1000f, ionTransition)));
	}
}
