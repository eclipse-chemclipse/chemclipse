/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageProcessorToolbar extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageProcessorToolbar() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Processor Quick-Access");
		setDescription("Set shortcuts for processing steps in the chromatogram editor toolbar for each detector type.");
	}

	@Override
	public void createFieldEditors() {

	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
