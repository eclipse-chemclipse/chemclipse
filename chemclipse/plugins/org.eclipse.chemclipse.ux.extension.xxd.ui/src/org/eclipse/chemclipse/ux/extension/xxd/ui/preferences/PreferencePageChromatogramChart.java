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
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.DoubleFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.charts.ChartOptions;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageChromatogramChart extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageChromatogramChart() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Chromatogram Chart");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_CHART_COMPRESSION_TYPE, ExtensionMessages.compressionType + ":", ChartOptions.COMPRESSION_TYPES, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_CHROMATOGRAM, "Color Chromatogram:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_CHROMATOGRAM_INACTIVE, "Color Chromatogram (Inactive):", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_ENABLE_CHROMATOGRAM_AREA, "Enable Chromatogram Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_CHROMATOGRAM_BASELINE, "Show Chromatogram Baseline", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_CHROMATOGRAM_BASELINE, "Color Chromatogram Baseline:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_ENABLE_BASELINE_AREA, "Enable Baseline Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_ALTERNATE_WINDOW_MOVE_DIRECTION, "Use alternate window move direction", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CONDENSE_CYCLE_NUMBER_SCANS, "Condense cycle number scans", getFieldEditorParent()));

		addField(new DoubleFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_EXTEND_Y, "Extend Y (1.0 = 100%)", PreferenceSupplier.MIN_CHROMATOGRAM_EXTEND_Y, PreferenceSupplier.MAX_CHROMATOGRAM_EXTEND_Y, getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("The following settings define how the chart reacts on a user selection and zoom events.", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_RESTRICT_SELECT_X, "Restrict Select X", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_RESTRICT_SELECT_Y, "Restrict Select Y", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_FORCE_ZERO_MIN_Y_MSD, "Force Zero Min Y (MSD)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_X, "Reference Zoom Zero X", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_REFERENCE_ZOOM_ZERO_Y, "Reference Zoom Zero Y", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_RESTRICT_ZOOM_X, "Restrict Zoom X", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_RESTRICT_ZOOM_Y, "Restrict Zoom Y", getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_MARK_ANALYSIS_SEGMENTS, "Mark Analysis Segments", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_RETENTION_INDEX_MARKER, "Show Retention Index Marker", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_TARGET_MARKER, "Show Target Marker", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}