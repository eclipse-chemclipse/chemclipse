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
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.csv.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.io.core.ChromatogramReader;

public class FileContentMatcherChromatogram extends AbstractFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		try {
			return ChromatogramReader.isValidFileFormat(file);
		} catch(IOException e) {
			// failed to parse
			return false;
		}
	}
}