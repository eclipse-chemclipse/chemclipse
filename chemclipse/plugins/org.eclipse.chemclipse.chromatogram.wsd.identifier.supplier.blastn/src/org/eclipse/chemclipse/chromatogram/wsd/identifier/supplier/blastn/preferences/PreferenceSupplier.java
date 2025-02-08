/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.chromatogram.IChromatogramIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.Activator;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.settings.IdentifierSettings;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_DATABASE = "database";
	public static final String DEF_DATABASE = "16S_ribosomal_RNA";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getContext().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_DATABASE, DEF_DATABASE);
	}

	public static String getDatabase() {

		return INSTANCE().get(P_DATABASE, DEF_DATABASE);
	}

	public static IChromatogramIdentifierSettings getIdentifierSettings() {

		IdentifierSettings settings = new IdentifierSettings();
		settings.setDatabase(getDatabase());
		initialize(settings);
		return settings;
	}

	private static void initialize(IdentifierSettings settings) {

		settings.setDatabase(INSTANCE().get(P_DATABASE, DEF_DATABASE));
	}
}