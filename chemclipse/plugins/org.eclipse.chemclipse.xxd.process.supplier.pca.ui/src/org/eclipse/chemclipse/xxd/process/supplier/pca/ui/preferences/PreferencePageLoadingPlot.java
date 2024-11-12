/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - add highlight option
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.ExtendedIntegerFieldEditor;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageLoadingPlot extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageLoadingPlot() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Loading Plot");
		setDescription("");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_TYPE, "Symbol type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_LOADING_PLOT_2D_HIGHLIGHT_SYMBOL_TYPE, "Highlight Symbol type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_LOADING_PLOT_2D_SYMBOL_SIZE, "Symbol size", PreferenceSupplier.MIN_LOADING_PLOT_2D_SYMBOL_SIZE, PreferenceSupplier.MAX_LOADING_PLOT_2D_SYMBOL_SIZE, getFieldEditorParent()));
	}
}