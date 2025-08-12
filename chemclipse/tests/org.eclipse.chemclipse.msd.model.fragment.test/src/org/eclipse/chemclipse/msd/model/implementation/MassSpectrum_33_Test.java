/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model.implementation;

import static org.junit.Assert.assertFalse;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.junit.Test;

public class MassSpectrum_33_Test {

	private IScanMSD massSpectrum = new ScanMSD();

	@Test
	public void test_1() {

		assertFalse(massSpectrum.isTandemMS());
	}

	@Test
	public void test_2() {

		assertFalse(massSpectrum.isHighResolutionMS());
	}
}
