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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.swt.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.ExtractTransferDefinition;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ExtractTransferDefinitionEditDialog extends TitleAreaDialog {

	private AtomicReference<ExtractTransferDefinitionEditor> editorControl = new AtomicReference<>();

	private ExtractTransferDefinition definition;
	private final Map<String, String> libraryFields;

	public ExtractTransferDefinitionEditDialog(Shell parentShell, ExtractTransferDefinition definition, Map<String, String> libraryFields) {

		super(parentShell);
		this.definition = definition;
		this.libraryFields = libraryFields;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(definition != null ? "Modify Definition" : "Add Definition");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite)super.createDialogArea(parent);
		setTitle("Extract and Transfer Definition");
		setMessage("Define a rule to extract data from a source field and transfer it to a sink field.");

		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		ExtractTransferDefinitionEditor editor = new ExtractTransferDefinitionEditor(container, SWT.NONE, libraryFields);
		editor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(definition != null) {
			editor.load(definition);
		}
		editorControl.set(editor);

		return area;
	}

	@Override
	protected void okPressed() {

		definition = editorControl.get().getDefinition();
		super.okPressed();
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(750, 230);
	}

	public ExtractTransferDefinition getDefinition() {

		return definition;
	}
}