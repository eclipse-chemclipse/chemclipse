/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.support;

import java.io.File;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.settings.ApplicationSettings;

// TODO JUnit
public class DatabasePathSupport {

	private static final Logger logger = Logger.getLogger(DatabasePathSupport.class);
	public static final String PLUGIN_NAME = "org.eclipse.chemclipse.msd.identifier";
	public static final String EMBEDDED_DATABASES = "embeddedDatabases";

	/**
	 * Returns the user specific file object (directory) where the database
	 * instances can be stored.
	 * 
	 * @return File
	 */
	public static File getDatabaseStoragePath() {

		File file = new File(ApplicationSettings.getSettingsDirectory().getAbsolutePath() + File.separator + PLUGIN_NAME + File.separator + EMBEDDED_DATABASES);
		/*
		 * Create the directory if it not exists.
		 */
		if(!file.exists()) {
			if(!file.mkdirs()) {
				logger.warn("The temporarily database file directory could not be created: " + file.getAbsolutePath());
			}
		}
		return file;
	}
}
