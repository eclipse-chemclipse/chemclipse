/*******************************************************************************
 * Copyright (c) 2023, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.ui.dialogs;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.services.RetentionIndexLibrarySettings;
import org.eclipse.chemclipse.model.support.ColumnIndexSupport;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class RetentionIndexLibrarySettingsDialog extends TitleAreaDialog {

	public static final String TITLE = "Retention Index Search";
	public static final String ERROR_NO_LIBRARY = "It was not possible to retrieve library entries.";
	public static final String ERROR_RETENTION_INDEX = "Please select a component with a retention index > 0.";
	public static final String ERROR_SELECT_COMPONENT = "Please select at least one component.";
	//
	private static final int MIN_RETENTION_INDEX_DELTA = 1;
	private static final int MAX_RETENTION_INDEX_DELTA = 50;
	//
	private AtomicReference<Text> textSearchColumnControl = new AtomicReference<>();
	private AtomicReference<Button> buttonCaseSensitiveControl = new AtomicReference<>();
	private AtomicReference<Button> buttonRemoveWhitespaceControl = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerRetentionIndexDeltaControl = new AtomicReference<>();
	//
	private RetentionIndexLibrarySettings retentionIndexLibrarySettings = new RetentionIndexLibrarySettings();

	public RetentionIndexLibrarySettingsDialog(Shell parentShell) {

		super(parentShell);
	}

	public RetentionIndexLibrarySettings getRetentionIndexLibrarySettings() {

		return retentionIndexLibrarySettings;
	}

	@Override
	public void create() {

		super.create();
		setTitle("Retention Index Library Settings");
		setMessage("Select the search settings.");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite)super.createDialogArea(parent);
		//
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		//
		createSectionSearchColumn(composite);
		createSectionCaseSensitive(composite);
		createSectionRemoveWhiteSpace(composite);
		createSectionRetentionIndexDelta(composite);
		//
		initialize();
		//
		return container;
	}

	private void initialize() {

		textSearchColumnControl.get().setText(retentionIndexLibrarySettings.getSearchColumn());
		buttonCaseSensitiveControl.get().setSelection(retentionIndexLibrarySettings.isCaseSensitive());
		buttonRemoveWhitespaceControl.get().setSelection(retentionIndexLibrarySettings.isRemoveWhiteSpace());
		spinnerRetentionIndexDeltaControl.get().setSelection(retentionIndexLibrarySettings.getRetentionIndexDelta());
	}

	private void createSectionSearchColumn(Composite parent) {

		createLabel(parent, "Search Column:");
		//
		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setToolTipText(ColumnIndexSupport.COLUMN_TYPES);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				retentionIndexLibrarySettings.setSearchColumn(text.getText().trim());
			}
		});
		//
		textSearchColumnControl.set(text);
	}

	private void createSectionCaseSensitive(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("Case Sensitive");
		button.setSelection(true);
		button.setToolTipText("Select whether to search case-sensitive or not.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				retentionIndexLibrarySettings.setCaseSensitive(button.getSelection());
			}
		});
		//
		buttonCaseSensitiveControl.set(button);
	}

	private void createSectionRemoveWhiteSpace(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("Remove White-Space");
		button.setSelection(true);
		button.setToolTipText("Select whether to remove the white space or not.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				retentionIndexLibrarySettings.setRemoveWhiteSpace(button.getSelection());
			}
		});
		//
		buttonRemoveWhitespaceControl.set(button);
	}

	private void createSectionRetentionIndexDelta(Composite parent) {

		createLabel(parent, "Retention Index Delta:");
		//
		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinner.setMinimum(MIN_RETENTION_INDEX_DELTA);
		spinner.setMaximum(MAX_RETENTION_INDEX_DELTA);
		spinner.setIncrement(1);
		spinner.setSelection(5);
		//
		spinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int selection = spinner.getSelection();
				if(selection >= MIN_RETENTION_INDEX_DELTA && selection <= MAX_RETENTION_INDEX_DELTA) {
					retentionIndexLibrarySettings.setRetentionIndexDelta(selection);
				}
			}
		});
		//
		spinnerRetentionIndexDeltaControl.set(spinner);
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}
}