/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - match *.dx mass spectra
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.jcampdx.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;

public class MagicNumberMatcherChromatogram extends AbstractMagicNumberMatcher {

	private static final String DATA_TYPE = "##DATA TYPE";
	private static final String MASS_SPECTRUM = "MASS SPECTRUM";

	@Override
	public boolean checkFileFormat(File file) {

		if(checkFileExtension(file, ".JDX") || checkFileExtension(file, ".JDL")) {
			return true;
		}
		/*
		 * *.dx is shared with FT-IR, UV-Vis and NMR, so the data type decides.
		 */
		return checkFileExtension(file, ".dx") && isMassSpectrum(file);
	}

	private boolean isMassSpectrum(File file) {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			for(int i = 0; i < 10 && (line = bufferedReader.readLine()) != null; i++) {
				line = line.trim();
				if(line.startsWith(DATA_TYPE)) {
					return line.toUpperCase().contains(MASS_SPECTRUM);
				}
			}
		} catch(IOException e) {
			return false;
		}
		return false;
	}
}
