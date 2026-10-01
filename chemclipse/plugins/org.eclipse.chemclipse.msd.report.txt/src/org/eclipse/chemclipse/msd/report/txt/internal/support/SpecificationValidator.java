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
package org.eclipse.chemclipse.msd.report.txt.internal.support;

import java.io.File;

public class SpecificationValidator {

	private static final String DEFAULT_FILE_NAME = "MassSpectraReport.txt";
	private static final String EXTENSION = ".txt";

	/**
	 * Use only static methods.
	 */
	private SpecificationValidator() {

	}

	/**
	 * Validates the given file.<br/>
	 *
	 * @param file
	 */
	public static File validateSpecification(File file) {

		if(file == null) {
			return null;
		}
		/*
		 * Validate
		 */
		File validFile;
		String path = file.getAbsolutePath().toLowerCase();
		if(file.isDirectory()) {
			validFile = new File(file.getAbsolutePath() + File.separator + DEFAULT_FILE_NAME);
		} else {
			if(path.endsWith(".")) {
				validFile = new File(file.getAbsolutePath() + "txt");
			} else if(!path.endsWith(EXTENSION)) {
				validFile = new File(file.getAbsolutePath() + EXTENSION);
			} else {
				validFile = file;
			}
		}
		return validFile;
	}
}
