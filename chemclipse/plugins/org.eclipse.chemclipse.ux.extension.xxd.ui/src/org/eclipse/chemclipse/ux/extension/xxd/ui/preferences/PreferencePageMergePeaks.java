/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageMergePeaks extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageMergePeaks() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Merge Peaks");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_MERGE_PEAKS_CALCULATION_TYPE, "Calculation Type", CalculationType.getOptions(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_MERGE_PEAKS_IDENTIFICATION_TARGETS, "Merge Identification Targets", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_MERGE_PEAKS_DELETE_ORIGINS, "Delete Origins", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
