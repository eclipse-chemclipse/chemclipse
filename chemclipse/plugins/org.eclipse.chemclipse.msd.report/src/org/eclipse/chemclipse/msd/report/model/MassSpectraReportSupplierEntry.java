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

public class MassSpectraReportSupplierEntry implements IMassSpectraReportSupplierEntry {

	private String reportFolderOrFile = "";
	private String reportSupplierId = "";

	/**
	 * Set the output file path and the report supplier id.
	 */
	public MassSpectraReportSupplierEntry(String reportFolderOrFile, String reportSupplierId) {

		if(reportFolderOrFile != null && reportSupplierId != null) {
			this.reportFolderOrFile = reportFolderOrFile;
			this.reportSupplierId = reportSupplierId;
		}
	}

	@Override
	public String getReportFolderOrFile() {

		return reportFolderOrFile;
	}

	@Override
	public String getReportSupplierId() {

		return reportSupplierId;
	}
}
