/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.core;

import java.io.File;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;

public class MagicNumberMatcher extends AbstractMagicNumberMatcher implements IMagicNumberMatcher {

	private static final byte[] MAGIC_CODE = new byte[]{(byte)'.', (byte)'s', (byte)'c', (byte)'f'};

	@Override
	public boolean checkFileFormat(File file) {

		if(!checkFileExtension(file, ".scf")) {
			return false;
		}
		return checkMagicCode(file, MAGIC_CODE);
	}
}
