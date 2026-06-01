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

import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.fieldeditors.TargetFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageTargetsList extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageTargetsList() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Targets List");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new TargetFieldEditor(PreferenceSupplier.P_TARGET_LIST, "Targets", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
