/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.core;

import java.io.File;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;

public class MagicNumberMatcher extends AbstractMagicNumberMatcher {

	private static final byte[] MAGIC_CODE = new byte[]{(byte)'.', (byte)'s', (byte)'c', (byte)'f'};

	@Override
	public boolean checkFileFormat(File file) {

		if(!checkFileExtension(file, ".scf")) {
			return false;
		}
		return checkMagicCode(file, MAGIC_CODE);
	}
}
