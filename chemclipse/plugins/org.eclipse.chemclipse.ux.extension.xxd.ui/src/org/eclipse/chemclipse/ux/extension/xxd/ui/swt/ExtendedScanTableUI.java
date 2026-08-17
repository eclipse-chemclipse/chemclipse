/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakModelMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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

public class ExtendedScanTableUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedScanTableUI.class);

	private AtomicReference<Composite> toolbarMain = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoTop = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoBottom = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarSearch = new AtomicReference<>();
	private AtomicReference<SearchSupportUI> toolbarSearch = new AtomicReference<>();
	private AtomicReference<TracesClipboardUI> tracesClipboardControl = new AtomicReference<>();
	private AtomicReference<Button> buttonSaveScan = new AtomicReference<>();
	private AtomicReference<ScanWebIdentifierUI> scanWebIdentifierUI = new AtomicReference<>();
	private AtomicReference<CLabel> labelOptimized = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteOptimized = new AtomicReference<>();
	private AtomicReference<ScanTableUI> scanTableControl = new AtomicReference<>();
	/*
	 * The object could be a IScan or IPeak
	 */
	private Object object;

	public ExtendedScanTableUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public ScanTableUI getScanTableUI() {

		return scanTableControl.get();
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

	private void updateObject() {

		setInfoTop();
		setInfoBottom();
		scanTableControl.get().setInput(getScan());
		scanWebIdentifierUI.get().setInput(getScan());
		tracesClipboardControl.get().updateOption();
		updateButtonStatus();

		if(toolbarSearch.get().isVisible()) {
			applySearch();
		}
	}

	private void applySearch() {

		String searchText = toolbarSearch.get().getSearchText();
		boolean caseSensitive = toolbarSearch.get().isSearchCaseSensitive();
		scanTableControl.get().setSearchText(searchText, caseSensitive);
	}

	private void setInfoTop() {

		ScanDataSupport scanDataSupport = new ScanDataSupport();
		toolbarInfoTop.get().setText(scanDataSupport.getScanLabel(getScan()));
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

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		createToolbarMain(composite);
		createToolbarInfoTop(composite);
		createToolbarSearch(composite);
		createTable(composite);
		createToolbarInfoBottom(composite);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfoTop, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarSearch, buttonToolbarSearch.get(), IMAGE_SEARCH, TOOLTIP_SEARCH, false);
		enableToolbar(toolbarInfoBottom, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(9, false));

		createInfoLabelOptimized(composite);
		createButtonToggleInfo(composite);
		createButtonToggleSearch(composite);
		createTracesClipboardUI(composite);
		createScanWebIdentifierUI(composite);
		createResetButton(composite);
		createSaveButton(composite);
		createDeleteOptimizedButton(composite);
		createSettingsButton(composite);

		toolbarMain.set(composite);
	}

	private void createButtonToggleInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, Arrays.asList(toolbarInfoTop, toolbarInfoBottom), IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createButtonToggleSearch(Composite parent) {

		buttonToolbarSearch.set(createButtonToggleToolbar(parent, toolbarSearch, IMAGE_SEARCH, TOOLTIP_SEARCH));
	}

	private void createInfoLabelOptimized(Composite parent) {

		CLabel label = createInfoLabel(parent);
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				if(!"".equals(label.getText())) {
					deleteOptimizedScan(e.widget.getDisplay());
				}
			}
		});

		labelOptimized.set(label);
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
		tracesClipboardControl.get().setInput(scan);
		tracesClipboardControl.get().setEnabled(scan instanceof IScanMSD || scan instanceof IScanWSD);
		buttonSaveScan.get().setEnabled(isSaveEnabled());
		buttonDeleteOptimized.get().setEnabled(isOptimizedScan());
		updateLabel(labelOptimized.get(), isOptimizedScan() ? "Optimized" : "");
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

	private void createTracesClipboardUI(Composite parent) {

		TracesClipboardUI tracesClipboardUI = new TracesClipboardUI(parent, SWT.NONE);
		tracesClipboardControl.set(tracesClipboardUI);
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the scan chart.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				reset();
			}
		});
	}

	private void createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(object instanceof IScanMSD scanMSD) {
						IScanMSD optimizedScanMSD = scanMSD.getOptimizedMassSpectrum();

						if(optimizedScanMSD != null) {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), optimizedScanMSD, "Scan[optimized]");
						} else {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "Scan");
						}
					} else if(object instanceof IPeakMSD peakMSD) {
						IPeakModelMSD peakModelMSD = peakMSD.getPeakModel();
						IScanMSD scanMSD = peakModelMSD.getPeakMassSpectrum();
						IScanMSD optimizedScanMSD = scanMSD.getOptimizedMassSpectrum();

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

		buttonSaveScan.set(button);
	}

	private void createDeleteOptimizedButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Delete the optimized scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteOptimizedScan(e.widget.getDisplay());
			}
		});

		buttonDeleteOptimized.set(button);
	}

	private void createSettingsButton(Composite parent) {

		createSettingsButton(parent, Arrays.asList(PreferencePageScans.class), _ -> applySettings());
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

		return informationUI;
	}

	private void createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener((_, _) -> applySearch());

		toolbarSearch.set(searchSupportUI);
	}

	private void createScanWebIdentifierUI(Composite parent) {

		scanWebIdentifierUI.set(new ScanWebIdentifierUI(parent, SWT.NONE));
	}

	private void createTable(Composite parent) {

		ScanTableUI scanTableUI = new ScanTableUI(parent, SWT.VIRTUAL | SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		scanTableUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		scanTableUI.setEditEnabled(false);

		scanTableControl.set(scanTableUI);
	}

	private void applySettings() {

		updateObject();
	}

	private void reset() {

		updateObject();
	}
}