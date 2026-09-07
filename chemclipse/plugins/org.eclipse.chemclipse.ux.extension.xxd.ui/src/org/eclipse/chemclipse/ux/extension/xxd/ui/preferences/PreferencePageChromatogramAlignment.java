/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageChromatogramAlignment extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageChromatogramAlignment() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Chromatogram Alignment");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_SET_CHROMATOGRAM_INTENSITY_RANGE, "Set chromatogram intensity range", getFieldEditorParent()));
		addIntegerField(PreferenceSupplier.P_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY, "Stretch Chromatogram Scan Delay [ms]:", PreferenceSupplier.MIN_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY, PreferenceSupplier.MAX_STRETCH_CHROMATOGRAM_MILLISECONDS_SCAN_DELAY);
		addIntegerField(PreferenceSupplier.P_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH, "Stretch Chromatogram Length [ms]:", PreferenceSupplier.MIN_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH, PreferenceSupplier.MAX_STRETCH_CHROMATOGRAM_MILLISECONDS_LENGTH);
	}

	private void addIntegerField(String name, String labelText, int min, int max) {

		IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(name, labelText, getFieldEditorParent());
		integerFieldEditor.setValidRange(min, max);
		addField(integerFieldEditor);
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}