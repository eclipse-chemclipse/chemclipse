/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.preferences;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.Activator;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Retention Index");
		setDescription("");
	}

	@Override
	protected void createFieldEditors() {

		addField(new LabelFieldEditor("Export Options", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CALIBRATION_EXPORT_USE_CURATED_NAMES, "Use Curated Names", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CALIBRATION_EXPORT_DERIVE_MISSING_INDICES, "Derive Missing Indices", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_OPEN_REPORT_AFTER_PROCESSING, "Open report after processing", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Import Calibration Files (*.cal)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_DIRECTORY_IMPORT_CALIBRATION_FILES, "Use a standard directory", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_STANDARD_DIRECTORY_IMPORT_CALIBRATION_FILES, "Load files from directory", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
