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
package org.eclipse.chemclipse.xxd.converter.supplier.mzml.converter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;

public class MagicNumberMatcherIndexed extends AbstractMagicNumberMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isValidFormat = false;
		try (FileReader fileReader = new FileReader(file)) {
			final char[] charBuffer = new char[256];
			fileReader.read(charBuffer);
			final String header = new String(charBuffer);
			if(header.contains("<indexedmzML")) {
				isValidFormat = true;
			}
		} catch(IOException e) {
			// fail silently
		}
		return isValidFormat;
	}
}
