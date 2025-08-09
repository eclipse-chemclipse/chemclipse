/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MagicNumberMatcher_12_ITest {

	private class MagicNumberMatcher extends AbstractMagicNumberMatcher implements IMagicNumberMatcher {

		@Override
		public boolean checkFileFormat(File file) {

			return checkFileName(file, "(.*\\.)(R|r)([0-9][0-9])");
		}
	}

	private IMagicNumberMatcher magicNumberMatcher = new MagicNumberMatcher();

	@Test
	public void test1() throws IOException {

		File file = new File("test.R01");
		file.createNewFile();
		assertTrue(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}

	@Test
	public void test2() throws IOException {

		File file = new File("test.R02");
		file.createNewFile();
		assertTrue(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}

	@Test
	public void test3() throws IOException {

		File file = new File("test.r01");
		file.createNewFile();
		assertTrue(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}

	@Test
	public void test4() throws IOException {

		File file = new File("test.r02");
		file.createNewFile();
		assertTrue(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}

	@Test
	public void test5() throws IOException {

		File file = new File("test.R0");
		file.createNewFile();
		assertFalse(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}

	@Test
	public void test6() throws IOException {

		File file = new File("test.r0");
		file.createNewFile();
		assertFalse(magicNumberMatcher.checkFileFormat(file));
		file.delete();
	}
}
