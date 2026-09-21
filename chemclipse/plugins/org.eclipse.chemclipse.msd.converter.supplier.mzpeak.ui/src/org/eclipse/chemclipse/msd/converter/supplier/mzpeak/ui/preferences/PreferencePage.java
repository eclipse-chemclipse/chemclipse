/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.ui.preferences;

import org.eclipse.chemclipse.msd.converter.supplier.mzpeak.ui.Activator;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("mzPeak");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_IMPORT_CENTROIDED_SPECTRA, "Import Centroided Spectra", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_IMPORT_PROFILE_SPECTRA, "Import Profile Spectra", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}