/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.ChromatogramSelection;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.EditorUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageChromatogram;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ExtendedScanBrowseUI extends Composite implements IExtendedPartUI {

	private Composite toolbarInfo;
	private Label labelInfo;
	//
	private Button buttonPreviousScan;
	private ComboViewer comboViewerType;
	private ComboViewer comboViewerSource;
	private Button buttonNextScan;
	private ScanChartUI scanChartUI;
	//
	private IChromatogramSelection<?, ?> chromatogramSelection;
	private int masterRetentionTime;
	//
	private final ScanDataSupport scanDataSupport = new ScanDataSupport();

	private enum Type {

		EXTERNAL("Editors"), //
		INTERNAL("References"), //
		BOTH("Both"); //

		private String label = "";

		private Type(String label) {

			this.label = label;
		}

		public String getLabel() {

			return label;
		}
	}

	public ExtendedScanBrowseUI(Composite parent, int type) {

		super(parent, type);
		createControl();
	}

	public void update(IChromatogramSelection<?, ?> chromatogramSelection) {

		this.chromatogramSelection = chromatogramSelection;
		IScan scan = chromatogramSelection != null ? chromatogramSelection.getSelectedScan() : null;
		masterRetentionTime = (scan != null) ? scan.getRetentionTime() : 0;
		updateScan(scan);
	}

	@Override
	public void dispose() {

		scanChartUI.dispose();
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		//
		createToolbarMain(composite);
		toolbarInfo = createToolbarInfo(composite);
		scanChartUI = createScanChart(composite);
		//
		PartSupport.setCompositeVisibility(toolbarInfo, true);
	}

	private Composite createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(5, false));
		//
		comboViewerType = createComboViewerType(composite);
		buttonPreviousScan = createPreviousReferenceScanButton(composite);
		comboViewerSource = createComboViewerSource(composite);
		buttonNextScan = createNextReferenceScanButton(composite);
		createSettingsButton(composite);
		//
		return composite;
	}

	private ComboViewer createComboViewerType(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof Type type) {
					return type.getLabel();
				}
				return null;
			}
		});
		//
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Select the source of referenced scans.");
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IScan scan = chromatogramSelection != null ? chromatogramSelection.getSelectedScan() : null;
				updateScan(scan);
			}
		});
		//
		comboViewer.setInput(Type.values());
		combo.select(0);
		//
		return comboViewer;
	}

	private Button createPreviousReferenceScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Get the scan of the previous source.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PREVIOUS_YELLOW, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectScan(-1);
			}
		});
		return button;
	}

	private ComboViewer createComboViewerSource(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {

				String label = "";
				if(element instanceof IChromatogramSelection<?, ?> chromatogramSelection) {
					IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
					label = ChromatogramDataSupport.getReferenceLabel(chromatogram, -1, false);
				}
				return label;
			}
		});
		//
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Editor References Scan");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectScan(0);
			}
		});
		return comboViewer;
	}

	private Button createNextReferenceScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Get the scan of the next source.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_NEXT_YELLOW, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectScan(1);
			}
		});
		return button;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList( //
				PreferencePageScans.class, //
				PreferencePageChromatogram.class //
		), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void applySettings() {

	}

	private Composite createToolbarInfo(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));
		//
		labelInfo = createLabel(composite);
		//
		return composite;
	}

	private Label createLabel(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return label;
	}

	private ScanChartUI createScanChart(Composite parent) {

		ScanChartUI scanChartUI = new ScanChartUI(parent, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		return scanChartUI;
	}

	private void selectScan(int moveIndex) {

		Combo combo = comboViewerSource.getCombo();
		int index = combo.getSelectionIndex() + moveIndex;
		//
		if(moveIndex < 0) {
			index = (index < 0) ? 0 : index;
			combo.select(index);
		} else if(moveIndex > 0) {
			index = (index > combo.getItemCount()) ? combo.getItemCount() : index;
			combo.select(index);
		}
		/*
		 * Update the chart and label
		 */
		IScan referenceScan = null;
		IStructuredSelection structuredSelection = comboViewerSource.getStructuredSelection();
		Object object = structuredSelection.getFirstElement();
		if(object instanceof IChromatogramSelection<?, ?> chromatogramSelection) {
			IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
			int scanNumber = chromatogram.getScanNumber(masterRetentionTime);
			referenceScan = chromatogram.getScan(scanNumber);
		}
		//
		updateChart(referenceScan);
		updatePreviousAndNextButton();
	}

	private void updateScan(IScan scan) {

		updateLabel(scan);
		updateChart(scan);
		updateComboViewer();
		updatePreviousAndNextButton();
	}

	private void updateLabel(IScan scan) {

		labelInfo.setText(scanDataSupport.getScanLabel(scan));
	}

	private void updateChart(IScan scan) {

		scanChartUI.setInput(scan);
		scanChartUI.getBaseChart().redraw();
	}

	@SuppressWarnings("rawtypes")
	private void updateComboViewer() {

		Type type = getSelectedType();
		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		/*
		 * Add this selection as the first entry.
		 */
		if(chromatogramSelection != null) {
			chromatogramSelections.add(chromatogramSelection);
		}
		/*
		 * Add the references
		 */
		switch(type) {
			case INTERNAL:
				chromatogramSelections.addAll(extractInternal());
				break;
			case EXTERNAL:
				chromatogramSelections.addAll(extractExternal());
				break;
			default:
				chromatogramSelections.addAll(extractInternal());
				chromatogramSelections.addAll(extractExternal());
				break;
		}
		/*
		 * Set and select the references
		 */
		Combo combo = comboViewerSource.getCombo();
		comboViewerSource.setInput(chromatogramSelections);
		if(!chromatogramSelections.isEmpty()) {
			combo.select(0);
		}
	}

	private void updatePreviousAndNextButton() {

		Combo combo = comboViewerSource.getCombo();
		buttonPreviousScan.setEnabled(combo.getSelectionIndex() > 0);
		buttonNextScan.setEnabled(combo.getSelectionIndex() < combo.getItemCount() - 1);
	}

	private Type getSelectedType() {

		Object object = comboViewerType.getStructuredSelection().getFirstElement();
		if(object instanceof Type type) {
			return type;
		}
		return Type.BOTH;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private List<IChromatogramSelection<?, ?>> extractInternal() {

		List<IChromatogramSelection<?, ?>> chromatogramSelections = new ArrayList<>();
		if(chromatogramSelection != null) {
			Object object = chromatogramSelection.getChromatogram();
			if(object instanceof IChromatogram<?> chromatogram) {
				List<IChromatogram<?>> chromatograms = chromatogram.getReferencedChromatograms();
				for(IChromatogram<?> chromatogramReference : chromatograms) {
					chromatogramSelections.add(new ChromatogramSelection(chromatogramReference));
				}
			}
		}
		return chromatogramSelections;
	}

	@SuppressWarnings("rawtypes")
	private List<IChromatogramSelection> extractExternal() {

		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		EditorUpdateSupport editorUpdateSupport = new EditorUpdateSupport();
		if(chromatogramSelection != null) {
			IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
			for(IChromatogramSelection<?, ?> chromatogramSelectionEditor : editorUpdateSupport.getChromatogramSelections()) {
				if(chromatogram != chromatogramSelectionEditor.getChromatogram()) {
					chromatogramSelections.add(chromatogramSelectionEditor);
				}
			}
		} else {
			chromatogramSelections.addAll(editorUpdateSupport.getChromatogramSelections());
		}
		//
		return chromatogramSelections;
	}
}