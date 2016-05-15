/*******************************************************************************
 * Copyright (c) 2016 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards;

import org.eclipse.chemclipse.support.ui.wizards.AbstractExtendedWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PageCalibrationSettings extends AbstractExtendedWizardPage {

	private IRetentionIndexWizardElements wizardElements;
	private Text textCalibrationFile;
	private Button buttonSelectCalibrationFile;

	public PageCalibrationSettings(IRetentionIndexWizardElements wizardElements) {
		//
		super(PageCalibrationSettings.class.getName());
		setTitle("Calibration Settings");
		setDescription("Please select the calibration settings.");
		this.wizardElements = wizardElements;
	}

	@Override
	public boolean canFinish() {

		return true;
	}

	@Override
	public void setDefaultValues() {

	}

	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		if(visible) {
			System.out.println(wizardElements.getFileName());
			validateSelection();
		}
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		//
		createCalibrationFileField(composite);
		createStartRetentionIndexField(composite);
		createStopRetentionIndexField(composite);
		createPeakIdentificationField(composite);
		//
		validateSelection();
		setControl(composite);
	}

	private void createCalibrationFileField(Composite composite) {

		Button checkBoxUseExistingCalFile = new Button(composite, SWT.CHECK);
		checkBoxUseExistingCalFile.setText("Use existing *.cal file for improved detection");
		checkBoxUseExistingCalFile.setEnabled(true);
		checkBoxUseExistingCalFile.setLayoutData(getGridData());
		checkBoxUseExistingCalFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean enabled = checkBoxUseExistingCalFile.getSelection();
				textCalibrationFile.setEnabled(enabled);
				buttonSelectCalibrationFile.setEnabled(enabled);
			}
		});
		//
		textCalibrationFile = new Text(composite, SWT.BORDER);
		textCalibrationFile.setText("");
		textCalibrationFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textCalibrationFile.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {

				validateSelection();
			}
		});
		//
		buttonSelectCalibrationFile = new Button(composite, SWT.PUSH);
		buttonSelectCalibrationFile.setText("Select *.cal");
		buttonSelectCalibrationFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText("Select an existing *.cal template file.");
				fileDialog.setFilterExtensions(new String[]{"*.cal", "*.CAL"});
				fileDialog.setFilterNames(new String[]{"AMDIS Calibration *.cal", "AMDIS Calibration *.CAL"});
				fileDialog.setFilterPath(""); // TODO persist path
				String pathname = fileDialog.open();
				if(pathname != null) {
					fileDialog.getFilterPath(); // TODO persist path
					textCalibrationFile.setText(pathname);
				}
			}
		});
	}

	private void createStartRetentionIndexField(Composite composite) {

		Label label = new Label(composite, SWT.NONE);
		label.setText("Start Index");
		label.setLayoutData(getGridData());
		//
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(getGridData());
		combo.setItems(wizardElements.getAvailableStandards());
	}

	private void createStopRetentionIndexField(Composite composite) {

		Label label = new Label(composite, SWT.NONE);
		label.setText("Stop Index");
		label.setLayoutData(getGridData());
		//
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(getGridData());
		combo.setItems(wizardElements.getAvailableStandards());
	}

	private void createPeakIdentificationField(Composite composite) {

		Button checkBoxUseExistingPeaks = new Button(composite, SWT.CHECK);
		checkBoxUseExistingPeaks.setText("Use existing peaks in chromatogram if available.");
		checkBoxUseExistingPeaks.setEnabled(true);
		checkBoxUseExistingPeaks.setLayoutData(getGridData());
		checkBoxUseExistingPeaks.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("Use peaks.");
			}
		});
	}

	private GridData getGridData() {

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.verticalIndent = 5;
		return gridData;
	}

	private void validateSelection() {

		String message = null;
		/*
		 * Updates the status
		 */
		updateStatus(message);
	}
}
