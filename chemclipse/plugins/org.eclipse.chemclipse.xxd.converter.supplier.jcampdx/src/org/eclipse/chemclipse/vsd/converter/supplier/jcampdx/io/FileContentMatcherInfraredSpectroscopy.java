/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.vsd.converter.supplier.jcampdx.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;

public class FileContentMatcherInfraredSpectroscopy extends AbstractFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			for(int i = 0; i < 10; i++) {
				String line = bufferedReader.readLine().trim();
				if(line.startsWith("##DATA TYPE") && line.contains("INFRARED SPECTRUM")) {
					return true;
				}
			}
		} catch(IOException e) {
			return false;
		}
		return false;
	}
}
