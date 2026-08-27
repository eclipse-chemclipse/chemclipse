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
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.converter;

import java.io.File;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;

public class MagicNumberMatcher extends AbstractMagicNumberMatcher {

	private static final byte[] MAGIC_CODE_ZIP = new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04};

	@Override
	public boolean checkFileFormat(File file) {

		return checkFileExtension(file, ".mzpeak") && checkMagicCode(file, MAGIC_CODE_ZIP);
	}
}
