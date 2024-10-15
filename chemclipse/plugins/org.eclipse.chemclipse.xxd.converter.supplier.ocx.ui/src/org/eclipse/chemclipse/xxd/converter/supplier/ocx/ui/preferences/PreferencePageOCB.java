/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.ocx.ui.preferences;

import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.ui.Activator;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.ChromatogramVersion;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageOCB extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageOCB() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Measurement Data (*.ocb)");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_VERSION_SAVE, "Save (*.ocb) as version:", ChromatogramVersion.getOptions(), getFieldEditorParent()));
		addField(new ScaleFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_COMPRESSION_LEVEL, "Compression: none to best", getFieldEditorParent(), PreferenceSupplier.MIN_COMPRESSION_LEVEL, PreferenceSupplier.MAX_COMPRESSION_LEVEL, 1, 1));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATGRAM_EXPORT_REFERENCES_SEPARATELY, "Export References Separately", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHROMATGRAM_EXPORT_REFERENCES_HEADER_FIELD, "Header Field (Export References)", HeaderField.getOptions(), getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {

	}
}