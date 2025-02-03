/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add a color codes setting
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.pcr.ui.Activator;
import org.eclipse.chemclipse.ux.extension.pcr.ui.fieldeditors.ColorCodesFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageWellChart extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageWellChart() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Well Chart");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_PCR_DEFAULT_COLOR, "Default Line Color:", getFieldEditorParent()));
		addField(new ColorCodesFieldEditor(PreferenceSupplier.P_PCR_WELL_COLOR_CODES, "Color Codes", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
