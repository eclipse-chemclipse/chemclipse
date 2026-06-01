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

import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.fieldeditors.NamedTracesFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageNamedTraces extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageNamedTraces() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Named Traces");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new NamedTracesFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_OVERLAY_NAMED_TRACES, "Named Traces", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
