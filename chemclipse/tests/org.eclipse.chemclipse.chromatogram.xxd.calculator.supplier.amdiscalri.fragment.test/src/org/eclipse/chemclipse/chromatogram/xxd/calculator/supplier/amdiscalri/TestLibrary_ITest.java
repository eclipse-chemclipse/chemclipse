/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class TestLibrary_ITest {

	@Test
	public void test1() {

		File file = new File(PathResolver.getAbsolutePath(PathResolver.ALKANES));
		assertTrue(file.exists());
	}
}