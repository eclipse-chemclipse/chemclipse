/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.ui.fieldeditors;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.ui.swt.FileHeaderDataEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FileHeaderDataFieldEditor extends FieldEditor {

	private AtomicReference<FileHeaderDataEditor> editorControl = new AtomicReference<>();

	public FileHeaderDataFieldEditor(String name, String labelText, Composite parent) {

		init(name, labelText);
		createControl(parent);
	}

	@Override
	public int getNumberOfControls() {

		return 2;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {

		getLabelControl(parent);

		FileHeaderDataEditor editor = new FileHeaderDataEditor(parent, SWT.NONE);
		editor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		editor.addChangeListener(_ -> {

			String message = editor.getMessage();
			if(message != null) {
				showErrorMessage(message);
			} else {
				clearErrorMessage();
			}
		});

		editorControl.set(editor);
	}

	@Override
	protected void doLoad() {

		editorControl.get().load(getPreferenceStore().getString(getPreferenceName()));
	}

	@Override
	protected void doLoadDefault() {

		editorControl.get().load(getPreferenceStore().getDefaultString(getPreferenceName()));
	}

	@Override
	protected void doStore() {

		getPreferenceStore().setValue(getPreferenceName(), editorControl.get().save());
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {

		Control control = editorControl.get();
		GridData gridData = (GridData)control.getLayoutData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = (numColumns >= 2) ? numColumns - 1 : 1;
	}
}