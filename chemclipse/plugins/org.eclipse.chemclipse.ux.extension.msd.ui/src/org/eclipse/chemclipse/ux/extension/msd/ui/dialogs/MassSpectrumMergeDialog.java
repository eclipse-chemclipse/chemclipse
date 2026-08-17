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
package org.eclipse.chemclipse.ux.extension.msd.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.support.CalculationType;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.support.CombinedNominalMassSpectrumCalculator;
import org.eclipse.chemclipse.msd.model.support.ICombinedMassSpectrumCalculator;
import org.eclipse.chemclipse.ux.extension.msd.ui.swt.MassSpectrumChartCentroid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MassSpectrumMergeDialog extends TitleAreaDialog {

	private static final int SKIP_ID = IDialogConstants.CLIENT_ID + 1;

	private List<IScanMSD> massSpectra;
	private List<IScanMSD> checkedMassSpectra = new ArrayList<>();
	private List<List<IScanMSD>> groups;
	private int currentGroupIndex = 0;
	private List<List<IScanMSD>> groupsToMerge = new ArrayList<>();

	private AtomicReference<CheckboxTableViewer> tableControl = new AtomicReference<>();
	private AtomicReference<MassSpectrumChartCentroid> chartControl = new AtomicReference<>();

	public MassSpectrumMergeDialog(Shell parentShell, List<IScanMSD> massSpectra) {

		super(parentShell);
		this.massSpectra = massSpectra;
	}

	public MassSpectrumMergeDialog(Shell parentShell, List<List<IScanMSD>> groups, boolean autoMerge) {

		super(parentShell);
		this.groups = groups;
		this.massSpectra = groups.isEmpty() ? Collections.emptyList() : groups.get(0);
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Merge Mass Spectra");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		if(isAutoMergeMode()) {
			updateGroupTitle();
		} else {
			setTitle("Merge Mass Spectra");
		}
		setMessage("Check entries to include in the merge. Select an entry to preview it (red) against the combined spectrum (blue).", IMessageProvider.INFORMATION);
		Composite container = (Composite)super.createDialogArea(parent);
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);

		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTable(sashForm);
		createChart(sashForm);

		sashForm.setWeights(40, 60);

		CheckboxTableViewer tableViewer = tableControl.get();
		tableViewer.setAllChecked(true);
		if(!massSpectra.isEmpty()) {
			tableViewer.getTable().select(0);
		}
		updateChart();

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		if(isAutoMergeMode()) {
			createButton(parent, IDialogConstants.OK_ID, "Merge", true);
			createButton(parent, SKIP_ID, "Skip", false);
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		} else {
			super.createButtonsForButtonBar(parent);
		}
	}

	@Override
	protected void buttonPressed(int buttonId) {

		if(buttonId == SKIP_ID) {
			skipPressed();
		} else {
			super.buttonPressed(buttonId);
		}
	}

	@Override
	protected void okPressed() {

		CheckboxTableViewer tableViewer = tableControl.get();
		if(tableViewer != null) {
			if(isAutoMergeMode()) {
				List<IScanMSD> checked = new ArrayList<>();
				for(Object element : tableViewer.getCheckedElements()) {
					if(element instanceof IScanMSD scan) {
						checked.add(scan);
					}
				}
				if(checked.size() >= 2) {
					groupsToMerge.add(checked);
				}
				advanceOrClose();
			} else {
				for(Object element : tableViewer.getCheckedElements()) {
					if(element instanceof IScanMSD scan) {
						checkedMassSpectra.add(scan);
					}
				}
				super.okPressed();
			}
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(700, 800);
	}

	public List<IScanMSD> getCheckedMassSpectra() {

		return checkedMassSpectra;
	}

	public List<List<IScanMSD>> getGroupsToMerge() {

		return groupsToMerge;
	}

	private boolean isAutoMergeMode() {

		return groups != null;
	}

	private void skipPressed() {

		advanceOrClose();
	}

	private void advanceOrClose() {

		currentGroupIndex++;
		if(currentGroupIndex < groups.size()) {
			loadCurrentGroup();
		} else {
			setReturnCode(OK);
			close();
		}
	}

	private void loadCurrentGroup() {

		List<IScanMSD> group = groups.get(currentGroupIndex);
		CheckboxTableViewer tableViewer = tableControl.get();
		if(tableViewer != null) {
			tableViewer.setInput(group);
			tableViewer.setAllChecked(true);
			if(!group.isEmpty()) {
				tableViewer.getTable().select(0);
			}
			updateChart();
		}
		updateGroupTitle();
	}

	private void updateGroupTitle() {

		setTitle("Merge Mass Spectra (" + (currentGroupIndex + 1) + " / " + groups.size() + ")");
	}

	private void createTable(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createColumn(tableViewer, "Name", 360);
		createColumn(tableViewer, "CAS#", 160);

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new MassSpectrumLabelProvider());
		tableViewer.setInput(massSpectra);

		tableViewer.addCheckStateListener(_ -> updateChart());
		tableViewer.addSelectionChangedListener(_ -> updateChart());

		tableControl.set(tableViewer);
	}

	private void createColumn(CheckboxTableViewer tableViewer, String title, int width) {

		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
	}

	private void createChart(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		MassSpectrumChartCentroid chart = new MassSpectrumChartCentroid(composite, SWT.NONE);
		chartControl.set(chart);
	}

	private void updateChart() {

		CheckboxTableViewer tableViewer = tableControl.get();
		MassSpectrumChartCentroid chart = chartControl.get();
		if(tableViewer == null || chart == null) {
			return;
		}

		ICombinedMassSpectrum combined = buildCombinedSpectrum(tableViewer);
		IScanMSD mirrored = null;
		IStructuredSelection selection = tableViewer.getStructuredSelection();
		if(selection.getFirstElement() instanceof IScanMSD scan) {
			mirrored = scan;
		}

		chart.update(combined, mirrored);
	}

	private ICombinedMassSpectrum buildCombinedSpectrum(CheckboxTableViewer tableViewer) {

		ICombinedMassSpectrumCalculator calculator = new CombinedNominalMassSpectrumCalculator();
		for(Object element : tableViewer.getCheckedElements()) {
			if(element instanceof IScanMSD scan) {
				for(IIon ion : scan.getIons()) {
					calculator.addIon(ion.getIon(), ion.getAbundance());
				}
			}
		}
		return calculator.size() > 0 ? calculator.createMassSpectrum(CalculationType.SUM) : null;
	}

	private static class MassSpectrumLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {

			if(element instanceof IRegularLibraryMassSpectrum libraryMassSpectrum) {
				ILibraryInformation info = libraryMassSpectrum.getLibraryInformation();
				return switch(columnIndex) {
					case 0 -> info.getName();
					case 1 -> info.getCasNumber();
					default -> "";
				};
			}
			return "";
		}
	}
}