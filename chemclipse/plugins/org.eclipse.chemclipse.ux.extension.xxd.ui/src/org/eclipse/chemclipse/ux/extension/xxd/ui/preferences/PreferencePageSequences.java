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
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageSequences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageSequences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Sequences");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_USE_SUBFOLDER, "Use Subfolder", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_SORT_DATA, "Sort Data", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_PATH_ROOT_FOLDER, "Root Folder", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_PATH_PARENT_FOLDER, "Parent Folder", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_PATH_SUB_FOLDER, "Sub Folder", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_SEQUENCE_EXPLORER_PATH_DIALOG_FOLDER, "Dialog Folder", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
