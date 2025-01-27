/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.preferences;

import org.eclipse.chemclipse.ux.extension.ui.Activator;
import org.eclipse.chemclipse.ux.extension.ui.fieldeditors.UserLocationsFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageUserLocations extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageUserLocations() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("User Locations");
		setDescription("");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		addField(new UserLocationsFieldEditor(PreferenceSupplier.P_USER_LOCATIONS, "User Locations", getFieldEditorParent()));
	}
}