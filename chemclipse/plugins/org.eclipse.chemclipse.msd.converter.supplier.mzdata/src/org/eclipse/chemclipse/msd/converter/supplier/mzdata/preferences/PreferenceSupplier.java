/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - preference initializer
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.preferences;

import org.eclipse.chemclipse.msd.converter.supplier.mzdata.internal.io.WriterVersion105;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final String P_CHROMATOGRAM_VERSION_SAVE = "chromatogramVersionSave";
	public static final String DEF_CHROMATOGRAM_VERSION_SAVE = WriterVersion105.VERSION;
	public static final String P_CHROMATOGRAM_SAVE_COMPRESSION = "chromatogramSaveCompression";
	public static final boolean DEF_CHROMATOGRAM_SAVE_COMPRESSION = true;
	public static final String P_MASS_SPECTRUM_VERSION_SAVE = "massSpectrumVersionSave";
	public static final String DEF_MASS_SPECTRUM_VERSION_SAVE = WriterVersion105.VERSION;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_CHROMATOGRAM_VERSION_SAVE, DEF_CHROMATOGRAM_VERSION_SAVE);
		putDefault(P_CHROMATOGRAM_SAVE_COMPRESSION, Boolean.toString(DEF_CHROMATOGRAM_SAVE_COMPRESSION));
		putDefault(P_MASS_SPECTRUM_VERSION_SAVE, DEF_MASS_SPECTRUM_VERSION_SAVE);
	}

	public static String getChromatogramVersionSave() {

		return INSTANCE().get(P_CHROMATOGRAM_VERSION_SAVE, DEF_CHROMATOGRAM_VERSION_SAVE);
	}

	public static String[][] getChromatogramVersions() {

		String[][] elements = new String[1][2];
		elements[0][0] = WriterVersion105.VERSION;
		elements[0][1] = WriterVersion105.VERSION;
		return elements;
	}

	public static boolean getChromatogramSaveCompression() {

		return INSTANCE().getBoolean(P_CHROMATOGRAM_SAVE_COMPRESSION, DEF_CHROMATOGRAM_SAVE_COMPRESSION);
	}

	public static String[][] getMassSpectrumVersions() {

		String[][] elements = new String[1][2];
		elements[0][0] = WriterVersion105.VERSION.split("_")[1];
		elements[0][1] = WriterVersion105.VERSION;
		return elements;
	}

	public static String getMassSpectrumVersionSave() {

		return INSTANCE().get(P_MASS_SPECTRUM_VERSION_SAVE, DEF_MASS_SPECTRUM_VERSION_SAVE);
	}
}
