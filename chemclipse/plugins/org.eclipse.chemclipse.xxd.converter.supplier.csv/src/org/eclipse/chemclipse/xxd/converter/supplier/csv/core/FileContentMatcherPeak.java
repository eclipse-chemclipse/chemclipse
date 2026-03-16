/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring package name
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.csv.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.csv.io.core.CSVPeakConverter;

public class FileContentMatcherPeak extends AbstractFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		try {
			try (CSVParser parser = CSVParser.parse(new FileReader(file), CSVFormat.EXCEL.builder().setHeader().get())) {
				String[] array = parser.getHeaderMap().keySet().toArray(new String[0]);
				return Arrays.equals(array, CSVPeakConverter.HEADERS);
			}
		} catch(IOException e) {
			// ignore
		}
		return false;
	}
}