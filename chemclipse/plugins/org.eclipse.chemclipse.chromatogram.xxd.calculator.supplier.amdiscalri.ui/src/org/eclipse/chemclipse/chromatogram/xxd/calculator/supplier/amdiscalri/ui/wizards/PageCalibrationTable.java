/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.CalibrationFile;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.RetentionIndexExtractor;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.StandardsReader;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.columns.ISeparationColumnIndices;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.support.ui.wizards.AbstractExtendedWizardPage;
import org.eclipse.chemclipse.ux.extension.xxd.ui.calibration.ExtendedRetentionIndexListUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.calibration.RetentionIndexTableViewerUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.custom.ChromatogramPeakChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class PageCalibrationTable extends AbstractExtendedWizardPage {

	private RetentionIndexWizardElements wizardElements;

	private ChromatogramPeakChart chromatogramPeakChart;
	private ExtendedRetentionIndexListUI extendedRetentionIndexTableViewerUI;

	public PageCalibrationTable(RetentionIndexWizardElements wizardElements) {

		super(PageCalibrationTable.class.getName());
		setTitle("Calibration Table");
		setDescription("Please verify the calibration table.");
		this.wizardElements = wizardElements;
	}

	@Override
	public boolean canFinish() {

		return wizardElements.getExportFilePath() != null;
	}

	@Override
	public void setDefaultValues() {

	}

	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		if(visible) {
			IChromatogramSelection chromatogramSelection = wizardElements.getChromatogramSelection();
			if(chromatogramSelection != null && chromatogramSelection.getChromatogram() != null) {
				IChromatogram chromatogram = chromatogramSelection.getChromatogram();
				updateChromatogramChart(chromatogramSelection);
				RetentionIndexExtractor retentionIndexExtractor = new RetentionIndexExtractor();
				ISeparationColumnIndices separationColumnIndices = retentionIndexExtractor.extract(chromatogram, false, true);
				wizardElements.setSeparationColumnIndices(separationColumnIndices);
				extendedRetentionIndexTableViewerUI.setInput(separationColumnIndices);
			}
			validateSelection();
		}
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		composite.setLayout(new GridLayout(1, false));

		createChromatogramField(composite);
		createTableField(composite);
		createCalibrationFileField(composite);

		validateSelection();
		setControl(composite);
	}

	private void createCalibrationFileField(Composite composite) {

		Composite parent = new Composite(composite, SWT.NONE);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new GridLayout(3, false));

		Label labelFile = new Label(parent, SWT.NONE);
		labelFile.setText("Save as:");

		Text fileText = new Text(parent, SWT.BORDER);
		fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button buttonBrowse = new Button(parent, SWT.PUSH);
		buttonBrowse.setText("Browse...");

		buttonBrowse.addListener(SWT.Selection, _ -> {
			String selected = openFileDialog(getShell());
			if(selected != null) {
				fileText.setText(selected);
				wizardElements.setExportFilePath(new File(selected));
			}
			validateSelection();
		});
	}

	private String openFileDialog(Shell shell) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText("Retention Index");
		fileDialog.setFileName("RetentionIndices_" + new Date().getTime());
		fileDialog.setFilterExtensions(CalibrationFile.FILTER_EXTENSION);
		fileDialog.setFilterNames(CalibrationFile.FILTER_NAME);
		return fileDialog.open();
	}

	private void createChromatogramField(Composite composite) {

		Composite parent = new Composite(composite, SWT.NONE);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new FillLayout());
		chromatogramPeakChart = new ChromatogramPeakChart(parent, SWT.BORDER);
	}

	private void createTableField(Composite composite) {

		extendedRetentionIndexTableViewerUI = new ExtendedRetentionIndexListUI(composite, SWT.NONE);
		extendedRetentionIndexTableViewerUI.setInput(new StandardsReader().getStandardsList());
		extendedRetentionIndexTableViewerUI.setLayoutData(new GridData(GridData.FILL_BOTH));

		RetentionIndexTableViewerUI retentionIndexTableViewerUI = extendedRetentionIndexTableViewerUI.getRetentionIndexTableViewerUI();
		retentionIndexTableViewerUI.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Table table = retentionIndexTableViewerUI.getTable();
				int index = table.getSelectionIndex();
				TableItem tableItem = table.getItem(index);
				Object object = tableItem.getData();
				if(object instanceof IRetentionIndexEntry retentionIndexEntry) {
					int retentionTime = retentionIndexEntry.getRetentionTime();
					IChromatogramSelection chromatogramSelection = wizardElements.getChromatogramSelection();
					if(chromatogramSelection != null && chromatogramSelection.getChromatogram() != null) {
						IChromatogram chromatogram = chromatogramSelection.getChromatogram();
						IPeak selectedPeak = getSelectedPeak(chromatogram, retentionTime);
						if(selectedPeak != null) {
							chromatogramSelection.setSelectedPeak(selectedPeak);
							List<IPeak> selectedPeaks = new ArrayList<>();
							selectedPeaks.add(selectedPeak);
							updateSelectedPeaksInChart(selectedPeaks);
						}
					}
				}
			}
		});
	}

	/**
	 * May return null.
	 *
	 * @param chromatogramMSD
	 * @param retentionTime
	 * @return {@link IChromatogramPeakMSD}
	 */
	private IPeak getSelectedPeak(IChromatogram chromatogram, int retentionTime) {

		for(IPeak peak : chromatogram.getPeaks()) {
			if(peak.getPeakModel().getRetentionTimeAtPeakMaximum() == retentionTime) {
				return peak;
			}
		}
		return null;
	}

	private void validateSelection() {

		String message = null;
		File file = wizardElements.getExportFilePath();
		if(file == null) {
			message = "Please select a file location.";
		}
		/*
		 * Updates the status
		 */
		updateStatus(message);
	}

	private void updateChromatogramChart(IChromatogramSelection chromatogramSelection) {

		chromatogramPeakChart.updateChromatogram(chromatogramSelection);
	}

	private void updateSelectedPeaksInChart(List<IPeak> selectedPeaks) {

		chromatogramPeakChart.updatePeaks(selectedPeaks);
	}
}
