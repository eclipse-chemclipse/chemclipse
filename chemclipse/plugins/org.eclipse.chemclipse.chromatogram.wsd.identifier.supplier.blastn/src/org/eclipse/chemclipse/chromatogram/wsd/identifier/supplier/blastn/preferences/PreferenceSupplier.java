/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.LocalIdentifierSettings;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_DATABASE = "database";
	public static final String DEF_DATABASE = "16S_ribosomal_RNA";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_DATABASE, DEF_DATABASE);
	}

	public static String getDatabase() {

		return INSTANCE().get(P_DATABASE, DEF_DATABASE);
	}

	public static IChromatogramIdentifierSettings getIdentifierSettings() {

		LocalIdentifierSettings settings = new LocalIdentifierSettings();
		settings.setDatabase(getDatabase());
		initialize(settings);
		return settings;
	}

	private static void initialize(LocalIdentifierSettings settings) {

		settings.setDatabase(INSTANCE().get(P_DATABASE, DEF_DATABASE));
	}
}