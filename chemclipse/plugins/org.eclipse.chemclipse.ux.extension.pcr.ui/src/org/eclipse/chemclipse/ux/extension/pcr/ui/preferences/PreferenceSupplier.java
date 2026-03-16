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
 * Christoph Läubrich - add NMR datatype, remove obsolete constants, extract init into static methods
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.preferences;

import org.eclipse.chemclipse.pcr.model.core.support.LabelSetting;
import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_PCR_DEFAULT_COLOR = "pcrDefaultColor";
	public static final String DEF_PCR_DEFAULT_COLOR = "192,192,192";
	public static final String P_PCR_PLATE_COLOR_CODES = "pcrColorCodes";
	public static final String DEF_PCR_PLATE_COLOR_CODES = "";
	public static final String P_PCR_WELL_COLOR_CODES = "pcrWellColorCodes";
	public static final String DEF_PCR_WELL_COLOR_CODES = "";
	public static final String P_PCR_SAVE_AS_FOLDER = "pcrSaveAsFolder";
	public static final String DEF_PCR_SAVE_AS_FOLDER = "";
	public static final String P_PCR_REFERENCE_LABEL = "pcrReferenceLabel";
	public static final String DEF_PCR_REFERENCE_LABEL = LabelSetting.COORDINATE_SAMPLENAME.name();

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	public static IPreferenceStore getPreferenceStore() {

		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_PCR_DEFAULT_COLOR, DEF_PCR_DEFAULT_COLOR);
		putDefault(P_PCR_PLATE_COLOR_CODES, DEF_PCR_PLATE_COLOR_CODES);
		putDefault(P_PCR_WELL_COLOR_CODES, DEF_PCR_WELL_COLOR_CODES);
		putDefault(P_PCR_SAVE_AS_FOLDER, DEF_PCR_SAVE_AS_FOLDER);
		putDefault(P_PCR_REFERENCE_LABEL, DEF_PCR_REFERENCE_LABEL);
	}
}
