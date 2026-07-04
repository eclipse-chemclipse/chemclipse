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

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.DoubleFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.charts.ChartOptions;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ScanChartAxisIon extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ScanChartAxisIon() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Scan Chart Ion Axis");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_SHOW_X_AXIS_IONS, ExtensionMessages.show, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_CHART_POSITION_X_AXIS_IONS, ExtensionMessages.position + ":", ChartOptions.POSITIONS, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_CHART_GRIDLINE_STYLE_X_AXIS_IONS, ExtensionMessages.gridLineStyle + ":", ChartOptions.LINE_STYLES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_SHOW_X_AXIS_TITLE_IONS, ExtensionMessages.showAxisTitle, getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("The axis scale is used to set the values.", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_ENABLE_FIXED_RANGE_X, "Fixed range X", getFieldEditorParent()));
		addField(new DoubleFieldEditor(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_START_X, "Start X", PreferenceSupplier.MIN_RANGE, PreferenceSupplier.MAX_RANGE, getFieldEditorParent()));
		addField(new DoubleFieldEditor(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_STOP_X, "Stop X", PreferenceSupplier.MIN_RANGE, PreferenceSupplier.MAX_RANGE, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
