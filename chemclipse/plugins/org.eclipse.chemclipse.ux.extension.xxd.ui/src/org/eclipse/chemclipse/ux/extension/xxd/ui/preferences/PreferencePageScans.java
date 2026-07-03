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
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.ExtendedIntegerFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpinnerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.TracesExportOption;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ScanIdentifierUI;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageScans extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageScans() {

		super(FLAT);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Scans");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpinnerFieldEditor(PreferenceSupplier.P_SCAN_LABEL_HIGHEST_INTENSITIES, "Label Intensities:", PreferenceSupplier.MIN_SCAN_LABEL_HIGHEST_INTENSITIES, PreferenceSupplier.MAX_SCAN_LABEL_HIGHEST_INTENSITIES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_LABEL_MODULO_INTENSITIES, "Add additional intensity labels", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_AUTOFOCUS_SUBTRACT_SCAN_PART, "Autofocus subtract scan part", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_ENABLE_COMPRESS, "Enable Compress", getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_IDENTIFER_MSD, "Scan Identifier (MSD):", ScanIdentifierUI.getScanIdentifierMSD(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_IDENTIFER_WSD, "Scan Identifier (WSD):", ScanIdentifierUI.getScanIdentifierWSD(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_ENABLE_MULTI_SUBTRACT, "Enable multi subtract modus", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_SUBTRACT_DIALOG, "Show subtract scan preferences dialog", getFieldEditorParent()));
		addField(new SpinnerFieldEditor(PreferenceSupplier.P_MAX_COPY_SCAN_TRACES, "Copy Traces", PreferenceSupplier.MIN_TRACES, PreferenceSupplier.MAX_TRACES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SORT_COPY_TRACES, "Sort Traces", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_TRACES_EXPORT_OPTION, "Traces Export Option", TracesExportOption.getOptions(), getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_TRACES_VIRTUAL_TABLE, "Traces Virtual Table", PreferenceSupplier.MIN_TRACES_VIRTUAL_TABLE, PreferenceSupplier.MAX_TRACES_VIRTUAL_TABLE, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_HYBRID_SEARCH_MOL_WEIGHT_MIN, "Hybrid Search MW (Min)", PreferenceSupplier.MIN_MOL_WEIGHT, PreferenceSupplier.MAX_MOL_WEIGHT, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_HYBRID_SEARCH_MOL_WEIGHT_MAX, "Hybrid Search MW (Max)", PreferenceSupplier.MIN_MOL_WEIGHT, PreferenceSupplier.MAX_MOL_WEIGHT, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
