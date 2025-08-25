/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.sumarea.preferences;

import org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.sumarea.settings.ChromatogramIntegrationSettings;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_SELECTED_IONS = "selectedIons";
	public static final String DEF_SELECTED_IONS = ""; // none selected means integrate all ion

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_SELECTED_IONS, DEF_SELECTED_IONS);
	}

	/**
	 * Returns a settings instance.
	 */
	public static ChromatogramIntegrationSettings getIntegrationSettings() {

		ChromatogramIntegrationSettings integrationSettings = new ChromatogramIntegrationSettings();
		String ions = INSTANCE().get(P_SELECTED_IONS, DEF_SELECTED_IONS);
		integrationSettings.setSelectedIons(ions);
		return integrationSettings;
	}
}