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
	private final String[] directoryExtensions = MethodConverter.getDirectoryExtensions();

	@Override
	public boolean accept(File dir, String name) {

		return isMethod(new File(dir, name), fileExtensions, directoryExtensions);
	}

	/**
	 * Returns whether the given file is a method, either a method file or a method directory.
	 */
	public static boolean isMethod(File file) {

		return isMethod(file, MethodConverter.getFileExtensions(), MethodConverter.getDirectoryExtensions());
	}

	private static boolean isMethod(File file, String[] fileExtensions, String[] directoryExtensions) {

		/*
		 * A method is stored either as a single file or, if the converter declares a
		 * directory extension, as a directory, e.g. Agilent style ".D" containers.
		 */
		if(file.isDirectory()) {
			return matchesExtension(file.getName(), directoryExtensions);
		}
		return file.isFile() && matchesExtension(file.getName(), fileExtensions);
	}

	private static boolean matchesExtension(String name, String[] extensions) {

		String fileName = name.toLowerCase();
		for(String extension : extensions) {
			if(!extension.isEmpty() && fileName.endsWith(extension.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
