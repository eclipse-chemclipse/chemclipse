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
 * Lorenz Gerber - add highlight option
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

public class PreferencePageScorePlot extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageScorePlot() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle(StatisticsMessages.scorePlot);
		setDescription(""); //$NON-NLS-1$
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_SCORE_PLOT_2D_SYMBOL_TYPE, StatisticsMessages.symbolType, PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCORE_PLOT_2D_HIGHLIGHT_SYMBOL_TYPE, StatisticsMessages.highlightSymbolType, PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceSupplier.P_SCORE_PLOT_2D_SYMBOL_SIZE, StatisticsMessages.symbolSize, PreferenceSupplier.MIN_SCORE_PLOT_2D_SYMBOL_SIZE, PreferenceSupplier.MAX_SCORE_PLOT_2D_SYMBOL_SIZE, getFieldEditorParent()));
	}
}