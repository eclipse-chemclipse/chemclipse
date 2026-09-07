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

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.FloatFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpinnerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageUnknownTarget extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageUnknownTarget() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Unknown Target");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_ADD_UNKNOWN_AFTER_DELETE_TARGETS_ALL, "Delete All (on Add)", getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MATCH_QUALITY_UNKNOWN_TARGET, "Match Quality", PreferenceSupplier.MIN_MATCH_QUALITY, PreferenceSupplier.MAX_MATCH_QUALITY, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_UNKNOWN_TARGET_ADD_RETENTION_INDEX, "Add Retention Index", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_VERIFY_UNKNOWN_TARGET, "Verify", getFieldEditorParent()));
		addField(new SpinnerFieldEditor(PreferenceSupplier.P_MAX_UNKNOWN_TRACES, "Show Traces", PreferenceSupplier.MIN_TRACES, PreferenceSupplier.MAX_TRACES, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}