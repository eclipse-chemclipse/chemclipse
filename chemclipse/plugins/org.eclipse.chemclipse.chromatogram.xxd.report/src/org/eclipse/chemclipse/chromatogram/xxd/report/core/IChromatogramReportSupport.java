/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.report.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.report.exceptions.NoReportSupplierAvailableException;

public interface IChromatogramReportSupport {

	/**
	 * Returns the report extensions which are actually registered at the
	 * chromatogram report extension point.<br/>
	 * The report extensions are the specific chromatogram report file extensions.
	 */
	String[] getReportExtensions() throws NoReportSupplierAvailableException;

	/**
	 * Returns the report names which are actually registered at the
	 * chromatogram report extension point.<br/>
	 * The report names are the specific chromatogram report names to be displayed
	 * for example in the SWT FileDialog.
	 */
	String[] getFilterNames() throws NoReportSupplierAvailableException;

	/**
	 * Returns the id of the selected report name.<br/>
	 * The id of the selected report is used to determine which report should
	 * be used to import or export the chromatogram.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getReportSupplierId(int index) throws NoReportSupplierAvailableException;

	/**
	 * Returns the report id e.g. "org.eclipse.chemclipse.chromatogram.xxd.report.supplier.peaks" available in the list defined by its name, e.g. "Peak Report (*.pdf)".
	 * If more reports with the given name are stored, the first match will be returned.
	 */
	String getReportSupplierId(String name) throws NoReportSupplierAvailableException;

	/**
	 * Returns the list of all available report suppliers.<br/>
	 * RATHER USE OTHER METHODS THAN THIS!
	 */
	List<IChromatogramReportSupplier> getReportSupplier();

	/**
	 * Returns the supplier with the given id.<br/>
	 * If no supplier with the given id is available, throw an exception.
	 */
	IChromatogramReportSupplier getReportSupplier(String id) throws NoReportSupplierAvailableException;

	List<String> getAvailableProcessorIds() throws NoReportSupplierAvailableException;
}
