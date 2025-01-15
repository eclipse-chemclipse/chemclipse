/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_MAGNIFICATION_FACTOR = "magnificationFactor";
	public static final int DEF_MAGNIFICATION_FACTOR = 1;
	public static final int DEF_MAGNIFICATION_FACTOR_MIN = 1;
	public static final int DEF_MAGNIFICATION_FACTOR_MAX = 50;
	//
	public static final String P_USE_PROFILE_MASS_SPECTRUM_VIEW = "useProfileMassSpectrumView";
	public static final boolean DEF_USE_PROFILE_MASS_SPECTRUM_VIEW = false;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(PreferenceSupplier.P_MAGNIFICATION_FACTOR, PreferenceSupplier.DEF_MAGNIFICATION_FACTOR);
		putDefault(PreferenceSupplier.P_USE_PROFILE_MASS_SPECTRUM_VIEW, PreferenceSupplier.DEF_USE_PROFILE_MASS_SPECTRUM_VIEW);
	}

	public static boolean useProfileMassSpectrumView() {

		return INSTANCE().getBoolean(P_USE_PROFILE_MASS_SPECTRUM_VIEW);
	}
}