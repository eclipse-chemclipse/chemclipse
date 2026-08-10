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
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Settings (MSD)");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new DirectoryFieldEditor(PreferenceSupplier.P_PATH_MASS_SPECTRUM_LIBRARIES, "Path mass spectrum libraries.", getFieldEditorParent()));
		IntegerFieldEditor libraryMSDLimitSortingEditor = new IntegerFieldEditor(PreferenceSupplier.P_LIBRARY_MSD_LIMIT_SORTING, "Disable sorting above number of entries:", getFieldEditorParent());
		libraryMSDLimitSortingEditor.setValidRange(PreferenceSupplier.MIN_LIBRARY_MSD_LIMIT_SORTING, PreferenceSupplier.MAX_LIBRARY_MSD_LIMIT_SORTING);
		addField(libraryMSDLimitSortingEditor);

		addField(new SpacerFieldEditor(getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceSupplier.P_MASS_SPECTRUM_SHOW_METHODS_TOOLBAR, "Show Methods Toolbar", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_MASS_SPECTRUM_SHOW_RELATIVE_INTENSITY, "Show Relative Intensity", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {

	}
}
