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
package org.eclipse.chemclipse.ux.extension.ui.fieldeditors;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.ux.extension.ui.locations.UserLocationsSettingsEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class UserLocationsFieldEditor extends FieldEditor {

	private AtomicReference<UserLocationsSettingsEditor> editorControl = new AtomicReference<>();

	public UserLocationsFieldEditor(String name, String labelText, Composite parent) {

		init(name, labelText);
		createControl(parent);
	}

	@Override
	public int getNumberOfControls() {

		return 1;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {

		getLabelControl(parent);
		UserLocationsSettingsEditor editor = new UserLocationsSettingsEditor(parent, null, null);
		editor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		editorControl.set(editor);
	}

	@Override
	protected void doLoad() {

		String entries = getPreferenceStore().getString(getPreferenceName());
		editorControl.get().load(entries);
	}

	@Override
	protected void doLoadDefault() {

		String entries = getPreferenceStore().getDefaultString(getPreferenceName());
		editorControl.get().load(entries);
	}

	@Override
	protected void doStore() {

		getPreferenceStore().setValue(getPreferenceName(), editorControl.get().getValues());
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {

		Control control = editorControl.get().getControl();
		GridData gridData = (GridData)control.getLayoutData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = (numColumns >= 2) ? numColumns - 1 : 1;
	}
}