/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageScanBrowse extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageScanBrowse() {

		super(FLAT);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Scan Browse");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new FileFieldEditor(PreferenceSupplier.P_SCAN_BROWSE_LIBRARY_FILE, "Library Path", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
