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
package org.eclipse.chemclipse.msd.converter.internal.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.chemclipse.converter.support.FileExtensionCompiler;
import org.junit.Test;

public class FileExtensionCompiler_2_Test {

	private FileExtensionCompiler fileExtensionCompiler;

	@Test
	public void testFileExtensionCompiler_1() {

		fileExtensionCompiler = new FileExtensionCompiler("", true);
		assertEquals("*", fileExtensionCompiler.getCompiledFileExtension());
	}

	@Test
	public void testFileExtensionCompiler_2() {

		fileExtensionCompiler = new FileExtensionCompiler("", false);
		assertEquals("*", fileExtensionCompiler.getCompiledFileExtension());
	}

	@Test
	public void testFileExtensionCompiler_3() {

		fileExtensionCompiler = new FileExtensionCompiler(null, true);
		assertEquals("*", fileExtensionCompiler.getCompiledFileExtension());
	}

	@Test
	public void testFileExtensionCompiler_4() {

		fileExtensionCompiler = new FileExtensionCompiler(null, false);
		assertEquals("*", fileExtensionCompiler.getCompiledFileExtension());
	}
}
