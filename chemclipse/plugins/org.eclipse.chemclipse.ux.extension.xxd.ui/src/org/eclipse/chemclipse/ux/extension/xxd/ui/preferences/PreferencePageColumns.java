/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.model.core.support.ColumnField;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.swt.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.fieldeditors.ColumnMappingFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageColumns extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageColumns() {

		super(GRID);
		/*
		 * Caution:
		 * Use strictly the 'org.eclipse.chemclipse.swt.ui.Activator'.
		 * Historically, the system column settings were stored in 'chemclipse.model'
		 * to allow to use them whilst importing chromatograms. But the
		 * ColumnMappingFieldEditor requires elements from 'xxd.ui' to create the
		 * process method settings UI editor.
		 */
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Columns");
		setDescription("");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_PARSE_SEPARATION_COLUMN_FROM_HEADER, "Parse Separation Column (Chromatogram Import)", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SEPARATION_COLUMN_HEADER_FIELD, "Column Field", ColumnField.getOptions(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_PARSE_SEPARATION_COLUMN_REFERENCED_CHROMATOGRAMS, "Parse Referenced Chromatograms", getFieldEditorParent()));
		addField(new ColumnMappingFieldEditor(PreferenceSupplier.P_SEPARATION_COLUMN_MAPPINGS, "Mappings", getFieldEditorParent()));
	}
}