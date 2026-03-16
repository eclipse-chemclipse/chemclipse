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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.TestPathHelper;
import org.junit.jupiter.api.Test;

public class MagicNumberMatcher_5_ITest {

	private static final byte[] MAGIC_CODE = new byte[]{(byte)0xF0, (byte)0xA7};

	private class MagicNumberMatcher extends AbstractMagicNumberMatcher {

		@Override
		public boolean checkFileFormat(File file) {

			return checkMagicCode(file, MAGIC_CODE);
		}
	}

	private IMagicNumberMatcher magicNumberMatcher = new MagicNumberMatcher();

	@Test
	public void test1() throws IOException {

		File file = new File(TestPathHelper.TESTFILE_IMPORT_BIN_TEST);
		assertTrue(magicNumberMatcher.checkFileFormat(file));
	}
}
