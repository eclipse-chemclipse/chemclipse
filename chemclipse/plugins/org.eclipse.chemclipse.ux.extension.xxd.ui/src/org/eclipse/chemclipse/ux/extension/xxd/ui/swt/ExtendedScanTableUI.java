/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.TracesSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ScanSignalWSD;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class ExtendedScanTableUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedScanTableUI.class);
	//
	private final ScanDataSupport scanDataSupport = new ScanDataSupport();
	//
	private AtomicReference<Composite> toolbarMain = new AtomicReference<>();
	private Button buttonToolbarInfo;
	private AtomicReference<InformationUI> toolbarInfoTop = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoBottom = new AtomicReference<>();
	private Button buttonToolbarSearch;
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private Button buttonToolbarEdit;
	private AtomicReference<Composite> toolbarEdit = new AtomicReference<>();
	private Button buttonCopyTraces;
	private Button buttonSaveScan;
	private ScanWebIdentifierUI scanWebIdentifierUI; // show database link
	//
	private CLabel labelOptimized;
	private Button buttonDeleteOptimized;
	//
	private Label labelX;
	private Text textX;
	private Label labelY;
	private Text textY;
	/*
	 * The object could be a IScan or IPeak
	 */
	private ScanTableUI scanTableUI;
	private Object object;
	//
	private DeleteMenuEntry deleteMenuEntry;
	private DeleteKeyEventProcessor deleteKeyEventProcessor;
	/*
	 * Set whether to force the edit modus.
	 */
	private boolean forceEnableEditModus = false;
	private boolean fireUpdate = true;
	//
	private EditListener editListener = null;

	private class DeleteMenuEntry implements ITableMenuEntry {

		@Override
		public String getName() {

			return "Delete Traces";
		}

		@Override
		public String getCategory() {

			return ""; // Must be empty to be placed on the main menu level.
		}

		@Override
		public void execute(ExtendedTableViewer extendedTableViewer) {

			deleteTraces(extendedTableViewer.getTable().getShell());
		}
	}

	private class DeleteKeyEventProcessor implements IKeyEventProcessor {

		@Override
		public void handleEvent(ExtendedTableViewer extendedTableViewer, KeyEvent e) {

			if(e.keyCode == SWT.DEL) {
				deleteTraces(e.display.getActiveShell());
			}
		}
	}

	public ExtendedScanTableUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	private String getLastTopic(List<String> topics) {

		Collections.reverse(topics);
		for(String topic : topics) {
			if(topic.equals(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION)) {
				return topic;
			}
			if(topic.equals(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION)) {
				return topic;
			}
		}
		return "";
	}

	@Override
	@Focus
	public boolean setFocus() {

		boolean focus = super.setFocus();
		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));
		if(!objects.isEmpty()) {
			object = objects.get(0);
			updateObject();
		}
		return focus;
	}

	public void setInput(Object object) {

		this.object = object;
		updateObject();
	}

	public void addEditListener(EditListener editListener) {

		this.editListener = editListener;
	}

	/**
	 * By default, an update is fired when modifying the scan.
	 * If this value is set to false, no update will be fired.
	 * 
	 * @param fireUpdate
	 */
	protected void setFireUpdate(boolean fireUpdate) {

		this.fireUpdate = fireUpdate;
	}

	protected void forceEnableEditModus(boolean forceEnableEditModus) {

		this.forceEnableEditModus = forceEnableEditModus;
		enableEditModus();
	}

	/**
	 * Enable or disable the edit functionality.
	 * It is disabled by default.
	 * 
	 * @param enabled
	 */
	private void enableEditModus(boolean enabled) {

		enableToolbar(toolbarEdit, buttonToolbarEdit, IMAGE_EDIT, TOOLTIP_EDIT, enabled);
		buttonToolbarEdit.setEnabled(enabled);
		//
		scanTableUI.setEditEnabled(enabled);
		ITableSettings tableSettings = scanTableUI.getTableSettings();
		if(enabled) {
			tableSettings.addMenuEntry(deleteMenuEntry);
			tableSettings.addKeyEventProcessor(deleteKeyEventProcessor);
		} else {
			tableSettings.removeMenuEntry(deleteMenuEntry);
			tableSettings.removeKeyEventProcessor(deleteKeyEventProcessor);
		}
		//
		scanTableUI.applySettings(tableSettings);
		Composite main = toolbarMain.get();
		main.layout(true);
		main.redraw();
	}

	private void updateObject() {

		setInfoTop();
		setInfoBottom();
		scanTableUI.setInput(getScan());
		scanWebIdentifierUI.setInput(getScan());
		updateEditFields();
		updateButtonStatus();
		//
		if(toolbarSearch.get().isVisible()) {
			applySearch();
		}
	}

	private void applySearch() {

		String searchText = toolbarSearch.get().getSearchText();
		boolean caseSensitive = toolbarSearch.get().isSearchCaseSensitive();
		scanTableUI.setSearchText(searchText, caseSensitive);
	}

	private void updateEditFields() {

		List<TableViewerColumn> tableViewerColumns = scanTableUI.getTableViewerColumns();
		if(tableViewerColumns.size() >= 2) {
			/*
			 * Add Signal
			 */
			String titleX = tableViewerColumns.get(0).getColumn().getText();
			labelX.setText(titleX + ":");
			textX.setToolTipText(titleX);
			//
			String titleY = tableViewerColumns.get(1).getColumn().getText();
			labelY.setText(titleY + ":");
			textY.setToolTipText(titleY);
			//
			toolbarEdit.get().layout(true);
		}
	}

	private boolean enableEditModus() {

		boolean isLibraryMassSpectrum = false;
		enableEditModus(false);
		//
		IScan scan = getScan();
		if(scan != null) {
			isLibraryMassSpectrum = (scan instanceof ILibraryMassSpectrum);
			if(forceEnableEditModus || isLibraryMassSpectrum) {
				enableEditModus(true);
			}
		}
		return isLibraryMassSpectrum;
	}

	private void setInfoTop() {

		IScan scan = getScan();
		boolean isLibraryMassSpectrum = enableEditModus();
		//
		if(forceEnableEditModus || isLibraryMassSpectrum) {
			String editInformation = scanTableUI.isEditEnabled() ? "Edit is enabled." : "Edit is disabled.";
			toolbarInfoTop.get().setText(scanDataSupport.getScanLabel(scan) + " - " + editInformation);
		} else {
			toolbarInfoTop.get().setText(scanDataSupport.getScanLabel(scan));
		}
	}

	private void setInfoBottom() {

		String signals;
		IScan scan = getScan();
		if(scan instanceof IScanCSD) {
			signals = "1";
		} else if(scan instanceof IScanMSD) {
			IScanMSD scanMSD = getScanMSD();
			IScanMSD optimizedScanMSD = getOptimizedScanMSD();
			signals = Integer.toString(optimizedScanMSD != null ? optimizedScanMSD.getNumberOfIons() : scanMSD.getNumberOfIons());
		} else if(scan instanceof IScanWSD scanWSD) {
			signals = Integer.toString(scanWSD.getNumberOfScanSignals());
		} else if(scan instanceof IScanVSD scanVSD) {
			signals = Integer.toString(scanVSD.getProcessedSignals().size());
		} else {
			signals = "--";
		}
		toolbarInfoBottom.get().setText("Signals: " + signals);
	}

	private boolean isSaveEnabled() {

		IScanMSD scanMSD = getScanMSD();
		return scanMSD != null;
	}

	private void createControl() {

		setLayout(new FillLayout());
		//
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		//
		deleteMenuEntry = new DeleteMenuEntry();
		deleteKeyEventProcessor = new DeleteKeyEventProcessor();
		//
		createToolbarMain(composite);
		createToolbarInfoTop(composite);
		createToolbarEdit(composite);
		createToolbarSearch(composite);
		createTable(composite);
		createToolbarInfoBottom(composite);
		//
		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfoTop, buttonToolbarInfo, IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarSearch, buttonToolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		enableToolbar(toolbarEdit, buttonToolbarEdit, IMAGE_EDIT, TOOLTIP_EDIT, false);
		enableToolbar(toolbarInfoBottom, buttonToolbarInfo, IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		//
		enableEditModus(false); // Disable the edit modus by default.
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(10, false));
		//
		labelOptimized = createInfoLabelOptimized(composite);
		buttonToolbarInfo = createButtonToggleToolbar(composite, Arrays.asList(toolbarInfoTop, toolbarInfoBottom), IMAGE_INFO, TOOLTIP_INFO);
		buttonToolbarSearch = createButtonToggleToolbar(composite, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH);
		buttonToolbarEdit = createButtonToggleToolbar(composite, toolbarEdit, IMAGE_EDIT, TOOLTIP_EDIT);
		buttonCopyTraces = createButtonCopyTracesClipboard(composite);
		scanWebIdentifierUI = createScanWebIdentifierUI(composite);
		createResetButton(composite);
		buttonSaveScan = createSaveButton(composite);
		buttonDeleteOptimized = createDeleteOptimizedButton(composite);
		createSettingsButton(composite);
		//
		toolbarMain.set(composite);
	}

	private CLabel createInfoLabelOptimized(Composite parent) {

		CLabel label = createInfoLabel(parent);
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				if(!"".equals(label.getText())) {
					deleteOptimizedScan(e.widget.getDisplay());
				}
			}
		});
		return label;
	}

	private CLabel createInfoLabel(Composite parent) {

		CLabel label = new CLabel(parent, SWT.CENTER);
		label.setForeground(Colors.RED);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return label;
	}

	private void deleteOptimizedScan(Display display) {

		IScan scan = getScan();
		if(scan instanceof IScanMSD scanMSD) {
			if(MessageDialog.openQuestion(display.getActiveShell(), "Optimized Scan", "Would you like to delete the optimized scan?")) {
				scanMSD.setOptimizedMassSpectrum(null);
				updateObject();
			}
		}
	}

	private void updateButtonStatus() {

		IScan scan = getScan();
		buttonCopyTraces.setEnabled(scan instanceof IScanMSD || scan instanceof IScanWSD);
		buttonSaveScan.setEnabled(isSaveEnabled());
		buttonDeleteOptimized.setEnabled(isOptimizedScan());
		updateLabel(labelOptimized, isOptimizedScan() ? "Optimized" : "");
	}

	private void updateLabel(CLabel label, String message) {

		label.setText(message);
		if("".equals(message)) {
			Color color = null;
			label.setBackground(color);
		} else {
			label.setBackground(Colors.LIGHT_YELLOW);
		}
	}

	private boolean isOptimizedScan() {

		return getOptimizedScanMSD() != null;
	}

	private IScan getScan() {

		IScan currentScan = null;
		if(object instanceof IScan scan) {
			currentScan = scan;
		} else if(object instanceof IPeak peak) {
			currentScan = peak.getPeakModel().getPeakMaximum();
		}
		return currentScan;
	}

	private IScanMSD getScanMSD() {

		IScan scan = getScan();
		if(scan instanceof IScanMSD scanMSD) {
			return scanMSD;
		}
		return null;
	}

	private IScanMSD getOptimizedScanMSD() {

		IScanMSD scanMSD = getScanMSD();
		if(scanMSD != null) {
			IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
			if(optimizedMassSpectrum != null) {
				return optimizedMassSpectrum;
			}
		}
		return null;
	}

	private Button createButtonCopyTracesClipboard(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Copy the traces to clipboard.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_COPY_CLIPBOARD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				TracesSupport.copyTracesToClipboard(e.display, getScan());
			}
		});
		//
		return button;
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the scan chart.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private Button createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(object instanceof IScanMSD scanMSD) {
						IScanMSD optimizedScanMSD = scanMSD.getOptimizedMassSpectrum();
						//
						if(optimizedScanMSD != null) {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), optimizedScanMSD, "Scan[optimized]");
						} else {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "Scan");
						}
					} else if(object instanceof IPeakMSD peakMSD) {
						IPeakModelMSD peakModelMSD = peakMSD.getPeakModel();
						IScanMSD scanMSD = peakModelMSD.getPeakMassSpectrum();
						IScanMSD optimizedScanMSD = scanMSD.getOptimizedMassSpectrum();
						//
						if(optimizedScanMSD != null) {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), optimizedScanMSD, "Peak[optimized]");
						} else {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "Peak");
						}
					}
				} catch(NoConverterAvailableException e1) {
					logger.warn(e1);
				}
			}
		});
		return button;
	}

	private Button createDeleteOptimizedButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete the optimized scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteOptimizedScan(e.widget.getDisplay());
			}
		});
		return button;
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageScans.class), new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
		});
	}

	private void createToolbarInfoTop(Composite parent) {

		toolbarInfoTop.set(createToolbarInfo(parent));
	}

	private void createToolbarInfoBottom(Composite parent) {

		toolbarInfoBottom.set(createToolbarInfo(parent));
	}

	private InformationUI createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return informationUI;
	}

	private void createToolbarEdit(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		labelX = createLabel(composite);
		textX = createText(composite);
		labelY = createLabel(composite);
		textY = createText(composite);
		createButtonAdd(composite);
		createButtonDelete(composite);
		//
		toolbarEdit.set(composite);
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener(new ISearchListener() {

			@Override
			public void performSearch(String searchText, boolean caseSensitive) {

				applySearch();
			}
		});
		//
		toolbarSearch.set(searchSupportUI);
	}

	private ScanWebIdentifierUI createScanWebIdentifierUI(Composite parent) {

		return new ScanWebIdentifierUI(parent, SWT.NONE);
	}

	private Text createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	private Label createLabel(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		return label;
	}

	private void createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add the scan signal.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IScan scan = getScan();
				if(scan != null) {
					addSignal(e.display.getActiveShell());
					scanTableUI.updateScan();
					fireEditEvent();
				} else {
					MessageDialog.openError(e.display.getActiveShell(), "Add Signal", "Please load a scan first.");
				}
			}
		});
	}

	private void createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the scan signals.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteTraces(e.display.getActiveShell());
				scanTableUI.updateScan();
				fireEditEvent();
			}
		});
	}

	private void createTable(Composite parent) {

		scanTableUI = new ScanTableUI(parent, SWT.VIRTUAL | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		scanTableUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		Table table = scanTableUI.getTable();
		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				super.widgetSelected(e);
				fireEditEvent();
			}
		});
		//
		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				super.keyReleased(e);
				fireEditEvent();
			}
		});
	}

	private void applySettings() {

		updateObject();
	}

	private void reset() {

		updateObject();
	}

	private void deleteTraces(Shell shell) {

		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setText("Delete Signals");
		messageBox.setMessage("Would you like to delete the selected signals?");
		if(messageBox.open() == SWT.YES) {
			/*
			 * Delete the signal
			 */
			Iterator<?> iterator = scanTableUI.getStructuredSelection().iterator();
			while(iterator.hasNext()) {
				Object object = iterator.next();
				deleteSignal(object);
			}
			//
			scanTableUI.refresh();
			fireScanUpdate();
		}
	}

	private void deleteSignal(Object signal) {

		IScan scan = getScan();
		if(scan instanceof IScanCSD scanCSD) {
			/*
			 * CSD
			 */
			scanCSD.adjustTotalSignal(0.0f);
		} else if(scan instanceof IScanMSD scanMSD) {
			/*
			 * MSD
			 */
			if(signal instanceof IIon ion) {
				scanMSD.removeIon(ion);
			}
		} else if(scan instanceof IScanWSD scanWSD) {
			/*
			 * WSD
			 */
			if(signal instanceof IScanSignalWSD signalWSD) {
				scanWSD.removeScanSignal(signalWSD);
			}
		} else if(scan instanceof IScanVSD) {
			/*
			 * VSD
			 */
			// Not supported yet.
		}
	}

	private void addSignal(Shell shell) {

		String x = textX.getText().trim();
		String y = textY.getText().trim();
		//
		if("".equals(x) || "".equals(y)) {
			MessageDialog.openError(shell, "Add Signal", "The values must be not empty.");
		} else {
			try {
				/*
				 * Add the signal.
				 */
				IScan scan = getScan();
				if(scan instanceof IScanCSD scanCSD) {
					/*
					 * CSD
					 */
					float valueY = Float.parseFloat(y);
					scanCSD.adjustTotalSignal(valueY);
				} else if(scan instanceof IScanMSD scanMSD) {
					/*
					 * MSD
					 */
					double valueX = Double.parseDouble(x);
					float valueY = Float.parseFloat(y);
					scanMSD.addIon(new Ion(valueX, valueY));
				} else if(scan instanceof IScanWSD scanWSD) {
					/*
					 * WSD
					 */
					float valueX = Float.parseFloat(x);
					float valueY = Float.parseFloat(y);
					scanWSD.addScanSignal(new ScanSignalWSD(valueX, valueY));
				} else if(scan instanceof IScanVSD) {
					/*
					 * VSD
					 */
					// Not supported yet.
				}
				//
				textX.setText("");
				textY.setText("");
				scanTableUI.refresh();
				fireScanUpdate();
				//
			} catch(Exception e) {
				MessageDialog.openError(shell, "Add Signal", "Something has gone wrong to add the signal.");
			}
		}
	}

	private void fireScanUpdate() {

		/*
		 * Fire an update.
		 */
		if(fireUpdate) {
			if(object instanceof IScan scan) {
				UpdateNotifierUI.update(getDisplay(), scan);
			} else if(object instanceof IPeak peak) {
				UpdateNotifierUI.update(getDisplay(), peak);
			}
		}
	}

	private void fireEditEvent() {

		if(editListener != null) {
			editListener.modify();
		}
	}
}