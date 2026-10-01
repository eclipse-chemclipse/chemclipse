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
package org.eclipse.chemclipse.msd.report.model;

public interface IMassSpectraReportSupplierEntry {

	/**
	 * Returns the report folder or file which shall be appended.
	 *
	 * @return String
	 */
	String getReportFolderOrFile();

	/**
	 * Returns the report supplier id.
	 *
	 * @return String
	 */
	String getReportSupplierId();
}
