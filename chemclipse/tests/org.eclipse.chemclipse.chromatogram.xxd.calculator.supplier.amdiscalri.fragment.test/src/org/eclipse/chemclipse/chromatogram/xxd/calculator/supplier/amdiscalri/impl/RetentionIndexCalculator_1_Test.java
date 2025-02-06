/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl;

import junit.framework.TestCase;

public class RetentionIndexCalculator_1_Test extends TestCase {

	public void test1() {

		String[] standards = RetentionIndexCalculator.getStandards();
		assertEquals(99, standards.length);
		assertEquals("C1 (Methane)", standards[0]);
		assertEquals("C49 (Nonatetracontane)", standards[48]);
		assertEquals("C99 (Nonanonacontane)", standards[98]);
	}
}