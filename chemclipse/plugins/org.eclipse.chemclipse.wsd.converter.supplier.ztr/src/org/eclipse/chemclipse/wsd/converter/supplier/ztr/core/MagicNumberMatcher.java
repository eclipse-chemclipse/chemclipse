/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.ztr.core;

import java.io.File;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;

public class MagicNumberMatcher extends AbstractMagicNumberMatcher {

	private static final byte[] MAGIC_CODE = {(byte)0xae, 0x5a, 0x54, 0x52, 0x0d, 0x0a, 0x1a, 0x0a};

	@Override
	public boolean checkFileFormat(File file) {

		if(!checkFileExtension(file, ".ztr")) {
			return false;
		}
		return checkMagicCode(file, MAGIC_CODE);
	}
}
