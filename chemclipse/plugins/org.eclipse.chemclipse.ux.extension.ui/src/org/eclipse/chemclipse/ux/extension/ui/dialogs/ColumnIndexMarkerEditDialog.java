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
package org.eclipse.chemclipse.ux.extension.ui.dialogs;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.columns.ISeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumn;
import org.eclipse.chemclipse.model.columns.SeparationColumnPackaging;
import org.eclipse.chemclipse.model.identifier.ColumnIndexMarker;
import org.eclipse.chemclipse.model.identifier.IColumnIndexMarker;
import org.eclipse.chemclipse.support.model.SeparationColumnType;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ColumnIndexMarkerEditDialog extends TitleAreaDialog {

	private AtomicReference<Text> textRetentionIndex = new AtomicReference<>();
	private AtomicReference<Text> textName = new AtomicReference<>();
	private AtomicReference<Combo> comboColumnType = new AtomicReference<>();
	private AtomicReference<Combo> comboColumnPackaging = new AtomicReference<>();
	private AtomicReference<Text> textCalculationType = new AtomicReference<>();
	private AtomicReference<Text> textLength = new AtomicReference<>();
	private AtomicReference<Text> textDiameter = new AtomicReference<>();
	private AtomicReference<Text> textPhase = new AtomicReference<>();
	private AtomicReference<Text> textThickness = new AtomicReference<>();

	private final IColumnIndexMarker columnIndexMarker;
	private IColumnIndexMarker result = null;

	public ColumnIndexMarkerEditDialog(Shell parentShell, IColumnIndexMarker columnIndexMarker) {

		super(parentShell);
		this.columnIndexMarker = columnIndexMarker;
	}

	public IColumnIndexMarker getColumnIndexMarker() {

		return result;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(columnIndexMarker == null ? "Add Column Index" : "Edit Column Index");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Column Index Marker");
		setMessage("Define the retention index and column properties.", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		ISeparationColumn column = columnIndexMarker != null ? columnIndexMarker.getSeparationColumn() : null;

		createLabel(composite, "Retention Index:");
		textRetentionIndex.set(createText(composite, columnIndexMarker != null ? Float.toString(columnIndexMarker.getRetentionIndex()) : "0.0"));

		createLabel(composite, "Column Name:");
		textName.set(createText(composite, column != null ? column.getName() : ""));

		createLabel(composite, "Column Type:");
		comboColumnType.set(createComboColumnType(composite, column != null ? column.getSeparationColumnType() : SeparationColumnType.DEFAULT));

		createLabel(composite, "Column Packaging:");
		comboColumnPackaging.set(createComboColumnPackaging(composite, column != null ? column.getSeparationColumnPackaging() : SeparationColumnPackaging.CAPILLARY));

		createLabel(composite, "Calculation Type:");
		textCalculationType.set(createText(composite, column != null ? column.getCalculationType() : ""));

		createLabel(composite, "Length:");
		textLength.set(createText(composite, column != null ? column.getLength() : ""));

		createLabel(composite, "Diameter:");
		textDiameter.set(createText(composite, column != null ? column.getDiameter() : ""));

		createLabel(composite, "Phase:");
		textPhase.set(createText(composite, column != null ? column.getPhase() : ""));

		createLabel(composite, "Thickness:");
		textThickness.set(createText(composite, column != null ? column.getThickness() : ""));

		return container;
	}

	@Override
	protected void okPressed() {

		try {
			float retentionIndex = Float.parseFloat(textRetentionIndex.get().getText().trim());
			String name = textName.get().getText().trim();
			int columnTypeIndex = comboColumnType.get().getSelectionIndex();
			SeparationColumnType columnType = columnTypeIndex >= 0 ? SeparationColumnType.values()[columnTypeIndex] : SeparationColumnType.DEFAULT;
			int packagingIndex = comboColumnPackaging.get().getSelectionIndex();
			SeparationColumnPackaging packaging = packagingIndex >= 0 ? SeparationColumnPackaging.values()[packagingIndex] : SeparationColumnPackaging.CAPILLARY;
			String calculationType = textCalculationType.get().getText().trim();
			String length = textLength.get().getText().trim();
			String diameter = textDiameter.get().getText().trim();
			String phase = textPhase.get().getText().trim();
			String thickness = textThickness.get().getText().trim();
			SeparationColumn separationColumn = new SeparationColumn(name, columnType, length, diameter, phase);
			separationColumn.setSeparationColumnPackaging(packaging);
			separationColumn.setCalculationType(calculationType);
			separationColumn.setThickness(thickness);
			result = new ColumnIndexMarker(separationColumn, retentionIndex);
			super.okPressed();
		} catch(NumberFormatException e) {
			setErrorMessage("Retention Index must be a valid number.");
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(450, 450);
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private Text createText(Composite parent, String value) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(value != null ? value : "");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	private Combo createComboColumnType(Composite parent, SeparationColumnType selected) {

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		SeparationColumnType[] values = SeparationColumnType.values();
		int selectionIndex = 0;
		for(int i = 0; i < values.length; i++) {
			combo.add(values[i].label());
			if(values[i] == selected) {
				selectionIndex = i;
			}
		}
		combo.select(selectionIndex);
		return combo;
	}

	private Combo createComboColumnPackaging(Composite parent, SeparationColumnPackaging selected) {

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		SeparationColumnPackaging[] values = SeparationColumnPackaging.values();
		int selectionIndex = 0;
		for(int i = 0; i < values.length; i++) {
			combo.add(values[i].label());
			if(values[i] == selected) {
				selectionIndex = i;
			}
		}
		combo.select(selectionIndex);
		return combo;
	}
}