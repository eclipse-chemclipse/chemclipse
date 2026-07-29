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
package org.eclipse.chemclipse.converter.methods;

import java.io.File;
import java.io.FilenameFilter;

public class MethodFilenameFilter implements FilenameFilter {

	private final String[] fileExtensions = MethodConverter.getFileExtensions();

	@Override
	public boolean accept(File dir, String name) {

		return isMethodFile(name, fileExtensions);
	}

	public static boolean isMethodFile(String name) {

		return isMethodFile(name, MethodConverter.getFileExtensions());
	}

	private static boolean isMethodFile(String name, String[] fileExtensions) {

		String fileName = name.toLowerCase();
		for(String fileExtension : fileExtensions) {
			if(!fileExtension.isEmpty() && fileName.endsWith(fileExtension.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
