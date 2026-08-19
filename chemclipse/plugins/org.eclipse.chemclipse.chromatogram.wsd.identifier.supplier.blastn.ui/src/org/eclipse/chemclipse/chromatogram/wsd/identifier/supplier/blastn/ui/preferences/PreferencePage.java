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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.preferences;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.ui.Activator;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Nucleotide BLAST");
		setDescription("Basic Local Alignment Search Tool");
	}

	@Override
	protected void createFieldEditors() {

		addField(new DirectoryFieldEditor(PreferenceSupplier.P_BLAST_EXECUTABLE_PATH, "BLAST installation path", getFieldEditorParent()));
		addField(new LabelFieldEditor("Uses PATH environment variable if left empty.", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_BLAST_DATABASE_PATH, "BLAST database path", getFieldEditorParent()));
		addField(new LabelFieldEditor("Select the location where update_blastdb downloaded the databases to.", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
