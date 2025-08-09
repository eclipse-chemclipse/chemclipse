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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.converter.TestPathHelper;
import org.junit.Test;

public class MagicNumberMatcher_10_ITest {

	private static final Map<Integer, Byte> INDEX_MAP = new HashMap<>();
	static {
		INDEX_MAP.put(0, (byte)0xF0);
		INDEX_MAP.put(5, (byte)0x9F);
		INDEX_MAP.put(7, (byte)0x3B);
	}

	private class MagicNumberMatcher extends AbstractMagicNumberMatcher implements IMagicNumberMatcher {

		@Override
		public boolean checkFileFormat(File file) {

			return checkPattern(file, 8, INDEX_MAP);
		}
	}

	private IMagicNumberMatcher magicNumberMatcher = new MagicNumberMatcher();

	@Test
	public void test1() throws IOException {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_BIN_TEST));
		assertTrue(magicNumberMatcher.checkFileFormat(file));
	}
}
