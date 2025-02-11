/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - improve data handling
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ExtendedMeasurementResultUI extends Composite implements IExtendedPartUI {

	private static final String NO_SELECTION = "--";
	//
	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarResults = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarResults = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboMeasurementResults = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteSelected = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteAll = new AtomicReference<>();
	private AtomicReference<Button> buttonClipboard = new AtomicReference<>();
	private AtomicReference<MeasurementResultUI> measurementResultsControl = new AtomicReference<>();
	//
	private IChromatogram chromatogram = null;

	public ExtendedMeasurementResultUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(IChromatogram chromatogram) {

		this.chromatogram = chromatogram;
		updateInput();
	}

	public ComboViewer getComboMeasurementResults() {

		return comboMeasurementResults.get();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createToolbarInfo(this);
		createTable(this);
		createToolbarResults(this);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfo, buttonToolbarInfo.get(), IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarResults, buttonToolbarResults.get(), IMAGE_RESULTS, TOOLTIP_RESULTS, false);
		//
		updateInput();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonToggleToolbarInfo(composite);
		createResultComboViewer(composite);
		createButtonDeleteSelected(composite);
		createButtonDeleteAll(composite);
		createButtonClipboard(composite);
		createButtonToggleToolbarResults(composite);
	}

	private void createButtonToggleToolbarInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createButtonToggleToolbarResults(Composite parent) {

		buttonToolbarResults.set(createButtonToggleToolbar(parent, toolbarResults, IMAGE_RESULTS, TOOLTIP_RESULTS));
	}

	private void createToolbarInfo(Composite parent) {

		toolbarInfo.set(createInformationUI(parent));
	}

	private void createTable(Composite parent) {

		MeasurementResultUI measurementResultsUI = new MeasurementResultUI(parent, SWT.VIRTUAL | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		measurementResultsUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		measurementResultsControl.set(measurementResultsUI);
	}

	private void createToolbarResults(Composite parent) {

		toolbarResults.set(createInformationUI(parent));
	}

	private InformationUI createInformationUI(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return informationUI;
	}

	private void createResultComboViewer(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.PUSH);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IMeasurementResult<?> measurementResult) {
					return measurementResult.getName();
				} else if(element instanceof String text) {
					return text;
				}
				//
				return super.getText(element);
			}
		});
		//
		combo.setToolTipText("Show the available measurement results.");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		combo.setLayoutData(gridData);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof IMeasurementResult<?> measurementResult) {
					update(measurementResult);
				} else {
					update(null);
				}
			}
		});
		//
		comboMeasurementResults.set(comboViewer);
	}

	private void createButtonDeleteSelected(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected measurement result.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogram != null) {
					IMeasurementResult<?> measurementResult = getSelection();
					if(measurementResult != null) {
						if(MessageDialog.openQuestion(e.display.getActiveShell(), "Measurement Result", "Would you like to delete the selected result?")) {
							chromatogram.deleteMeasurementResult(measurementResult.getIdentifier());
							updateComboViewerInput();
							update(null);
						}
					}
				}
			}
		});
		//
		buttonDeleteSelected.set(button);
	}

	private void createButtonDeleteAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete all measurement result.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE_ALL, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogram != null) {
					if(MessageDialog.openQuestion(e.display.getActiveShell(), "Measurement Result", "Would you like to delete all results?")) {
						chromatogram.removeAllMeasurementResults();
						updateComboViewerInput();
						update(null);
					}
				}
			}
		});
		//
		buttonDeleteAll.set(button);
	}

	private void createButtonClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Copy the measurement result ids to clipboard.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(chromatogram != null) {
					String lineDelimiter = OperatingSystemUtils.getLineDelimiter();
					List<IMeasurementResult<?>> measurementResults = getMeasurementResults(chromatogram);
					StringBuilder builder = new StringBuilder();
					/*
					 * Header
					 */
					builder.append("Name");
					builder.append("\t");
					builder.append("Identifier");
					builder.append(lineDelimiter);
					/*
					 * Data
					 */
					Iterator<IMeasurementResult<?>> iterator = measurementResults.iterator();
					while(iterator.hasNext()) {
						IMeasurementResult<?> measurementResult = iterator.next();
						builder.append(measurementResult.getName());
						builder.append("\t");
						builder.append(measurementResult.getIdentifier());
						if(iterator.hasNext()) {
							builder.append(lineDelimiter);
						}
					}
					/*
					 * Clipboard
					 */
					TextTransfer textTransfer = TextTransfer.getInstance();
					Object[] data = new Object[]{builder.toString()};
					Transfer[] dataTypes = new Transfer[]{textTransfer};
					Clipboard clipboard = new Clipboard(e.display);
					clipboard.setContents(data, dataTypes);
					clipboard.dispose();
				}
			}
		});
		//
		buttonClipboard.set(button);
	}

	private IMeasurementResult<?> getSelection() {

		IMeasurementResult<?> measurementResult = null;
		//
		Object object = comboMeasurementResults.get().getStructuredSelection().getFirstElement();
		if(object instanceof IMeasurementResult<?> result) {
			measurementResult = result;
		}
		//
		return measurementResult;
	}

	private void update(IMeasurementResult<?> measurementResult) {

		updateLabel(measurementResult);
		measurementResultsControl.get().update(measurementResult);
		updateWidgets();
	}

	private void updateInput() {

		toolbarInfo.get().setText(chromatogram != null ? ChromatogramDataSupport.getChromatogramLabel(chromatogram) : "");
		updateComboViewerInput();
		updateWidgets();
	}

	private void updateWidgets() {

		boolean enabled = false;
		if(chromatogram != null) {
			enabled = !chromatogram.getMeasurementResults().isEmpty();
		}
		buttonDeleteAll.get().setEnabled(enabled);
		buttonClipboard.get().setEnabled(enabled);
		//
		IMeasurementResult<?> measurementResult = getSelection();
		buttonDeleteSelected.get().setEnabled(measurementResult != null);
	}

	private void updateComboViewerInput() {

		List<IMeasurementResult<?>> results = getMeasurementResults(chromatogram);
		List<Object> measurementResults = new ArrayList<>();
		measurementResults.add(NO_SELECTION);
		measurementResults.addAll(results);
		//
		IStructuredSelection structuredSelection = comboMeasurementResults.get().getStructuredSelection();
		Object object = structuredSelection.getFirstElement();
		comboMeasurementResults.get().setSelection(StructuredSelection.EMPTY);
		comboMeasurementResults.get().setInput(measurementResults);
		/*
		 * Get the selection
		 */
		IMeasurementResult<?> selection = null;
		int index = 0;
		if(object instanceof IMeasurementResult<?> result) {
			/*
			 * Validation
			 */
			exitloop:
			for(int i = 0; i < results.size(); i++) {
				IMeasurementResult<?> measurementResult = results.get(i);
				if(measurementResult.getName().equals(result.getName())) {
					selection = measurementResult;
					index = i + 1;
					break exitloop;
				}
			}
		}
		/*
		 * Set the selection
		 */
		comboMeasurementResults.get().setSelection(selection == null ? StructuredSelection.EMPTY : new StructuredSelection(selection));
		comboMeasurementResults.get().getCombo().select(index);
	}

	private List<IMeasurementResult<?>> getMeasurementResults(IChromatogram chromatogram) {

		List<IMeasurementResult<?>> measurementResults = new ArrayList<>();
		//
		if(chromatogram != null) {
			measurementResults.addAll(chromatogram.getMeasurementResults().stream().filter(m -> m.isVisible()).collect(Collectors.toList()));
			Collections.sort(measurementResults, (r1, r2) -> r1.getName().compareTo(r2.getName()));
		}
		//
		return measurementResults;
	}

	private void updateLabel(IMeasurementResult<?> measurementResult) {

		toolbarResults.get().setText(measurementResult != null ? measurementResult.getDescription() : "");
	}
}