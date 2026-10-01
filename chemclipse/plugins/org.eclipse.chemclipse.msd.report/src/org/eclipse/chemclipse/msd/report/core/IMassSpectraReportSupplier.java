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
package org.eclipse.chemclipse.msd.report.core;

import org.eclipse.chemclipse.msd.report.settings.IMassSpectraReportSettings;

public interface IMassSpectraReportSupplier {

	/**
	 * The id of the extension point: e.g.
	 * (org.eclipse.chemclipse.msd.report.supplier.txt)
	 *
	 * @return String
	 */
	String getId();

	/**
	 * A short description of the functionality of the extension point.
	 *
	 * @return String
	 */
	String getDescription();

	/**
	 * The report name that will be shown in the FileDialog.
	 *
	 * @return String
	 */
	String getReportName();

	/**
	 * The file extension, e.g. Mass Spectra (.txt) will be returned.<br/>
	 * If the file extension has a value, it starts in every case with a point.
	 *
	 * @return String
	 */
	String getFileExtension();

	/**
	 * The default file name, e.g. MassSpectraReport.txt.
	 *
	 * @return String
	 */
	String getFileName();

	/**
	 * Returns the settings class or <code>null</code> if no report settings are associated.
	 *
	 * @return Class
	 */
	Class<? extends IMassSpectraReportSettings> getSettingsClass();
}
