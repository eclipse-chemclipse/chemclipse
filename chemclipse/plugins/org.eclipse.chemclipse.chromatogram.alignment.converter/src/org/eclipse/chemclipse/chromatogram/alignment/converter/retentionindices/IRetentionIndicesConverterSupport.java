/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.alignment.converter.retentionindices;

import java.io.File;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.alignment.converter.exceptions.NoRetentionIndicesConverterAvailableException;

public interface IRetentionIndicesConverterSupport {

	/**
	 * Returns the filter extension which are actually registered at the
	 * retention indices converter extension point.<br/>
	 * The filter extension are the specific retention indices file extensions.
	 * AMDIS has for example an filter extension (.cal) which represents a
	 * retention indices file.
	 */
	String[] getFilterExtensions() throws NoRetentionIndicesConverterAvailableException;

	/**
	 * Returns the filter names which are actually registered at the retention
	 * indices converter extension point.<br/>
	 * The filter names are the specific retention indices file names to be
	 * displayed for example in the SWT FileDialog. AMDIS has for example an
	 * filter name "AMDIS RI Calibration Data (.cal)".
	 */
	String[] getFilterNames() throws NoRetentionIndicesConverterAvailableException;

	/**
	 * Returns the id of the selected filter name.<br/>
	 * The id of the selected filter is used to determine which converter should
	 * be used to import or export the retention indices.<br/>
	 * Be aware of that the first index is 0. It is a 0-based index.
	 */
	String getConverterId(int index) throws NoRetentionIndicesConverterAvailableException;

	/**
	 * Returns an ArrayList with all available converter ids for the given file.<br/>
	 * If the file ends with "*.cal" all converter ids which can convert files
	 * ending with "*.cal" will be returned.<br/>
	 */
	List<String> getAvailableConverterIds(File retentionIndices) throws NoRetentionIndicesConverterAvailableException;
}
