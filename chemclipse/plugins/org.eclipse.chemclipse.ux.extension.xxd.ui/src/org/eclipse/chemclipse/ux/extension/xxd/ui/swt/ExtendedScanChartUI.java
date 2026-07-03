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
 * Christoph Läubrich - add support for configuration, improve user feedback for unsaved changes
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.getMassSpectrum;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.isUseNominalMZ;
import static org.eclipse.chemclipse.msd.model.preferences.PreferenceSupplier.isUseNormalizedScan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.calculator.SubtractCalculator;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.model.types.SignalType;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.updates.IUpdateListener;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.ChartGridSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.charts.ChartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageSubtract;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisIntensity;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisIon;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisRelativeIntensity;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.ChromatogramUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.wizards.SubtractScanWizard;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;

public class ExtendedScanChartUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedScanChartUI.class);

	private AtomicReference<Composite> toolbarMain = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfo = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarEdit = new AtomicReference<>();
	private AtomicReference<Composite> toolbarEdit = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarTypes = new AtomicReference<>();
	private AtomicReference<Composite> toolbarTypes = new AtomicReference<>();
	private AtomicReference<CLabel> labelEdit = new AtomicReference<>();
	private AtomicReference<CLabel> labelSubtract = new AtomicReference<>();
	private AtomicReference<CLabel> labelOptimized = new AtomicReference<>();
	private AtomicReference<TracesClipboardUI> tracesClipboardControl = new AtomicReference<>();
	private AtomicReference<Button> buttonSave = new AtomicReference<>();
	private AtomicReference<Button> buttonDeleteOptimized = new AtomicReference<>();
	private AtomicReference<Button> buttonChartGrid = new AtomicReference<>();
	private AtomicReference<ScanChartUI> chartControl = new AtomicReference<>();
	private AtomicReference<Combo> comboDataType = new AtomicReference<>();
	private AtomicReference<Combo> comboSignalType = new AtomicReference<>();
	private AtomicReference<Button> buttonSubtractOption = new AtomicReference<>();
	private AtomicReference<ScanFilterUI> scanFilterUI = new AtomicReference<>();
	private AtomicReference<ScanIdentifierUI> scanIdentifierControl = new AtomicReference<>();
	private AtomicReference<ScanWebIdentifierUI> scanWebIdentifierUI = new AtomicReference<>();

	private boolean editModus = false;
	private boolean subtractModus = false;
	private IUpdateListener updateListener = null;
	private IScan scan = null;

	public ExtendedScanChartUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setUpdateListener(IUpdateListener updateListener) {

		this.updateListener = updateListener;
	}

	public void clear() {

		update(null);
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
		if(editModus || subtractModus) {
			return focus;
		}

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		List<Object> objects = dataUpdateSupport.getUpdates(getLastTopic(dataUpdateSupport.getTopics()));
		if(!objects.isEmpty()) {
			Object last = objects.get(0);
			if(last instanceof IScan scan) {
				update(scan);
			} else if(last instanceof IPeak peak) {
				update(peak.getPeakModel().getPeakMaximum());
			}
		}

		return focus;
	}

	public void update(IScan scan) {

		this.update(scan, getDisplay());
	}

	public void mirror(IScan scanReference) {

		if(scanReference != null) {
			if(scan instanceof IScanMSD) {
				if(scanReference instanceof IScanMSD) {
					chartControl.get().setInput(scan, scanReference, true);
				}
			}
		} else {
			this.update(scan, getDisplay());
		}
	}

	@Override
	public void dispose() {

		chartControl.get().dispose();
	}

	/**
	 * Getting the updates from the system.
	 *
	 * @param scan
	 */
	private void update(IScan scan, Display display) {

		if(editModus) {
			if(subtractModus) {
				if(this.scan instanceof IScanMSD scanSource) {
					if(scan instanceof IScanMSD scanSubtract) {
						/*
						 * Just subtract a mass spectrum once.
						 * Otherwise, following updates would lead
						 * to subsequent subtractions.
						 */
						int scanNumberSource = scanSource.getScanNumber();
						int scanNumberSubtract = scanSubtract.getScanNumber();
						if(scanNumberSubtract > 0 && scanNumberSource != scanNumberSubtract) {
							/*
							 * Prevent the scan is subtracted from itself.
							 */
							logger.info("Subtract Scan: " + scanNumberSource + " - " + scanNumberSubtract);
							subtractScanMSD(scanSource, scanSubtract);
							IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
							if(!preferenceStore.getBoolean(PreferenceSupplier.P_ENABLE_MULTI_SUBTRACT)) {
								setSubtractModus(display, false, false);
								updateInfoLabels();
							}

							ChromatogramUpdateSupport.fireUpdateChromatogramSelection(display, scanSource);
							updateScan(scanSource);
						}
					}
				}
			} else {
				/*
				 * Updates are disabled in "Edit Modus".
				 * Scan updates are fired regularly in the platform. We don't want to show a recurring
				 * dialog here, that the edit modus is activated. Maybe, it could be display once.
				 */
				// MessageDialog.openInformation(display, "Edit Modus", "To retrieve updates, please disable the edit modus.");
			}
		} else {
			updateScan(scan);
		}

		tracesClipboardControl.get().setInput(scan);
	}

	private void updateScan(IScan scan) {

		this.scan = scan;

		scanFilterUI.get().setInput(scan);
		scanIdentifierControl.get().setInput(scan);
		scanWebIdentifierUI.get().setInput(scan);
		ScanDataSupport scanDataSupport = new ScanDataSupport();
		toolbarInfo.get().setText(scanDataSupport.getScanLabel(scan));
		setDetectorSignalType(scan);
		updateScanChart(scan);
		updateInfoLabels();
		updateButtons();
		adjustAxisSettings();
		/*
		 * Set a fixed range on demand.
		 */
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

		boolean isFixedRangeX = preferenceStore.getBoolean(PreferenceSupplier.P_SCAN_CHART_ENABLE_FIXED_RANGE_X);
		boolean isFixedRangeY = preferenceStore.getBoolean(PreferenceSupplier.P_SCAN_CHART_ENABLE_FIXED_RANGE_Y);

		if(isFixedRangeX || isFixedRangeY) {
			BaseChart baseChart = chartControl.get().getBaseChart();

			if(isFixedRangeX) {
				double startX = preferenceStore.getDouble(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_START_X);
				double stopX = preferenceStore.getDouble(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_STOP_X);
				IAxis axisX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
				axisX.setRange(new Range(startX, stopX));
			}

			if(isFixedRangeY) {
				double startY = preferenceStore.getDouble(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_START_Y);
				double stopY = preferenceStore.getDouble(PreferenceSupplier.P_SCAN_CHART_FIXED_RANGE_STOP_Y);
				IAxis axisY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
				axisY.setRange(new Range(startY, stopY));
			}
		}
	}

	private void updateScanChart(IScan scan) {

		IScanMSD optimizedMassSpectrum = getOptimizedScanMSD();
		ScanChartUI scanChartUI = chartControl.get();

		if(optimizedMassSpectrum != null) {
			scanChartUI.setInput(optimizedMassSpectrum);
		} else {
			scanChartUI.setInput(scan);
		}
	}

	private void createControl() {

		setLayout(new FillLayout());

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		createToolbarMain(composite);
		createToolbarInfo(composite);
		createToolbarTypes(composite);
		createToolbarEdit(composite);
		createScanChart(composite);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfo, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarTypes, buttonToolbarTypes.get(), IMAGE_TYPES, TOOLTIP_TYPES, false);
		enableToolbar(toolbarEdit, buttonToolbarEdit.get(), IMAGE_EDIT, TOOLTIP_EDIT, false);
		enableChartGrid(chartControl, buttonChartGrid.get(), IMAGE_CHART_GRID, new ChartGridSupport());

		updateButtons();
		adjustAxisSettings();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(14, false));

		createInfoLabelEdit(composite);
		createInfoLabelSubtract(composite);
		createInfoLabelOptimized(composite);
		createButtonToggleInfo(composite);
		createButtonToggleTypes(composite);
		createButtonToggleEdit(composite);
		createTracesClipboardUI(composite);
		createScanIdentifierUI(composite);
		createScanWebIdentifierUI(composite);
		createResetButton(composite);
		createSaveButton(composite);
		createDeleteOptimizedButton(composite);
		createButtonToggleGrid(composite);
		createSettingsButton(composite);

		toolbarMain.set(composite);
	}

	private void createButtonToggleInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, toolbarInfo, IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createButtonToggleTypes(Composite parent) {

		buttonToolbarTypes.set(createButtonToggleToolbar(parent, toolbarTypes, IMAGE_TYPES, TOOLTIP_TYPES));
	}

	private void createButtonToggleEdit(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarEdit, IMAGE_EDIT, TOOLTIP_EDIT);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = toolbarEdit.get().isVisible();
				setEditToolbarStatus(visible);
				updateInfoLabels();
			}
		});

		buttonToolbarEdit.set(button);
	}

	private void createButtonToggleGrid(Composite parent) {

		buttonChartGrid.set(createButtonToggleChartGrid(parent, chartControl, IMAGE_CHART_GRID, new ChartGridSupport()));
	}

	private void createInfoLabelEdit(Composite parent) {

		CLabel label = createInfoLabel(parent);
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				if(!label.getText().isEmpty()) {
					setEditToolbarStatus(false);
					updateInfoLabels();
				}
			}
		});

		labelEdit.set(label);
	}

	private void createInfoLabelSubtract(Composite parent) {

		CLabel label = createInfoLabel(parent);
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				if(!label.getText().isEmpty()) {
					setSubtractModus(e.display, false, false);
					updateInfoLabels();
				}
			}
		});

		labelSubtract.set(label);
	}

	private void createInfoLabelOptimized(Composite parent) {

		CLabel label = createInfoLabel(parent);
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				if(!label.getText().isEmpty()) {
					deleteOptimizedScan(e.widget.getDisplay());
					updateInfoLabels();
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

	private void createToolbarTypes(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, true));

		createDataType(composite);
		createSignalType(composite);

		toolbarTypes.set(composite);
	}

	private void createToolbarEdit(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));

		createButtonSubtractOption(composite);
		createScanFilterUI(composite);

		toolbarEdit.set(composite);
	}

	private void createButtonSubtractOption(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Enable/Disable the interactive subtract option.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_SCAN_DEFAULT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setSubtractModus(e.display, !subtractModus, true);
				updateInfoLabels();
				fireUpdate();
			}
		});

		buttonSubtractOption.set(button);
	}

	private void setSubtractModus(Display display, boolean subtractModus, boolean showDialog) {

		this.subtractModus = subtractModus;
		String fileName = this.subtractModus ? IApplicationImage.IMAGE_SUBTRACT_SCAN_ACTIVE : IApplicationImage.IMAGE_SUBTRACT_SCAN_DEFAULT;
		buttonSubtractOption.get().setImage(ApplicationImageFactory.getInstance().getImage(fileName, IApplicationImageProvider.SIZE_16x16));

		if(this.subtractModus && showDialog) {
			IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			if(preferenceStore.getBoolean(PreferenceSupplier.P_SHOW_SUBTRACT_DIALOG)) {
				if(display != null) {
					SubtractScanWizard.openWizard(display.getActiveShell());
				}
			}
		}
	}

	private void createScanFilterUI(Composite parent) {

		ScanFilterUI scanFilter = new ScanFilterUI(parent, SWT.NONE);
		scanFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		scanFilter.setUpdateListener(display -> {

			updateScan(scan);
			fireUpdate();
		});

		scanFilterUI.set(scanFilter);
	}

	private void createScanIdentifierUI(Composite parent) {

		ScanIdentifierUI scanIdentifierUI = new ScanIdentifierUI(parent, SWT.NONE);
		scanIdentifierUI.setUpdateListener(display -> {

			updateScan(scan);
			UpdateNotifierUI.update(display, scan);
			UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "Scan Chart identification has been performed.");
			ChromatogramUpdateSupport.fireUpdateChromatogramSelection(display, scan);
			fireUpdate();
		});

		scanIdentifierControl.set(scanIdentifierUI);
	}

	private void subtractScanMSD(IScanMSD scanSource, IScanMSD scanSubtract) {

		/*
		 * Settings
		 */
		MassSpectrumFilterSettings settings = new MassSpectrumFilterSettings();
		settings.setUseNominalMasses(isUseNominalMZ());
		settings.setUseNormalize(isUseNormalizedScan());
		settings.setSubtractMassSpectrum(getMassSpectrum(scanSubtract));
		/*
		 * Subtract
		 */
		IScanMSD optimizedMassSpectrum = getOptimizedMassSpectrum(scanSource);
		SubtractCalculator subtractCalculator = new SubtractCalculator();
		subtractCalculator.subtractMassSpectrum(optimizedMassSpectrum, settings);
	}

	private IScanMSD getOptimizedMassSpectrum(IScanMSD scanMSD) {

		IScanMSD optimizedMassSpectrum = scanMSD.getOptimizedMassSpectrum();
		if(optimizedMassSpectrum == null) {
			try {
				optimizedMassSpectrum = scanMSD.makeDeepCopy();
				scanMSD.setOptimizedMassSpectrum(optimizedMassSpectrum);
			} catch(CloneNotSupportedException e) {
				logger.warn(e);
			}
		}

		return optimizedMassSpectrum;
	}

	private void createDataType(Composite parent) {

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setToolTipText("Data Type (MS, MS/MS, FID, DAD, ...)");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setItems(ScanDataSupport.DATA_TYPES_DEFAULT);
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String selection = combo.getText();
				chartControl.get().setDataType(DataType.valueOf(selection));
				updateScanChart(scan);
			}
		});

		comboDataType.set(combo);
	}

	private void createSignalType(Composite parent) {

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setToolTipText("Signal Type (Centroid: Bar Series, Profile: Line Series)");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setItems(ScanDataSupport.SIGNAL_TYPES_DEFAULT);
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String selection = combo.getText();
				chartControl.get().setSignalType(SignalType.valueOf(selection));
				updateScanChart(scan);
			}
		});

		comboSignalType.set(combo);
	}

	private void setEditToolbarStatus(boolean visible) {

		if(!visible) {
			boolean toolbarIsVisible = toolbarEdit.get().isVisible();
			if(toolbarIsVisible) {
				enableToolbar(toolbarEdit, buttonToolbarEdit.get(), IMAGE_EDIT, TOOLTIP_EDIT, false);
			}
		}
		/*
		 * Set the edit modus and button icon.
		 */
		editModus = visible;
	}

	private void updateInfoLabels() {

		updateLabel(labelEdit.get(), (editModus) ? "Edit On" : "");
		updateLabel(labelSubtract.get(), (subtractModus) ? "Subtract On" : "");
		updateLabel(labelOptimized.get(), (isOptimizedScan()) ? "Optimized" : "");
	}

	private boolean isMassSpectrum() {

		return scan instanceof IScanMSD;
	}

	private boolean isWaveSpectrum() {

		return scan instanceof IScanWSD;
	}

	private boolean isOptimizedScan() {

		IScanMSD optimizedMassSpectrum = getOptimizedScanMSD();
		return optimizedMassSpectrum != null;
	}

	private IScanMSD getScanMSD() {

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

	private void updateLabel(CLabel label, String message) {

		label.setText(message);
		label.setBackground(message.isEmpty() ? null : Colors.LIGHT_YELLOW);
	}

	private void updateButtons() {

		buttonChartGrid.get().setEnabled(true);
		boolean enabled = isMassSpectrum();

		scanIdentifierControl.get().setEnabled(enabled || isWaveSpectrum());
		if(!enabled) {
			scanWebIdentifierUI.get().setEnabled(false);
		}

		tracesClipboardControl.get().setEnabled(scan instanceof IScanMSD || scan instanceof IScanWSD);
		buttonSave.get().setEnabled(enabled);
		buttonDeleteOptimized.get().setEnabled(enabled && isOptimizedScan());
		buttonSubtractOption.get().setEnabled(enabled);
		scanFilterUI.get().setEnabled(enabled);
	}

	private void createTracesClipboardUI(Composite parent) {

		TracesClipboardUI tracesClipboardUI = new TracesClipboardUI(parent, SWT.NONE);
		tracesClipboardControl.set(tracesClipboardUI);
	}

	private void createScanWebIdentifierUI(Composite parent) {

		scanWebIdentifierUI.set(new ScanWebIdentifierUI(parent, SWT.NONE));
	}

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the scan");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_RESET, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setEditToolbarStatus(false);
				setSubtractModus(e.display, false, false);
				updateInfoLabels();
				updateScan(scan);
				fireUpdate();
			}
		});
	}

	private void createSettingsButton(Composite parent) {

		ISettingsHandler settingsHandler = display -> {

			updateScan(scan);
			scanIdentifierControl.get().updateIdentifier();
			tracesClipboardControl.get().updateOption();
			fireUpdate();
			adjustAxisSettings();
		};

		Button button = createSettingsButtonBasic(parent);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {

				/*
				 * Dynamically show different settings, based on the selected scan type.
				 */
				List<Class<? extends IPreferencePage>> preferencePages = getPreferencePages();
				showPreferencesDialog(event, preferencePages, settingsHandler, true);
			}
		});
	}

	private List<Class<? extends IPreferencePage>> getPreferencePages() {

		List<Class<? extends IPreferencePage>> preferencePages = new ArrayList<>();
		preferencePages.add(PreferencePageScans.class);
		preferencePages.add(ScanChartAxisIon.class);
		preferencePages.add(ScanChartAxisIntensity.class);
		preferencePages.add(ScanChartAxisRelativeIntensity.class);
		preferencePages.add(PreferencePageSubtract.class);

		return preferencePages;
	}

	void adjustAxisSettings() {

		adjustAxisIons();
		adjustAxisIntensity();
		adjustAxisRelativeIntensity();

		IChartSettings chartSettings = chartControl.get().getChartSettings();
		chartControl.get().applySettings(chartSettings);
	}

	private void adjustAxisIons() {

		IChartSettings chartSettings = chartControl.get().getChartSettings();
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(ExtensionMessages.ion);

		String positionNode = PreferenceSupplier.P_SCAN_CHART_POSITION_X_AXIS_IONS;
		String gridLineStyleNode = PreferenceSupplier.P_SCAN_CHART_GRIDLINE_STYLE_X_AXIS_IONS;

		ChartSupport.setAxisSettingsExtended(primaryAxisSettingsX, positionNode, "0", gridLineStyleNode);
		ChartSupport.themeAxis(primaryAxisSettingsX, ExtendedScanChartUI.class.getName() + ".AxisIons");
		primaryAxisSettingsX.setVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_X_AXIS_IONS));
		primaryAxisSettingsX.setTitleVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_X_AXIS_TITLE_IONS));
	}

	private void adjustAxisIntensity() {

		IChartSettings chartSettings = chartControl.get().getChartSettings();
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(ExtensionMessages.intensity);

		String positionNode = PreferenceSupplier.P_SCAN_CHART_POSITION_Y_AXIS_INTENSITY;
		String patternNode = PreferenceSupplier.P_SCAN_CHART_FORMAT_Y_AXIS_INTENSITY;
		String gridLineStyleNode = PreferenceSupplier.P_SCAN_CHART_GRIDLINE_STYLE_Y_AXIS_INTENSITY;

		ChartSupport.setAxisSettingsExtended(primaryAxisSettingsY, positionNode, patternNode, gridLineStyleNode);
		ChartSupport.themeAxis(primaryAxisSettingsY, ExtendedScanChartUI.class.getName() + ".AxisIntensity");
		primaryAxisSettingsY.setVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_INTENSITY));
		primaryAxisSettingsY.setTitleVisible(ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_TITLE_INTENSITY));
	}

	private void adjustAxisRelativeIntensity() {

		IChartSettings chartSettings = chartControl.get().getChartSettings();
		ISecondaryAxisSettings axisSettings = ChartSupport.getSecondaryAxisSettingsY(ExtensionMessages.relativeIntensity, chartSettings);

		String positionNode = PreferenceSupplier.P_SCAN_CHART_POSITION_Y_AXIS_RELATIVE_INTENSITY;
		String patternNode = PreferenceSupplier.P_SCAN_CHART_FORMAT_Y_AXIS_RELATIVE_INTENSITY;
		String gridLineStyleNode = PreferenceSupplier.P_SCAN_CHART_GRIDLINE_STYLE_Y_AXIS_RELATIVE_INTENSITY;

		boolean isShowAxis = ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_RELATIVE_INTENSITY);
		boolean isShowAxisTitle = ChartSupport.getBoolean(PreferenceSupplier.P_SCAN_CHART_SHOW_Y_AXIS_TITLE_RELATIVE_INTENSITY);

		if(isShowAxis) {
			if(axisSettings == null) {
				ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings(ExtensionMessages.relativeIntensity, new PercentageConverter(SWT.VERTICAL, true));
				ChartSupport.setAxisSettingsExtended(secondaryAxisSettingsY, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(secondaryAxisSettingsY, ExtendedScanChartUI.class.getName() + ".AxisRelativeIntensity");
				secondaryAxisSettingsY.setTitleVisible(isShowAxisTitle);
				chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
			} else {
				ChartSupport.setAxisSettingsExtended(axisSettings, positionNode, patternNode, gridLineStyleNode);
				ChartSupport.themeAxis(axisSettings, ExtendedScanChartUI.class.getName() + ".AxisRelativeIntensity");
				axisSettings.setTitle(ExtensionMessages.relativeIntensity);
				axisSettings.setVisible(true);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		} else {
			if(axisSettings != null) {
				axisSettings.setTitle(ExtensionMessages.relativeIntensity);
				axisSettings.setVisible(false);
				axisSettings.setTitleVisible(isShowAxisTitle);
			}
		}
	}

	private void createSaveButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Save the scan.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(scan instanceof IScanMSD scanMSD) {
						DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), scanMSD, "OriginalScan");
						IScanMSD optimizedScan = scanMSD.getOptimizedMassSpectrum();
						if(optimizedScan != null) {
							DatabaseFileSupport.saveMassSpectrum(e.display.getActiveShell(), optimizedScan, "OptimizedScan");
						}
					}
				} catch(NoConverterAvailableException ex) {
					logger.warn(ex);
				}
			}
		});

		buttonSave.set(button);
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

	private void deleteOptimizedScan(Display display) {

		if(scan instanceof IScanMSD scanMSD) {
			if(MessageDialog.openQuestion(display.getActiveShell(), "Optimized Scan", "Would you like to delete the optimized scan?")) {
				scanMSD.setOptimizedMassSpectrum(null);
				updateScan(scan);
				fireUpdate();
			}
		}
	}

	private void createToolbarInfo(Composite parent) {

		InformationUI informationUI = new InformationUI(parent, SWT.NONE);
		informationUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolbarInfo.set(informationUI);
	}

	private void createScanChart(Composite parent) {

		ScanChartUI scanChartUI = new ScanChartUI(parent, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));

		chartControl.set(scanChartUI);
	}

	private void setDetectorSignalType(IScan scan) {

		Combo comboData = comboDataType.get();
		Combo comboSignal = comboSignalType.get();

		if(scan instanceof IScanMSD) {
			setSelectionIndex(comboData, ScanDataSupport.DATA_TYPES_MSD);
			setSelectionIndex(comboSignal, ScanDataSupport.SIGNAL_TYPES_MSD);
		} else if(scan instanceof IScanCSD) {
			setSelectionIndex(comboData, ScanDataSupport.DATA_TYPES_CSD);
			setSelectionIndex(comboSignal, ScanDataSupport.SIGNAL_TYPES_CSD);
		} else if(scan instanceof IScanWSD) {
			setSelectionIndex(comboData, ScanDataSupport.DATA_TYPES_WSD);
			setSelectionIndex(comboSignal, ScanDataSupport.SIGNAL_TYPES_WSD);
		} else if(scan instanceof IScanVSD) {
			setSelectionIndex(comboData, ScanDataSupport.DATA_TYPES_VSD);
			setSelectionIndex(comboSignal, ScanDataSupport.SIGNAL_TYPES_VSD);
		} else {
			comboData.setItems(ScanDataSupport.DATA_TYPES_DEFAULT);
			comboData.select(0);
			comboSignal.setItems(ScanDataSupport.SIGNAL_TYPES_DEFAULT);
			comboSignal.select(0);
		}
		/*
		 * Data / Signal Type
		 */
		ScanChartUI scanChartUI = chartControl.get();
		scanChartUI.setDataType(DataType.valueOf(comboData.getText()));
		scanChartUI.setSignalType(SignalType.valueOf(comboSignal.getText()));
	}

	private void setSelectionIndex(Combo combo, String[] items) {

		int index;
		if(combo.getSelectionIndex() == -1) {
			index = 0;
		} else {
			index = (combo.getSelectionIndex() < items.length) ? combo.getSelectionIndex() : 0;
		}

		combo.setItems(items);
		combo.select(index);
	}

	private void fireUpdate() {

		if(updateListener != null) {
			updateListener.update();
		}
	}
}