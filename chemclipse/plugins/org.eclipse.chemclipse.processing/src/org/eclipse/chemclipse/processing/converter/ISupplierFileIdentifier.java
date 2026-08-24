/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - support for new E4 launching API
 *******************************************************************************/
package org.eclipse.chemclipse.processing.converter;

import java.io.File;
import java.util.Collection;

public interface ISupplierFileIdentifier extends SupplierContext {

	String TYPE_MSD = "MSD";
	String TYPE_CSD = "CSD";
	String TYPE_WSD = "WSD";
	String TYPE_DSD = "DSD"; // DNA Sequencing
	String TYPE_TSD = "TSD";
	String TYPE_VSD = "VSD";
	String TYPE_FSD = "FSD";
	String TYPE_NMR = "NMR";
	String TYPE_DATABASE_MSD = "DATABASE_MSD";
	String TYPE_DATABASE_CSD = "DATABASE_CSD";
	String TYPE_DATABASE_WSD = "DATABASE_WSD";
	String TYPE_DATABASE_NMR = "DATABASE_NMR";
	String TYPE_PEAKLIST_MSD = "PEAKLIST MSD"; // e.g. Matlab Parafac Peaklist *.mpl
	String TYPE_SCAN_MSD = "SCAN_MSD"; // MALDI-TOF
	String TYPE_SCAN_VSD = "SCAN_VSD"; // FT-IR
	String TYPE_SCAN_WSD = "SCAN_WSD"; // UV/Vis
	String TYPE_SCAN_FSD = "SCAN_FSD"; // Fluorescence
	String TYPE_CAL = "CAL"; // RI Calibration
	String TYPE_OBJ = "OBJ"; // Batch Job
	String TYPE_PCR = "PCR"; // Polymerase Chain Reaction
	String TYPE_SEQ = "SEQ"; // Sequence
	String TYPE_MTH = "MTH"; // Method
	String TYPE_QDB = "QDB"; // Quantitation Database

	/**
	 * If the following term is supplied:
	 * .r##
	 * the it returns:
	 * .*\\.r[0-9][0-9]
	 */
	static String getExtensionMatcher(String supplierExtension) {

		String extensionMatcher = supplierExtension.replaceAll(ISupplier.WILDCARD_NUMBER, "[0-9]");
		return extensionMatcher.replace(".", ".*\\.");
	}

	/**
	 * Returns the identifier type.
	 * 
	 * @return String
	 */
	String getType();

	/**
	 * Check whether the file is a supplied file type or not.
	 * 
	 * @param file
	 * @return boolean
	 */
	default boolean isSupplierFile(File file) {

		return !getSuppliers(file).isEmpty();
	}

	/**
	 * Get all matching suppliers for the given file the list might be ambiguous, then the only way might be to try out each supplier to convert the file
	 * 
	 * @param file
	 * @return
	 */
	Collection<ISupplier> getSuppliers(File file);

	/**
	 * Checks if a file can be handled by the supplier.
	 *
	 * @param file
	 * @return boolean
	 */
	default boolean isMatch(File file) {

		return false;
	}
}
