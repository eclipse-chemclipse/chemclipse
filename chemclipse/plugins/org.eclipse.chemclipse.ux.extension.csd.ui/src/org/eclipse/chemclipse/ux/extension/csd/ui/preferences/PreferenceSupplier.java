/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.csd.ui.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.csd.ui.Activator;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final int MIN_X_OFFSET = 0; // = 0.0 minutes
	public static final int MAX_X_OFFSET = 6000000; // = 100.0 minutes;

	public static final String P_OVERLAY_X_OFFSET = "overlayXOffset";
	public static final int DEF_OVERLAY_X_OFFSET = 0;
	public static final String P_OVERLAY_Y_OFFSET = "overlayYOffset";
	public static final int DEF_OVERLAY_Y_OFFSET = 0;
	public static final String P_PATH_OPEN_CHROMATOGRAMS = "pathOpenChromatograms";
	public static final String DEF_PATH_OPEN_CHROMATOGRAMS = "";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_OVERLAY_X_OFFSET, DEF_OVERLAY_X_OFFSET);
		putDefault(P_OVERLAY_Y_OFFSET, DEF_OVERLAY_Y_OFFSET);
		putDefault(P_PATH_OPEN_CHROMATOGRAMS, DEF_PATH_OPEN_CHROMATOGRAMS);
	}

	public static String getPathOpenChromatograms() {

		return INSTANCE().get(P_PATH_OPEN_CHROMATOGRAMS);
	}

	public static void setPathOpenChromatograms(String value) {

		INSTANCE().set(P_PATH_OPEN_CHROMATOGRAMS, value);
	}
}
