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
import org.eclipse.swtchart.extensions.linecharts.ICompressionSupport;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	/*
	 * UI only settings.
	 */
	public static final String P_PATH_MASS_SPECTRUM_LIBRARIES = "pathMassSpectrumLibraries";
	public static final String DEF_PATH_MASS_SPECTRUM_LIBRARIES = "";

	public static final String P_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE = "profileMassSpectrumChartCompressionType";
	public static final String DEF_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE = ICompressionSupport.COMPRESSION_AUTO;

	public static final String P_SHOW_MASS_SPECTRUM_SELECTION_COMBO = "showMassSpectrumSelectionCombo";
	public static final boolean DEF_SHOW_MASS_SPECTRUM_SELECTION_COMBO = false;

	public static final String P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR = "massSpectrumShowMethodsToolbar";
	public static final boolean DEF_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR = false;

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
		putDefault(P_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE, DEF_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE);
		putDefault(PreferenceSupplier.P_SHOW_MASS_SPECTRUM_SELECTION_COMBO, PreferenceSupplier.DEF_SHOW_MASS_SPECTRUM_SELECTION_COMBO);
		putDefault(PreferenceSupplier.P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR, PreferenceSupplier.DEF_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR);
	}

	public static String getPathMassSpectrumLibraries() {

		return INSTANCE().get(P_PATH_MASS_SPECTRUM_LIBRARIES, DEF_PATH_MASS_SPECTRUM_LIBRARIES);
	}

	public static String getProfileMassSpectrumChartCompression() {

		return INSTANCE().get(P_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE, DEF_PROFILE_MASS_SPECTRUM_CHART_COMPRESSION_TYPE);
	}

	public static void setPathMassSpectrumLibraries(String pathMassSpectrumLibraries) {

		INSTANCE().put(P_PATH_MASS_SPECTRUM_LIBRARIES, pathMassSpectrumLibraries);
	}

	public static boolean isSelectionComboVisible() {

		return INSTANCE().getBoolean(P_SHOW_MASS_SPECTRUM_SELECTION_COMBO);
	}

	public static boolean isMethodToolbarVisible() {

		return INSTANCE().getBoolean(P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR);
	}
}