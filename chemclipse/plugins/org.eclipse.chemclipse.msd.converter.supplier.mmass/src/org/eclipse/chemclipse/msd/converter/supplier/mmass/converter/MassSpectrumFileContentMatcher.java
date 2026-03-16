/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.mmass.converter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;

public class MassSpectrumFileContentMatcher extends AbstractFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isValidFormat = false;
		try (FileReader fileReader = new FileReader(file)) {
			final char[] charBuffer = new char[60];
			fileReader.read(charBuffer);
			final String header = new String(charBuffer);
			if(header.contains("<mSD version")) {
				isValidFormat = true;
			}
		} catch(IOException e) {
			// fail silently
		}
		return isValidFormat;
	}
}
