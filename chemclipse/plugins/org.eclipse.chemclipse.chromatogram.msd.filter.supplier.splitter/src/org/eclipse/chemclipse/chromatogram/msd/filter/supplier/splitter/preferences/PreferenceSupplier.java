/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.preferences;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsHighResMS;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsMSx;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsNominalMS;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsPolarity;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsSIM;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.settings.FilterSettingsTandemMS;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final int MIN_LIMIT_IONS_SIM = 1;
	public static final int MAX_LIMIT_IONS_SIM = Integer.MAX_VALUE;

	public static final String P_LIMIT_IONS_SIM = "limitIonsSIM";
	public static final int DEF_LIMIT_IONS_SIM = 5;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_LIMIT_IONS_SIM, Integer.toString(DEF_LIMIT_IONS_SIM));
	}

	public static FilterSettingsMSx getFilterSettingsMSx() {

		return new FilterSettingsMSx();
	}

	public static FilterSettingsSIM getFilterSettingsSIM() {

		FilterSettingsSIM settings = new FilterSettingsSIM();
		settings.setLimitIons(INSTANCE().getInteger(P_LIMIT_IONS_SIM, DEF_LIMIT_IONS_SIM));

		return settings;
	}

	public static FilterSettingsTandemMS getFilterSettingsTandemMS() {

		return new FilterSettingsTandemMS();
	}

	public static FilterSettingsHighResMS getFilterSettingsHighResMS() {

		return new FilterSettingsHighResMS();
	}

	public static FilterSettingsNominalMS getFilterSettingsNominalMS() {

		return new FilterSettingsNominalMS();
	}

	public static FilterSettingsPolarity getFilterSettingsPolarity() {

		return new FilterSettingsPolarity();
	}
}