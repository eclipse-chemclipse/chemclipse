/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.swt.ui.preferences;

import org.eclipse.chemclipse.msd.swt.ui.Activator;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	/*
	 * UI only settings.
	 */
	public static final String P_PATH_MASS_SPECTRUM_LIBRARIES = "pathMassSpectrumLibraries";
	public static final String DEF_PATH_MASS_SPECTRUM_LIBRARIES = "";

	public static final String P_LIBRARY_MSD_LIMIT_SORTING = "libraryMSDLimitSorting";
	public static final int DEF_LIBRARY_MSD_LIMIT_SORTING = 10000;
	public static final int MIN_LIBRARY_MSD_LIMIT_SORTING = 500;
	public static final int MAX_LIBRARY_MSD_LIMIT_SORTING = 30000;

	public static final String P_SHOW_MASS_SPECTRUM_SELECTION_COMBO = "showMassSpectrumSelectionCombo";
	public static final boolean DEF_SHOW_MASS_SPECTRUM_SELECTION_COMBO = false;

	public static final String P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR = "massSpectrumShowMethodsToolbar";
	public static final boolean DEF_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR = false;

	public static final String P_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY = "massSpectrumShowRelativeIntensity";
	public static final boolean DEF_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY = false;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_PATH_MASS_SPECTRUM_LIBRARIES, DEF_PATH_MASS_SPECTRUM_LIBRARIES);
		putDefault(P_LIBRARY_MSD_LIMIT_SORTING, Integer.toString(DEF_LIBRARY_MSD_LIMIT_SORTING));
		putDefault(PreferenceSupplier.P_SHOW_MASS_SPECTRUM_SELECTION_COMBO, PreferenceSupplier.DEF_SHOW_MASS_SPECTRUM_SELECTION_COMBO);
		putDefault(PreferenceSupplier.P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR, PreferenceSupplier.DEF_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR);
		putDefault(PreferenceSupplier.P_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY, PreferenceSupplier.DEF_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY);
	}

	public static String getPathMassSpectrumLibraries() {

		return INSTANCE().get(P_PATH_MASS_SPECTRUM_LIBRARIES, DEF_PATH_MASS_SPECTRUM_LIBRARIES);
	}

	public static void setPathMassSpectrumLibraries(String pathMassSpectrumLibraries) {

		INSTANCE().put(P_PATH_MASS_SPECTRUM_LIBRARIES, pathMassSpectrumLibraries);
	}

	public static int getLibraryMSDLimitSorting() {

		return INSTANCE().getInteger(P_LIBRARY_MSD_LIMIT_SORTING, DEF_LIBRARY_MSD_LIMIT_SORTING);
	}

	public static boolean isSelectionComboVisible() {

		return INSTANCE().getBoolean(P_SHOW_MASS_SPECTRUM_SELECTION_COMBO);
	}

	public static boolean isMethodToolbarVisible() {

		return INSTANCE().getBoolean(P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR);
	}

	public static boolean isRelativeIntensityAxisVisible() {

		return INSTANCE().getBoolean(P_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY);
	}
}