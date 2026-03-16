/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.ui.Activator;

public class PreferenceSupplierMethods extends AbstractPreferenceSupplier {

	public static final String P_SHOW_RESUME_METHOD_DIALOG = "showResumeMethodDialog";
	public static final boolean DEF_SHOW_RESUME_METHOD_DIALOG = true;
	public static final String P_CREATE_METHOD_ENABLE_RESUME = "createMethodEnableResume";
	public static final boolean DEF_CREATE_METHOD_ENABLE_RESUME = true;
	public static final String P_PROCESSOR_SELECTION_DATA_CATEGORY = "processorSelectionDataCategory";
	public static final boolean DEF_PROCESSOR_SELECTION_DATA_CATEGORY = true;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplierMethods.class);
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getDefault().getBundle().getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_SHOW_RESUME_METHOD_DIALOG, DEF_SHOW_RESUME_METHOD_DIALOG);
		putDefault(P_CREATE_METHOD_ENABLE_RESUME, DEF_CREATE_METHOD_ENABLE_RESUME);
		putDefault(P_PROCESSOR_SELECTION_DATA_CATEGORY, DEF_PROCESSOR_SELECTION_DATA_CATEGORY);
	}

	public static boolean isShowResumeMethodDialog() {

		return INSTANCE().getBoolean(P_SHOW_RESUME_METHOD_DIALOG, DEF_SHOW_RESUME_METHOD_DIALOG);
	}

	public static void setShowResumeMethodDialog(boolean showResumeMethodDialog) {

		INSTANCE().setBoolean(P_SHOW_RESUME_METHOD_DIALOG, showResumeMethodDialog);
	}

	public static boolean isCreateMethodEnableResume() {

		return INSTANCE().getBoolean(P_CREATE_METHOD_ENABLE_RESUME, DEF_CREATE_METHOD_ENABLE_RESUME);
	}
}