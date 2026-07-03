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

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageChromatogramHeatmap extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageChromatogramHeatmap() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle(ExtensionMessages.chromatogramHeatmap);
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MIN_MSD, "Scale Intensity Min (MSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MAX_MSD, "Scale Intensity Max (MSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MIN_WSD, "Scale Intensity Min (WSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MAX_WSD, "Scale Intensity Max (WSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MIN_TSD, "Scale Intensity Min (TSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
		addIntegerField(PreferenceSupplier.P_HEATMAP_SCALE_INTENSITY_MAX_TSD, "Scale Intensity Max (TSD)", PreferenceSupplier.MIN_HEATMAP_SCALE_INTENSITY, PreferenceSupplier.MAX_HEATMAP_SCALE_INTENSITY);
	}

	private void addIntegerField(String name, String labelText, int min, int max) {

		IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(name, labelText, getFieldEditorParent());
		integerFieldEditor.setValidRange(min, max);
		addField(integerFieldEditor);
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
