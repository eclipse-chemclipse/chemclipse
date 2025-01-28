/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - new format
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences;

import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.Activator;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.settings.Format;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final int MIN_COMPRESSION_LEVEL = 0;
	public static final int MAX_COMPRESSION_LEVEL = 9;
	//
	public static final HeaderField DEF_HEADER_FIELD = HeaderField.DATA_NAME;
	/*
	 * .ocb (measurement data container)
	 * Use always the latest version.
	 */
	public static final String P_CHROMATOGRAM_COMPRESSION_LEVEL = "chromatogramCompressionLevel";
	public static final int DEF_CHROMATOGRAM_COMPRESSION_LEVEL = 1; // compromise between file size and write speed
	public static final String P_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY = "chromatogramExportReferencesSeparately";
	public static final boolean DEF_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY = false;
	public static final String P_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD = "chromatogramExportReferencesHeaderField";
	public static final String DEF_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD = DEF_HEADER_FIELD.name();
	//
	public static final String P_FORCE_LOAD_ALTERNATE_DETECTOR = "forceLoadAlternateDetector"; // TODO REMOVE
	public static final boolean DEF_FORCE_LOAD_ALTERNATE_DETECTOR = false;
	//
	public static final String P_USE_SCAN_PROXIES = "useScanProxies"; // TODO CHECK
	public static final boolean DEF_USE_SCAN_PROXIES = false;
	public static final String P_LOAD_SCAN_PROXIES_IN_BACKGROUND = "loadScanProxiesInBackground"; // TODO REMOVE
	public static final boolean DEF_LOAD_SCAN_PROXIES_IN_BACKGROUND = false; // This could lead java.util.ConcurrentModificationException if true
	public static final String P_MIN_BYTES_TO_LOAD_IN_BACKGROUND = "minBytesToLoadInBackground"; // TODO REMOVE
	public static final int DEF_MIN_BYTES_TO_LOAD_IN_BACKGROUND = 2000000; // 2 MB
	/*
	 * .ocm (process method container)
	 * Use always the latest version.
	 */
	public static final String P_METHOD_COMPRESSION_LEVEL = "methodCompressionLevel";
	public static final int DEF_METHOD_COMPRESSION_LEVEL = Format.METHOD_COMPRESSION_LEVEL;
	/*
	 * .ocq (quanititation table container)
	 */
	public static final String P_QUANTITATION_DB_VERSION_SAVE = "quantitationDatabaseVersionSave";
	public static final String DEF_QUANTITATION_DB_VERSION_SAVE = Format.QUANTDB_VERSION_LATEST;
	public static final String P_QUANTITATION_DB_COMPRESSION_LEVEL = "quantitationDatabaseCompressionLevel";
	public static final int DEF_QUANTITATION_DB_COMPRESSION_LEVEL = Format.QUANTDB_COMPRESSION_LEVEL;
	/*
	 * General
	 */
	public static final String P_LIST_PATH_IMPORT = "listPathImport";
	public static final String DEF_LIST_PATH_IMPORT = "";
	public static final String P_LIST_PATH_EXPORT = "listPathExport";
	public static final String DEF_LIST_PATH_EXPORT = "";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getContext().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_CHROMATOGRAM_COMPRESSION_LEVEL, Integer.toString(DEF_CHROMATOGRAM_COMPRESSION_LEVEL));
		putDefault(P_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY, DEF_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY);
		putDefault(P_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD, DEF_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD);
		putDefault(P_FORCE_LOAD_ALTERNATE_DETECTOR, Boolean.toString(DEF_FORCE_LOAD_ALTERNATE_DETECTOR));
		putDefault(P_USE_SCAN_PROXIES, Boolean.toString(DEF_USE_SCAN_PROXIES));
		putDefault(P_LOAD_SCAN_PROXIES_IN_BACKGROUND, Boolean.toString(DEF_LOAD_SCAN_PROXIES_IN_BACKGROUND));
		putDefault(P_MIN_BYTES_TO_LOAD_IN_BACKGROUND, Integer.toString(DEF_MIN_BYTES_TO_LOAD_IN_BACKGROUND));
		//
		putDefault(P_METHOD_COMPRESSION_LEVEL, Integer.toString(DEF_METHOD_COMPRESSION_LEVEL));
		//
		putDefault(P_QUANTITATION_DB_VERSION_SAVE, DEF_QUANTITATION_DB_VERSION_SAVE);
		putDefault(P_QUANTITATION_DB_COMPRESSION_LEVEL, Integer.toString(DEF_QUANTITATION_DB_COMPRESSION_LEVEL));
		//
		putDefault(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
		putDefault(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
	}

	public static int getChromatogramCompressionLevel() {

		return INSTANCE().getInteger(P_CHROMATOGRAM_COMPRESSION_LEVEL, DEF_CHROMATOGRAM_COMPRESSION_LEVEL);
	}

	public static void setChromatogramCompressionLevel(int value) {

		INSTANCE().putInteger(P_CHROMATOGRAM_COMPRESSION_LEVEL, value);
	}

	public static void setChromatogramExportReferencesSeparately(boolean value) {

		INSTANCE().putBoolean(P_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY, value);
	}

	public static boolean isChromatogramExportReferencesSeparately() {

		return INSTANCE().getBoolean(P_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY, DEF_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY);
	}

	public static void setChromatogramExportReferencesHeaderField(HeaderField headerField) {

		INSTANCE().put(P_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD, headerField.name());
	}

	public static HeaderField getChromatogramExportReferencesHeaderField() {

		try {
			return HeaderField.valueOf(INSTANCE().get(P_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD, DEF_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD));
		} catch(Exception e) {
			return DEF_HEADER_FIELD;
		}
	}

	public static int getMethodCompressionLevel() {

		return INSTANCE().getInteger(P_METHOD_COMPRESSION_LEVEL, DEF_METHOD_COMPRESSION_LEVEL);
	}

	public static void setMethodCompressionLevel(int value) {

		INSTANCE().putInteger(P_METHOD_COMPRESSION_LEVEL, value);
	}

	public static void setForceLoadAlternateDetector(boolean forceLoadAlternateDetector) {

		INSTANCE().putBoolean(P_FORCE_LOAD_ALTERNATE_DETECTOR, forceLoadAlternateDetector);
	}

	public static boolean isForceLoadAlternateDetector() {

		return INSTANCE().getBoolean(P_FORCE_LOAD_ALTERNATE_DETECTOR, DEF_FORCE_LOAD_ALTERNATE_DETECTOR);
	}

	public static String getListPathImport() {

		return INSTANCE().get(P_LIST_PATH_IMPORT, DEF_LIST_PATH_IMPORT);
	}

	public static void setListPathImport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_IMPORT, filterPath);
	}

	public static String getListPathExport() {

		return INSTANCE().get(P_LIST_PATH_EXPORT, DEF_LIST_PATH_EXPORT);
	}

	public static void setListPathExport(String filterPath) {

		INSTANCE().put(P_LIST_PATH_EXPORT, filterPath);
	}
}