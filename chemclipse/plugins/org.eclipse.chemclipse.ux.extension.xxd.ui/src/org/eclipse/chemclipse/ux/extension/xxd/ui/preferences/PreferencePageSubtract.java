/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
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
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpinnerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.preferences.PreferenceSupplierModelMSD;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageSubtract extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageSubtract() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStoreSubtract());
		setTitle("Subtract/Combined");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplierModelMSD.P_USE_NOMINAL_MZ, "Use nominal m/z", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplierModelMSD.P_USE_NORMALIZED_SCAN, "Use normalized scan", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplierModelMSD.P_CALCULATION_TYPE, "Calculation Type", CalculationType.getOptions(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplierModelMSD.P_USE_PEAKS_INSTEAD_OF_SCANS, "Use peaks instead of scans", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplierModelMSD.P_DELETE_TARGETS_OPTIMIZE_SCAN, "Delete Targets (Optimize Scan)", getFieldEditorParent()));
		addField(new SpinnerFieldEditor(PreferenceSupplierModelMSD.P_COPY_TRACES_CLIPBOARD, "Copy Traces", PreferenceSupplierModelMSD.MIN_TRACES, PreferenceSupplierModelMSD.MAX_TRACES, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}