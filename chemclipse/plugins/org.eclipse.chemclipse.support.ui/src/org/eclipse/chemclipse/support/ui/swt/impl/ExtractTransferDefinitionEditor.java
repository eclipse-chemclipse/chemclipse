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

import org.eclipse.chemclipse.model.core.DataType;
import org.eclipse.chemclipse.model.core.ExtractTransferDefinition;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class ExtractTransferDefinitionEditor extends Composite {

	private AtomicReference<Button> checkboxUse = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboSourceField = new AtomicReference<>();
	private AtomicReference<Text> textRegularExpression = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerGroupIndex = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboDataType = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboSinkField = new AtomicReference<>();

	private final Map<String, String> libraryFields;
	private boolean use = true;
	private String sourceField = "name";
	private String regularExpression = "(.*)";
	private int groupIndex = 1;
	private DataType dataType = DataType.STRING;
	private String sinkField = "referenceIdentifier";

	public ExtractTransferDefinitionEditor(Composite parent, int style, Map<String, String> libraryFields) {

		super(parent, style);
		this.libraryFields = libraryFields;
		createControl();
	}

	public void load(ExtractTransferDefinition definition) {

		use = definition.isUse();
		sourceField = definition.getSourceField();
		regularExpression = definition.getRegularExpression();
		groupIndex = definition.getGroupIndex();
		dataType = definition.getDataType();
		sinkField = definition.getSinkField();
		updateInput();
	}

	public ExtractTransferDefinition getDefinition() {

		ExtractTransferDefinition definition = new ExtractTransferDefinition(sourceField, regularExpression, groupIndex, dataType, sinkField);
		definition.setUse(use);
		return definition;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		createCheckboxUse(this);
		createComboSourceField(this);
		createTextRegularExpression(this);
		createSpinnerGroupIndex(this);
		createComboDataType(this);
		createComboSinkField(this);

		updateInput();
	}

	private void createCheckboxUse(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setToolTipText("Use this definition.");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				use = button.getSelection();
			}
		});
		checkboxUse.set(button);
	}

	private void createComboSourceField(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof String key) {
					return libraryFields.getOrDefault(key, key);
				}
				return null;
			}
		});

		GridData gridData = new GridData();
		gridData.widthHint = 160;
		comboViewer.getCombo().setLayoutData(gridData);
		comboViewer.getCombo().setToolTipText("Source Field");
		comboViewer.setInput(libraryFields.keySet().toArray());
		comboViewer.addSelectionChangedListener(_ -> {
			Object selection = comboViewer.getStructuredSelection().getFirstElement();
			if(selection instanceof String field) {
				sourceField = field;
			}
		});

		comboSourceField.set(comboViewer);
	}

	private void createTextRegularExpression(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setToolTipText("Regular Expression");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(_ -> regularExpression = text.getText().trim());
		textRegularExpression.set(text);
	}

	private void createSpinnerGroupIndex(Composite parent) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(Integer.MAX_VALUE);
		spinner.setPageIncrement(1);
		spinner.setToolTipText("Group Index");
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		spinner.setLayoutData(gridData);
		spinner.addModifyListener(_ -> groupIndex = spinner.getSelection());
		spinnerGroupIndex.set(spinner);
	}

	private void createComboDataType(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof DataType dt) {
					return dt.label();
				}
				return null;
			}
		});
		comboViewer.getCombo().setToolTipText("Data Type");
		comboViewer.setInput(DataType.values());
		comboViewer.addSelectionChangedListener(_ -> {
			Object selection = comboViewer.getStructuredSelection().getFirstElement();
			if(selection instanceof DataType dt) {
				dataType = dt;
			}
		});
		comboDataType.set(comboViewer);
	}

	private void createComboSinkField(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof String key) {
					return libraryFields.getOrDefault(key, key);
				}
				return null;
			}
		});

		GridData gridData = new GridData();
		gridData.widthHint = 160;
		comboViewer.getCombo().setLayoutData(gridData);
		comboViewer.getCombo().setToolTipText("Sink Field");
		comboViewer.setInput(libraryFields.keySet().toArray());
		comboViewer.addSelectionChangedListener(_ -> {
			Object selection = comboViewer.getStructuredSelection().getFirstElement();
			if(selection instanceof String field) {
				sinkField = field;
			}
		});

		comboSinkField.set(comboViewer);
	}

	private void updateInput() {

		checkboxUse.get().setSelection(use);
		comboSourceField.get().setSelection(new StructuredSelection(sourceField));
		textRegularExpression.get().setText(regularExpression);
		spinnerGroupIndex.get().setSelection(groupIndex);
		comboDataType.get().setSelection(new StructuredSelection(dataType));
		comboSinkField.get().setSelection(new StructuredSelection(sinkField));
	}
}