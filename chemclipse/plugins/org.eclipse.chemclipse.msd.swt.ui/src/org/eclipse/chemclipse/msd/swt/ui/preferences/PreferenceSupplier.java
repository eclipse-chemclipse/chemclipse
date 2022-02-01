/*******************************************************************************
 * Copyright (c) 2015, 2022 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.swt.ui.Activator;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class PreferenceSupplier implements IPreferenceSupplier {

	private static final Logger logger = Logger.getLogger(PreferenceSupplier.class);
	/*
	 * UI only settings.
	 */
	public static final String P_FILTER_MASS_SPECTRUM = "filterMassSpectrum";
	public static final boolean DEF_FILTER_MASS_SPECTRUM = false;
	public static final String P_FILTER_LIMIT_IONS = "filterLimitIons";
	public static final int DEF_FILTER_LIMIT_IONS = 8000;
	public static final int MIN_FILTER_LIMIT_IONS = 10;
	public static final int MAX_FILTER_LIMIT_IONS = 1000000;
	//
	public static final String P_PATH_MASS_SPECTRUM_LIBRARIES = "pathMassSpectrumLibraries";
	public static final String DEF_PATH_MASS_SPECTRUM_LIBRARIES = "";
	//
	public static final String P_LIBRARY_MSD_LIMIT_SORTING = "libraryMSDLimitSorting";
	public static final int DEF_LIBRARY_MSD_LIMIT_SORTING = 10000;
	public static final int MIN_LIBRARY_MSD_LIMIT_SORTING = 500;
	public static final int MAX_LIBRARY_MSD_LIMIT_SORTING = 100000;
	//
	private static IPreferenceSupplier preferenceSupplier;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public IScopeContext getScopeContext() {

		return InstanceScope.INSTANCE;
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public Map<String, String> getDefaultValues() {

		Map<String, String> defaultValues = new HashMap<String, String>();
		defaultValues.put(P_FILTER_MASS_SPECTRUM, Boolean.toString(DEF_FILTER_MASS_SPECTRUM));
		defaultValues.put(P_FILTER_LIMIT_IONS, Integer.toString(DEF_FILTER_LIMIT_IONS));
		defaultValues.put(P_PATH_MASS_SPECTRUM_LIBRARIES, DEF_PATH_MASS_SPECTRUM_LIBRARIES);
		defaultValues.put(P_LIBRARY_MSD_LIMIT_SORTING, Integer.toString(DEF_LIBRARY_MSD_LIMIT_SORTING));
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static String getPathMassSpectrumLibraries() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.get(P_PATH_MASS_SPECTRUM_LIBRARIES, DEF_PATH_MASS_SPECTRUM_LIBRARIES);
	}

	public static void setPathMassSpectrumLibraries(String pathMassSpectrumLibraries) {

		try {
			IEclipsePreferences preferences = INSTANCE().getPreferences();
			preferences.put(P_PATH_MASS_SPECTRUM_LIBRARIES, pathMassSpectrumLibraries);
			preferences.flush();
		} catch(BackingStoreException e) {
			logger.warn(e);
		}
	}

	public static int getLibraryMSDLimitSorting() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_LIBRARY_MSD_LIMIT_SORTING, DEF_LIBRARY_MSD_LIMIT_SORTING);
	}
}
