/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.identifier.settings;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.msd.identifier.settings.IMassSpectrumComparatorSettings;
import org.junit.Test;

public class IdentifierSettingsMSD_1_Test {

	@Test
	public void test1() {

		assertEquals("org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.cosine", IMassSpectrumComparatorSettings.DEFAULT_COMPARATOR_ID);
	}
}
