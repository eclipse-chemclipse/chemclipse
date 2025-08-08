/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RetentionIndexCalculator_1_Test {

	@Test
	public void test1() {

		String[] standards = RetentionIndexCalculator.getStandards();
		assertEquals(99, standards.length);
		assertEquals("C1 (Methane)", standards[0]);
		assertEquals("C49 (Nonatetracontane)", standards[48]);
		assertEquals("C99 (Nonanonacontane)", standards[98]);
	}
}