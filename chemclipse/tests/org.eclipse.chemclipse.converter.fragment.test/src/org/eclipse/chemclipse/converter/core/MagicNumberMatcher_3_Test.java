/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.converter.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class MagicNumberMatcher_3_Test {

	private class MagicNumberMatcher extends AbstractMagicNumberMatcher {

		@Override
		public boolean checkFileFormat(File file) {

			return checkFileExtension(file, ".txt", true);
		}
	}

	private IMagicNumberMatcher magicNumberMatcher = new MagicNumberMatcher();

	@Test
	public void test1() {

		File file = new File("HelloWorld.txt");
		assertTrue(magicNumberMatcher.checkFileFormat(file));
	}

	@Test
	public void test2() {

		File file = new File("HelloWorld.TXT");
		assertFalse(magicNumberMatcher.checkFileFormat(file));
	}

	@Test
	public void test3() {

		File file = new File("HelloWorld.png");
		assertFalse(magicNumberMatcher.checkFileFormat(file));
	}

	@Test
	public void test4() {

		File file = new File("HelloWorld.PNG");
		assertFalse(magicNumberMatcher.checkFileFormat(file));
	}
}
