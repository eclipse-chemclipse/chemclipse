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
package org.eclipse.chemclipse.msd.report.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.report.core.IMassSpectraReportSupplier;
import org.eclipse.chemclipse.msd.report.core.MassSpectraReports;
import org.eclipse.chemclipse.msd.report.exceptions.NoReportSupplierAvailableException;
import org.eclipse.chemclipse.msd.report.model.IMassSpectraReportSupplierEntry;

public class ReportSupplierTypeSupport {

	private static final Logger logger = Logger.getLogger(ReportSupplierTypeSupport.class);
	public static final String NOT_AVAILABLE = "n.a.";

	public String getReportName(IMassSpectraReportSupplierEntry entry) {

		String reportName = NOT_AVAILABLE;
		try {
			IMassSpectraReportSupplier reportSupplier = MassSpectraReports.getMassSpectraReportSupplierSupport().getReportSupplier(entry.getReportSupplierId());
			reportName = reportSupplier.getReportName();
		} catch(NoReportSupplierAvailableException e) {
			logger.warn(e);
		}
		return reportName;
	}
}
