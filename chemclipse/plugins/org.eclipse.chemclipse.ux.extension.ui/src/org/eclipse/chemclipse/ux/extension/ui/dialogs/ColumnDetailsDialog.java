/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.ui.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ColumnDetailsDialog extends TitleAreaDialog {

	private Map<SeparationColumnType, String> nameMap = new HashMap<>();

	public ColumnDetailsDialog(Shell parentShell) {

		super(parentShell);
	}

	public void update(List<ISeparationColumn> separationColumns) {

		for(ISeparationColumn separationColumn : separationColumns) {
			nameMap.put(separationColumn.getSeparationColumnType(), separationColumn.getName());
		}
	}

	public Map<SeparationColumnType, String> getNameMap() {

		return nameMap;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Column Details");
		setMessage("Manage the column specific settings.", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		createColumnDetails(composite, SeparationColumnType.NON_POLAR);
		createColumnDetails(composite, SeparationColumnType.SEMI_POLAR);
		createColumnDetails(composite, SeparationColumnType.POLAR);

		return container;
	}

	private void createColumnDetails(Composite parent, SeparationColumnType separationColumnType) {

		createLabel(parent, separationColumnType);
		createTextName(parent, separationColumnType);
	}

	private void createLabel(Composite parent, SeparationColumnType separationColumnType) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(separationColumnType.label());
		label.setToolTipText("SeparationColumnType");
	}

	private void createTextName(Composite parent, SeparationColumnType separationColumnType) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(nameMap.getOrDefault(separationColumnType, ""));
		text.setToolTipText("Column Name");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(e -> nameMap.put(separationColumnType, text.getText().trim()));
	}

	@Override
	protected void okPressed() {

		super.okPressed();
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(500, 700);
	}
}