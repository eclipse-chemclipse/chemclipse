/*******************************************************************************
 * Copyright (c) 2020, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.settings;

import junit.framework.TestCase;

public class IdentifierSettingsMSD_1_Test extends TestCase {

	public void test1() {

		assertEquals("org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.cosine", IMassSpectrumComparatorSettings.DEFAULT_COMPARATOR_ID);
	}
}
