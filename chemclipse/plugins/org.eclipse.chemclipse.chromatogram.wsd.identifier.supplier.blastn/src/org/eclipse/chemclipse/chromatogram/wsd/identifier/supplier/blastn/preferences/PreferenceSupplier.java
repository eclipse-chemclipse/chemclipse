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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_BLAST_EXECUTABLE_PATH = "blastExecutablePath";
	public static final String DEF_BLAST_EXECUTABLE_PATH = "";

	public static final String P_BLAST_DATABASE_PATH = "blastDatabasePath";
	public static final String DEF_BLAST_DATABASE_PATH = "";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_BLAST_EXECUTABLE_PATH, DEF_BLAST_EXECUTABLE_PATH);
		putDefault(P_BLAST_DATABASE_PATH, DEF_BLAST_DATABASE_PATH);
	}

	public static String getExecutableFolder() {

		return INSTANCE().get(P_BLAST_EXECUTABLE_PATH, DEF_BLAST_EXECUTABLE_PATH);
	}

	public static String getDatabaseFolder() {

		return INSTANCE().get(P_BLAST_DATABASE_PATH, DEF_BLAST_DATABASE_PATH);
	}
}