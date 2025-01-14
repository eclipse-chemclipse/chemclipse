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
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.firstderivative.ui.preferences;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.firstderivative.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.firstderivative.ui.Activator;
import org.eclipse.chemclipse.chromatogram.peak.detector.core.FilterMode;
import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.model.DetectorType;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.FloatFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.WindowSizeFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageMSD extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageMSD() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("MSD");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceSupplier.P_THRESHOLD_MSD, "Threshold", Threshold.getOptions(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_DETECTOR_TYPE_MSD, "Detector Type", DetectorType.getOptions(), getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MIN_SN_RATIO_MSD, "Min S/N Ratio", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		addField(new WindowSizeFieldEditor(PreferenceSupplier.P_MOVING_AVERAGE_WINDOW_SIZE_MSD, "Window Size", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_NOISE_SEGMENTS_MSD, "Use Noise-Segments", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_FILTER_MODE_MSD, "Filter Mode", FilterMode.getOptions(), getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceSupplier.P_MZ_VALUES_TO_FILTER_MSD, "m/z values to filter", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_INDIVIDUAL_TRACES_MSD, "Use Individual Traces", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_OPTIMIZE_BASELINE_MSD, "Optimize Baseline (VV)", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}