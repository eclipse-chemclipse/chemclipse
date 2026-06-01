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

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageTasks extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageTasks() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Task Bar (Quick Access)");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new LabelFieldEditor("The task bar enables you to select the part needed for your work. Please select the position where you'd like to display the items.", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
