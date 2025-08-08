/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.classifier.supplier.wnc.settings;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WncClassifierSettings_1_Test {

	private ClassifierSettings classifierSettings = new ClassifierSettings();

	@Test
	public void testGetWncIons_1() {

		assertNotNull(classifierSettings.getTargetTraces());
	}
}