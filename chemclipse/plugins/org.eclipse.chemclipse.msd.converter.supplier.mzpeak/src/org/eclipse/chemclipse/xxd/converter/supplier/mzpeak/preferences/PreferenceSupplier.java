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
package org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_IMPORT_PROFILE_SPECTRA = "importProfileSpectra"; // $NON-NLS-1$
	public static final boolean DEF_IMPORT_PROFILE_SPECTRA = false;

	public static final String P_IMPORT_CENTROIDED_SPECTRA = "importCentroidedSpectra"; // $NON-NLS-1$
	public static final boolean DEF_IMPORT_CENTROIDED_SPECTRA = true;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_IMPORT_PROFILE_SPECTRA, Boolean.toString(DEF_IMPORT_PROFILE_SPECTRA));
		putDefault(P_IMPORT_CENTROIDED_SPECTRA, Boolean.toString(DEF_IMPORT_CENTROIDED_SPECTRA));
	}

	public static boolean isImportProfileSpectra() {

		return INSTANCE().getBoolean(P_IMPORT_PROFILE_SPECTRA, DEF_IMPORT_PROFILE_SPECTRA);
	}

	public static boolean isImportCentroidedSpectra() {

		return INSTANCE().getBoolean(P_IMPORT_CENTROIDED_SPECTRA, DEF_IMPORT_CENTROIDED_SPECTRA);
	}
}