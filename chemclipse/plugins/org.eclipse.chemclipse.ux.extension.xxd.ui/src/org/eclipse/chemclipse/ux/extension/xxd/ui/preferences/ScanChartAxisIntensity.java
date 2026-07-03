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
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.charts.ChartOptions;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ScanChartAxisIntensity extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ScanChartAxisIntensity() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Scan Chart Intensity");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new StringFieldEditor(PreferenceSupplier.P_SCAN_CHART_FORMAT_Y_AXIS_INTENSITY, ExtensionMessages.format + ":", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_INTENSITY, ExtensionMessages.show, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_CHART_POSITION_Y_AXIS_INTENSITY, ExtensionMessages.position + ":", ChartOptions.POSITIONS, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SCAN_CHART_GRIDLINE_STYLE_Y_AXIS_INTENSITY, ExtensionMessages.gridLineStyle + ":", ChartOptions.LINE_STYLES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_TITLE_INTENSITY, ExtensionMessages.showAxisTitle, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
