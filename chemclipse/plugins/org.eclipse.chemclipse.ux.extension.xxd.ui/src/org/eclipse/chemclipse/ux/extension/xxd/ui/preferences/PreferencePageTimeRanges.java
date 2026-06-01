/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageTimeRanges extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageTimeRanges() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Time Ranges");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_TIME_RANGE_SELECTION_OFFSET, "Offset [ms] (Selection)", PreferenceSupplier.MIN_TIME_RANGE_SELECTION_OFFSET, PreferenceSupplier.MAX_TIME_RANGE_SELECTION_OFFSET, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}