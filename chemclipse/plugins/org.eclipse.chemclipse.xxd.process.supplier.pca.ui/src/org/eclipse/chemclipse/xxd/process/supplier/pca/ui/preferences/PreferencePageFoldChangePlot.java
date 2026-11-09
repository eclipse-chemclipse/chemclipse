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
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.ExtendedIntegerFieldEditor;
import org.eclipse.chemclipse.xxd.process.supplier.pca.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.Activator;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageFoldChangePlot extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageFoldChangePlot() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle(StatisticsMessages.foldChangePlot);
		setDescription(""); //$NON-NLS-1$
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_FOLD_CHANGE_PLOT_SYMBOL_TYPE, StatisticsMessages.symbolType, PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_FOLD_CHANGE_PLOT_HIGHLIGHT_SYMBOL_TYPE, StatisticsMessages.highlightSymbolType, PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_FOLD_CHANGE_PLOT_SYMBOL_SIZE, StatisticsMessages.symbolSize, PreferenceSupplier.MIN_SCORE_PLOT_2D_SYMBOL_SIZE, PreferenceSupplier.MAX_SCORE_PLOT_2D_SYMBOL_SIZE, getFieldEditorParent()));

	}

}
