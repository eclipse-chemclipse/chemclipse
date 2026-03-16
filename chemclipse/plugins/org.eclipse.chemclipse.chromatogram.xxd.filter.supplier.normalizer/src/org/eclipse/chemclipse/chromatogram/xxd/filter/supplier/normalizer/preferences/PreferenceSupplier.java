/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.normalizer.preferences;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.normalizer.settings.FilterSettings;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_NORMALIZATION_BASE = "normalizationBase";
	public static final float DEF_NORMALIZATION_BASE = 1000.0f;
	public static final float MIN_NORMALIZATION_BASE = 1.0f;
	public static final float MAX_NORMALIZATION_BASE = Float.MAX_VALUE;
	private static IPreferenceSupplier preferenceSupplier = null;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_NORMALIZATION_BASE, Float.toString(DEF_NORMALIZATION_BASE));
	}

	public static FilterSettings getFilterSettings() {

		FilterSettings filterSettings = new FilterSettings();
		filterSettings.setNormalizationBase(INSTANCE().getFloat(P_NORMALIZATION_BASE, DEF_NORMALIZATION_BASE));
		return filterSettings;
	}
}