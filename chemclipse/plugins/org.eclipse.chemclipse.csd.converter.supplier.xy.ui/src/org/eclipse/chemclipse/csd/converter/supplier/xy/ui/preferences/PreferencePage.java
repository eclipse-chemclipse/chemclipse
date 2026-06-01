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
package org.eclipse.chemclipse.csd.converter.supplier.xy.ui.preferences;

import org.eclipse.chemclipse.csd.converter.supplier.xy.io.DelimiterFormat;
import org.eclipse.chemclipse.csd.converter.supplier.xy.io.RetentionTimeFormat;
import org.eclipse.chemclipse.csd.converter.supplier.xy.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.csd.converter.supplier.xy.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("XY CSD");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_AUTO_DETECT_FORMAT, "Auto-detect Format", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_DELIMITER_FORMAT, "Delimiter Format: ", DelimiterFormat.getOptions(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_RETENTION_TIME_FORMAT, "Retention Time Format:", RetentionTimeFormat.getOptions(), getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}