/*******************************************************************************
 * Copyright (c) 2017, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - make this configurable, null check for scan
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.calculator.SubtractCalculator;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.subtract.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.model.types.SignalType;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.swt.ui.support.DatabaseFileSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.provider.AbstractLabelProvider;
import org.eclipse.chemclipse.support.ui.swt.EnhancedComboViewer;
import org.eclipse.chemclipse.support.ui.updates.IUpdateListenerUI;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.swt.ui.components.InformationUI;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.swt.ui.services.IScanIdentifierService;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.ISettingsHandler;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.charts.LabelOption;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.preferences.PreferenceSupplierModelMSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.model.ComparisonScanOption;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageSubtract;
import org.eclipse.chemclipse.ux.extension.xxd.ui.runnables.LibraryServiceRunnable;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.ChromatogramUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanChartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ChartType;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

import jakarta.inject.Inject;

public class ExtendedComparisonScanUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedComparisonScanUI.class);

	private static final float NORMALIZATION_FACTOR = 1000.0f;

	private static final String PREFIX_U = "[U]";
	private static final String PREFIX_R = "[R]";
	private static final String PREFIX_UR = "[U-R]";
	private static final String TITLE_UNKNOWN = "UNKNOWN MS";
	private static final String TITLE_REFERENCE = "REFERENCE MS";
	private static final String POSTFIX_NONE = "";
	private static final String POSTFIX_SHIFTED = "SHIFTED";
	private static final String IMAGE_HYBRID = IApplicationImage.IMAGE_BACKWARD;
	private static final String TOOLTIP_HYBRID = "the hybrid search.";

	private AtomicReference<TabFolder> tabFolderControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> comboViewerOptionControl = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarInfo = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoTop = new AtomicReference<>();
	private AtomicReference<InformationUI> toolbarInfoBottom = new AtomicReference<>();
	private AtomicReference<ScanChartUI> scanChartControl = new AtomicReference<>();
	private AtomicReference<Button> buttonToolbarHybrid = new AtomicReference<>();
	private AtomicReference<Button> buttonSubtractReference = new AtomicReference<>();
	private AtomicReference<Button> buttonUseOptimizedScan = new AtomicReference<>();
	private AtomicReference<ScanIdentifierUI> scanIdentifierControl = new AtomicReference<>();
	private AtomicReference<Button> buttonMirroredReference = new AtomicReference<>();
	private AtomicReference<Button> buttonDifferenceSpectrum = new AtomicReference<>();
	private AtomicReference<Button> buttonShiftReferenceSpectrum = new AtomicReference<>();
	private AtomicReference<Spinner> spinnerShiftReferenceSpectrum = new AtomicReference<>();
	private AtomicReference<Button> buttonLegendControl = new AtomicReference<>();
	private AtomicReference<Composite> toolbarHybridSearch = new AtomicReference<>();
	private AtomicReference<Text> textWeightUnknownControl = new AtomicReference<>();
	private AtomicReference<Text> textWeightReferenceControl = new AtomicReference<>();
	private AtomicReference<ScanChartUI> scanChartStackUnknownControl = new AtomicReference<>();
	private AtomicReference<ScanChartUI> scanChartStackReferenceControl = new AtomicReference<>();

	private boolean showDifferenceSpectrum = false;
	private boolean useMirroredSpectrum = true;
	private boolean useOptimizedSpectrum = false;
	private boolean shiftReferenceSpectrum = false;
	private int shiftMass = 1;

	private IScanMSD scanUnknownMaster = null;
	private IScanMSD scanUnknown = null;
	private IScanMSD scanReference = null;

	private ScanChartSupport scanChartSupport = new ScanChartSupport();
	private ScanDataSupport scanDataSupport = new ScanDataSupport();

	@Inject
	public ExtendedComparisonScanUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	@Focus
	public boolean setFocus() {

		updateOnFocus();
		return true;
	}

	public void clear() {

		IScanMSD unknown = null;
		IScanMSD reference = null;
		update(unknown, reference);
	}

	public void update(IScanMSD scanMSD) {

		boolean update = false;
		switch(getComparisonScanOption()) {
			case UNKNOWN:
				scanUnknownMaster = scanMSD;
				assignScan(scanMSD, true);
				update = true;
				break;
			case REFERENCE:
				assignScan(scanMSD, false);
				update = true;
				break;
			default:
				break;
		}

		if(update) {
			updateInput();
		}
	}

	public void update(IIdentificationTarget identificationTarget) {

		if(isLibrarySearch()) {
			updateIdentificationTarget(identificationTarget);
			updateIdentifierControl();
		}
	}

	public void update(IScanMSD massSpectrum, IIdentificationTarget identificationTarget) {

		if(isLibrarySearch()) {
			scanUnknownMaster = massSpectrum;
			scanUnknown = copyScan(massSpectrum);
			updateMolecularWeightUnknown();
			updateIdentificationTarget(identificationTarget);
			updateIdentifierControl();
		}
	}

	/**
	 * Update unknown and reference
	 * 
	 * @param unknownMassSpectrum
	 * @param referenceMassSpectrum
	 */
	public void update(IScanMSD unknownMassSpectrum, IScanMSD referenceMassSpectrum) {

		scanUnknownMaster = unknownMassSpectrum;
		updateInput(unknownMassSpectrum, referenceMassSpectrum);
	}

	@Override
	public void dispose() {

		scanChartControl.get().dispose();
	}

	private boolean isLibrarySearch() {

		return ComparisonScanOption.LIBRARY_SEARCH.equals(getComparisonScanOption());
	}

	private ComparisonScanOption getComparisonScanOption() {

		Object selection = comboViewerOptionControl.get().getStructuredSelection().getFirstElement();
		if(selection instanceof ComparisonScanOption comparisonScanOption) {
			return comparisonScanOption;
		} else {
			return ComparisonScanOption.LIBRARY_SEARCH;
		}
	}

	private void updateIdentificationTarget(IIdentificationTarget identificationTarget) {

		updateMolecularWeightReference(identificationTarget);

		LibraryServiceRunnable runnable = new LibraryServiceRunnable(identificationTarget, referenceMassSpectrum -> {

			scanReference = copyScan(referenceMassSpectrum);
			updateMolecularIon(identificationTarget, scanReference);
			Display.getDefault().asyncExec(() -> {

				updateChart();
			});
		});
		/*
		 * Create a runnable to update the reference.
		 */
		try {
			if(runnable.requireProgressMonitor()) {
				DisplayUtils.executeInUserInterfaceThread(() -> {
					ProgressMonitorDialog monitor = new ProgressMonitorDialog(scanChartControl.get().getShell());
					monitor.run(true, true, runnable);
					return null;
				});
			} else {
				DisplayUtils.executeBusy(() -> {
					runnable.run(new NullProgressMonitor());
					return null;
				});
			}
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch(ExecutionException e) {
			ILog.get().error("Updating the reference scan failed.", e);
		}
	}

	private void updateMolecularIon(IIdentificationTarget identificationTarget, IScanMSD scanMSD) {

		if(identificationTarget != null && scanMSD != null) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			IIon molecularIon = scanMSD.getIon(libraryInformation.getMolWeight());
			if(molecularIon.getAbundance() > 0) {
				scanChartControl.get().setMolecularIon(molecularIon);
			}
		}
	}

	private void updateMolecularWeightUnknown() {

		ILibraryInformation libraryInformation = IIdentificationTarget.getLibraryInformation(scanUnknown);
		if(libraryInformation != null) {
			textWeightUnknownControl.get().setText(Integer.toString((int)Math.round(libraryInformation.getMolWeight())));
		} else {
			textWeightUnknownControl.get().setText("");
		}
	}

	private void updateMolecularWeightReference(IIdentificationTarget identificationTarget) {

		if(identificationTarget != null) {
			ILibraryInformation libraryInformation = identificationTarget.getLibraryInformation();
			textWeightReferenceControl.get().setText(Integer.toString((int)Math.round(libraryInformation.getMolWeight())));
		} else {
			textWeightReferenceControl.get().setText("");
		}
	}

	private void updateStackCharts() {

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		ITheme currentTheme = themeManager.getCurrentTheme();
		ColorRegistry colorRegistry = currentTheme.getColorRegistry();
		Color colorScan1 = colorRegistry.get("org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ScanChart.ColorScanOne");
		Color colorScan2 = colorRegistry.get("org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ScanChart.ColorScanTwo");
		updateStackChart(scanChartStackUnknownControl.get(), scanUnknown, colorScan1, "Unknown");
		updateStackChart(scanChartStackReferenceControl.get(), scanReference, colorScan2, "Reference");
	}

	private void updateStackChart(ScanChartUI scanChartUI, IScanMSD scanMSD, Color color, String label) {

		scanChartUI.deleteSeries();
		List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
		IBarSeriesData barSeriesDataScan = scanChartSupport.getBarSeriesData(scanMSD, label, false);
		IBarSeriesSettings barSeriesSettings = barSeriesDataScan.getSettings();
		barSeriesSettings.setBarColor(color);
		barSeriesSettings.setBarOverlay(true);
		barSeriesDataList.add(barSeriesDataScan);
		scanChartUI.addBarSeriesData(barSeriesDataList);
	}

	private void clearStackCharts() {

		scanChartStackUnknownControl.get().deleteSeries();
		scanChartStackReferenceControl.get().deleteSeries();
	}

	private void updateScanComparisonNormal() {

		updateToolbarInfoSpecial(PREFIX_U, PREFIX_R);
		ScanChartUI scanChartUI = scanChartControl.get();
		if(shiftReferenceSpectrum) {
			IScanMSD scanReferenceShifted = new ScanMSD();
			IExtractedIonSignal extractedIonSignalScanReference = scanReference.getExtractedIonSignal();
			int startIon = extractedIonSignalScanReference.getStartIon();
			int stopIon = extractedIonSignalScanReference.getStopIon();
			for(int ion = startIon; ion <= stopIon; ion++) {
				float abundance = extractedIonSignalScanReference.getAbundance(ion);
				if(abundance > 0) {
					scanReferenceShifted.addIon(getIon(ion + shiftMass, abundance));
				}
			}
			scanChartUI.setInput(scanUnknown, scanReferenceShifted, useMirroredSpectrum);
		} else {
			scanChartUI.setInput(scanUnknown, scanReference, useMirroredSpectrum);
		}
		/*
		 * Post modify if hybrid search is active.
		 */
		if(toolbarHybridSearch.get().isVisible()) {
			IChartSettings chartSettings = scanChartUI.getChartSettings();
			chartSettings.getPrimaryAxisSettingsX().setTitle("Delta [m/z]");
			RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
			rangeRestriction.setZeroX(false);
			rangeRestriction.setZeroY(false);
			rangeRestriction.setForceZeroMinY(false);
			rangeRestriction.setRestrictZoomX(false);
			scanChartUI.applySettings(chartSettings);
			int molWeightUnknown = getMolWeight(textWeightUnknownControl);
			int molWeightReference = getMolWeight(textWeightReferenceControl);
			BaseChart baseChart = scanChartUI.getBaseChart();
			baseChart.shiftSeries(ScanChartUI.LABEL_SCAN1, IExtendedChart.X_AXIS, -molWeightUnknown);
			baseChart.shiftSeries(ScanChartUI.LABEL_SCAN2, IExtendedChart.X_AXIS, -molWeightReference);
			scanChartUI.adjustRange(true);
		}
	}

	private int getMolWeight(AtomicReference<Text> textControl) {

		try {
			return Integer.parseInt(textControl.get().getText().trim());
		} catch(NumberFormatException e) {
			return 0;
		}
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));

		createToolbarMain(this);
		createToolbarInfoTop(this);
		createToolbarHybridSearch(this);
		createTabFolderCharts(this);
		createToolbarInfoBottom(this);

		initialize();
	}

	private void initialize() {

		enableToolbar(toolbarInfoTop, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarInfoBottom, buttonToolbarInfo.get(), IApplicationImage.IMAGE_INFO, TOOLTIP_INFO, true);
		enableToolbar(toolbarHybridSearch, buttonToolbarHybrid.get(), IMAGE_HYBRID, TOOLTIP_HYBRID, false);
		comboViewerOptionControl.get().setInput(ComparisonScanOption.values());
		comboViewerOptionControl.get().setSelection(new StructuredSelection(ComparisonScanOption.LIBRARY_SEARCH));
		buttonLegendControl.get().setEnabled(true);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(14, false));

		createButtonToggleInfo(composite);
		createComboViewerComparisonScanOption(composite);
		createResetButton(composite);
		createSaveButton(composite);
		createButtonToggleHybrid(composite);
		createButtonSubtractReference(composite);
		createButtonOptimizedSpectrum(composite);
		createScanIdentifierUI(composite);
		createButtonMirroredSpectrum(composite);
		createButtonDifferenceSpectrum(composite);
		createButtonShiftReferenceSpectrum(composite);
		createSpinnerShiftReferenceSpectrum(composite);
		createToggleLegendButton(composite);
		createSettingsButton(composite);
	}

	private void createButtonToggleInfo(Composite parent) {

		buttonToolbarInfo.set(createButtonToggleToolbar(parent, Arrays.asList(toolbarInfoTop, toolbarInfoBottom), IMAGE_INFO, TOOLTIP_INFO));
	}

	private void createComboViewerComparisonScanOption(Composite parent) {

		ComboViewer comboViewer = new EnhancedComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new AbstractLabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof ComparisonScanOption comparisonScanOption) {
					return comparisonScanOption.label();
				}
				return null;
			}
		});

		combo.setToolTipText("Comparison Scan Option");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof ComparisonScanOption) {
					updateChart();
				}
			}
		});

		comboViewerOptionControl.set(comboViewer);
	}

	private void createToolbarHybridSearch(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(3, true));

		createTextWeightUnknown(composite);
		createLabel(composite, "MW (Unknown vs. Reference)");
		createTextWeightReference(composite);

		toolbarHybridSearch.set(composite);
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
	}

	private void createTextWeightUnknown(Composite parent) {

		textWeightUnknownControl.set(createText(parent));
	}

	private void createTextWeightReference(Composite parent) {

		textWeightReferenceControl.set(createText(parent));
	}

	private Text createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.keyCode == SWT.LF || e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					updateChart();
				}
			}
		});

		return text;
	}

	private void createTabFolderCharts(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		createScanChartComparison(tabFolder);
		createScanChartStacked(tabFolder);

		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean enabled = tabFolder.getSelectionIndex() == 0;
				buttonLegendControl.get().setEnabled(enabled);
			}
		});

		tabFolderControl.set(tabFolder);
	}

	private void createScanChartComparison(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Comparison");

		ScanChartUI scanChartUI = new ScanChartUI(tabFolder, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));

		tabItem.setControl(scanChartUI);
		scanChartControl.set(scanChartUI);
	}

	private void createScanChartStacked(TabFolder tabFolder) {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Stacked");

		Composite composite = new Composite(tabFolder, SWT.BORDER);
		composite.setLayout(new GridLayout(1, true));

		ScanChartUI scanChartUnknown = createScanChartStacked(composite);
		ScanChartUI scanChartReference = createScanChartStacked(composite);
		scanChartReference.addLinkedScrollableChart(scanChartUnknown);

		scanChartStackUnknownControl.set(scanChartUnknown);
		scanChartStackReferenceControl.set(scanChartReference);
		tabItem.setControl(composite);
	}

	private ScanChartUI createScanChartStacked(Composite parent) {

		ScanChartUI scanChartUI = new ScanChartUI(parent, SWT.NONE);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		scanChartUI.setChartType(ChartType.BAR);
		scanChartUI.setDataType(DataType.MSD_NOMINAL);
		scanChartUI.setSignalType(SignalType.CENTROID);
		scanChartUI.setLabelOption(LabelOption.NOMIMAL);
		scanChartUI.activateLabelMarkerX();
		/*
		 * Settings
		 */
		IChartSettings chartSettings = scanChartUI.getChartSettings();
		chartSettings.setHorizontalSliderVisible(false);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.getPrimaryAxisSettingsX().setVisible(false);
		chartSettings.getPrimaryAxisSettingsY().setVisible(false);
		for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListY()) {
			secondaryAxisSettings.setVisible(false);
		}
		scanChartUI.applySettings(chartSettings);

		return scanChartUI;
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

	private void createResetButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the chart.");
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
		button.setToolTipText("Save both mass spectra.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SAVE_AS, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				saveMassSpectrum(scanUnknown, "UnknownMS");
				saveMassSpectrum(scanReference, "ReferenceMS");
			}
		});
		return button;
	}

	private void saveMassSpectrum(IScanMSD scanMSD, String fileName) {

		if(scanMSD != null) {
			try {
				DatabaseFileSupport.saveMassSpectrum(DisplayUtils.getShell(), scanMSD, fileName);
			} catch(NoConverterAvailableException e1) {
				logger.warn(e1);
			}
		}
	}

	private void createButtonToggleHybrid(Composite parent) {

		Button button = createButtonToggleToolbar(parent, toolbarHybridSearch, IMAGE_HYBRID, TOOLTIP_HYBRID);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent s) {

				updateChart();
			}
		});

		buttonToolbarHybrid.set(button);
	}

	private void createButtonSubtractReference(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Subtract the reference spectrum.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SUBTRACT_SCAN_DEFAULT, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IScanMSD scanMSD = subtractScanMSD(scanUnknown, scanReference);
				scanUnknown = copyScan(scanMSD, false);
				if(scanUnknownMaster != null) {
					scanUnknownMaster.setOptimizedMassSpectrum(scanUnknown);
				}
				useOptimizedSpectrum = true;
				updateButtonOptimized(buttonUseOptimizedScan.get());
				updateInput();
			}
		});

		buttonSubtractReference.set(button);
	}

	private IScanMSD subtractScanMSD(IScanMSD scanSource, IScanMSD scanSubtract) {

		/*
		 * Settings
		 */
		MassSpectrumFilterSettings settings = new MassSpectrumFilterSettings();
		settings.setUseNominalMasses(PreferenceSupplierModelMSD.isUseNominalMZ());
		settings.setUseNormalize(PreferenceSupplierModelMSD.isUseNormalizedScan());
		settings.setSubtractMassSpectrum(PreferenceSupplierModelMSD.getMassSpectrum(scanReference));
		/*
		 * Subtract
		 */
		SubtractCalculator subtractCalculator = new SubtractCalculator();
		subtractCalculator.subtractMassSpectrum(scanSource, settings);
		scanSource = copyScan(scanSource);

		return scanSource;
	}

	private void createButtonOptimizedSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		button.setToolTipText("Use the optimized mass spectrum if available.");
		button.setSelection(useOptimizedSpectrum);
		updateButtonOptimized(button);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent s) {

				useOptimizedSpectrum = button.getSelection();
				updateButtonOptimized(button);
				if(scanUnknownMaster != null) {
					scanUnknown = copyScan(scanUnknownMaster, false);
				}
				updateInput(scanUnknown, scanReference);
			}
		});

		buttonUseOptimizedScan.set(button);
	}

	private void createScanIdentifierUI(Composite parent) {

		ScanIdentifierUI scanIdentifierUI = new ScanIdentifierUI(parent, SWT.NONE);
		scanIdentifierUI.setUpdateListener(new IUpdateListenerUI() {

			@Override
			public void update(Display display) {

				updateInput();
				if(scanUnknownMaster != null) {
					UpdateNotifierUI.update(display, scanUnknownMaster);
					UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "Scan Chart identification has been performed.");
					ChromatogramUpdateSupport.fireUpdateChromatogramSelection(display, scanUnknownMaster);
				}
			}
		});

		scanIdentifierControl.set(scanIdentifierUI);
	}

	private void updateButtonOptimized(Button button) {

		setButtonImage(button, IApplicationImage.IMAGE_PLUS, PREFIX_ENABLE, PREFIX_DISABLE, "using the optimized mass spectrum if available.", useOptimizedSpectrum);
	}

	private void createButtonMirroredSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		button.setToolTipText("Show the reference in mirrored modus.");
		updateButtonMirrored(button);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent s) {

				useMirroredSpectrum = !useMirroredSpectrum;
				updateButtonMirrored(button);
				updateInput();
			}
		});

		buttonMirroredReference.set(button);
	}

	private void updateButtonMirrored(Button button) {

		setButtonImage(button, IApplicationImage.IMAGE_MIRRORED_MASS_SPECTRUM, PREFIX_ENABLE, PREFIX_DISABLE, "showing the reference in mirrored modus.", useMirroredSpectrum);
	}

	private void createButtonDifferenceSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		button.setToolTipText("Show both unknown and reference in difference modus.");
		updateButtonDifference(button);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent s) {

				showDifferenceSpectrum = !showDifferenceSpectrum;
				updateButtonDifference(button);
				updateInput();
			}
		});

		buttonDifferenceSpectrum.set(button);
	}

	private void updateButtonDifference(Button button) {

		setButtonImage(button, IApplicationImage.IMAGE_SUBTRACT_SCAN_DEFAULT, PREFIX_ENABLE, PREFIX_DISABLE, "showing the difference spectrum.", showDifferenceSpectrum);
	}

	private void createButtonShiftReferenceSpectrum(Composite parent) {

		Button button = new Button(parent, SWT.TOGGLE);
		button.setText("");
		button.setToolTipText("Shift the reference spectrum.");
		updateButtonShiftReference(button);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent s) {

				shiftReferenceSpectrum = !shiftReferenceSpectrum;
				updateButtonShiftReference(button);
				updateInput();
			}
		});

		buttonShiftReferenceSpectrum.set(button);
	}

	private void updateButtonShiftReference(Button button) {

		setButtonImage(button, IApplicationImage.IMAGE_SHIFTED_MASS_SPECTRUM, PREFIX_ENABLE, PREFIX_DISABLE, "showing the reference spectrum with a mass shift.", shiftReferenceSpectrum);
	}

	private void createSpinnerShiftReferenceSpectrum(Composite parent) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMinimum(1);
		spinner.setMaximum(50);
		spinner.setPageIncrement(1);
		spinner.setSelection(shiftMass);
		spinner.setToolTipText("Determine the shitf mass.");
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		spinner.setLayoutData(gridData);

		spinner.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.keyCode == SWT.LF || e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					updateShiftMass();
				}
			}
		});

		spinner.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				if(e.button == 1) {
					updateShiftMass();
				}
			}
		});

		spinnerShiftReferenceSpectrum.set(spinner);
	}

	private void updateShiftMass() {

		shiftMass = spinnerShiftReferenceSpectrum.get().getSelection();
		updateInput();
	}

	private void createToggleLegendButton(Composite parent) {

		buttonLegendControl.set(createButtonToggleChartLegend(parent, scanChartControl, IMAGE_LEGEND));
	}

	private void createSettingsButton(Composite parent) {

		ISettingsHandler settingsHandler = new ISettingsHandler() {

			@Override
			public void apply(Display display) {

				applySettings();
			}
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

		/*
		 * Default pages
		 */
		List<Class<? extends IPreferencePage>> preferencePages = new ArrayList<>();
		preferencePages.add(PreferencePageScans.class);
		preferencePages.add(PreferencePageSubtract.class);
		/*
		 * Additional pages.
		 */
		DataType scanDataType = getScanDataType();
		Object[] scanIdentifierServices = Activator.getDefault().getScanIdentifierServices();
		if(scanIdentifierServices != null) {
			for(Object object : scanIdentifierServices) {
				if(object instanceof IScanIdentifierService scanIdentifierService) {
					DataType dataType = scanIdentifierService.getDataType();
					if(scanDataType.equals(dataType)) {
						Class<? extends IWorkbenchPreferencePage> preferencePage = scanIdentifierService.getPreferencePage();
						if(preferencePage != null) {
							preferencePages.add(preferencePage);
						}
					}
				}
			}
		}

		return preferencePages;
	}

	private DataType getScanDataType() {

		if(scanUnknown instanceof IScanCSD) {
			return DataType.CSD;
		} else if(scanUnknown instanceof IScanMSD) {
			return DataType.MSD;
		} else if(scanUnknown instanceof IScanWSD) {
			return DataType.WSD;
		} else if(scanUnknown instanceof IScanVSD) {
			return DataType.VSD;
		}

		return DataType.NONE;
	}

	private void updateInput(IScanMSD unknownMassSpectrum, IScanMSD referenceMassSpectrum) {

		scanUnknown = copyScan(unknownMassSpectrum);
		scanReference = copyScan(referenceMassSpectrum);
		updateInput();
	}

	private void updateInput() {

		updateWidgets();
		Display.getDefault().asyncExec(this::updateChart);
	}

	private void updateWidgets() {

		boolean enabled = scanUnknown != null && scanReference != null;
		toolbarHybridSearch.get().setEnabled(enabled);
		buttonSubtractReference.get().setEnabled(enabled);
		updateIdentifierControl();
	}

	private void updateIdentifierControl() {

		scanIdentifierControl.get().setInput(scanUnknown);
		scanIdentifierControl.get().setEnabled(scanUnknown != null);
	}

	private void reset() {

		updateChart();
	}

	private void applySettings() {

		scanIdentifierControl.get().updateIdentifier();
		updateChart();
	}

	private void assignScan(IScanMSD scanMSD, boolean unknown) {

		if(scanMSD == null) {
			if(unknown) {
				scanUnknown = null;
			} else {
				scanReference = null;
			}
		} else {
			IScanMSD copy = copyScan(scanMSD);
			if(unknown) {
				scanUnknown = copy;
			} else {
				scanReference = copy;
			}
		}
	}

	private void updateScanComparisonDifference() {

		updateToolbarInfoDifference();
		IExtractedIonSignal extractedIonSignalReference = scanUnknown.getExtractedIonSignal();
		IExtractedIonSignal extractedIonSignalComparison = scanReference.getExtractedIonSignal();
		int startIon = (extractedIonSignalReference.getStartIon() < extractedIonSignalComparison.getStartIon()) ? extractedIonSignalReference.getStartIon() : extractedIonSignalComparison.getStartIon();
		int stopIon = (extractedIonSignalReference.getStopIon() > extractedIonSignalComparison.getStopIon()) ? extractedIonSignalReference.getStopIon() : extractedIonSignalComparison.getStopIon();

		IScanMSD scanDifference1 = new ScanMSD();
		IScanMSD scanDifference2 = new ScanMSD();

		for(int ion = startIon; ion <= stopIon; ion++) {
			float abundance = extractedIonSignalReference.getAbundance(ion) - extractedIonSignalComparison.getAbundance(ion);
			if(abundance > 0) {
				scanDifference1.addIon(getIon(ion, abundance));
			} else if(abundance < 0) {
				abundance *= -1;
				if(shiftReferenceSpectrum) {
					scanDifference2.addIon(getIon(ion + shiftMass, abundance));
				} else {
					scanDifference2.addIon(getIon(ion, abundance));
				}
			}
		}

		scanChartControl.get().setInput(scanDifference1, scanDifference2, useMirroredSpectrum);
	}

	private IIon getIon(int mz, float abundance) {

		return new Ion(mz, abundance);
	}

	private void updateToolbarInfoDifference() {

		updateToolbarInfoSpecial(PREFIX_UR, PREFIX_UR);
	}

	private void updateToolbarInfoSpecial(String prefixUnknown, String prefixReference) {

		toolbarInfoTop.get().setText(scanDataSupport.getMassSpectrumLabel(scanUnknown, prefixUnknown, TITLE_UNKNOWN, POSTFIX_NONE));
		toolbarInfoBottom.get().setText(scanDataSupport.getMassSpectrumLabel(scanReference, prefixReference, TITLE_REFERENCE, shiftReferenceSpectrum ? POSTFIX_SHIFTED + " (" + shiftMass + ")" : POSTFIX_NONE));
	}

	private void updateScanNormal() {

		toolbarInfoTop.get().setText("");
		toolbarInfoBottom.get().setText("");

		if(scanUnknown != null) {
			toolbarInfoTop.get().setText(scanDataSupport.getMassSpectrumLabel(scanUnknown, PREFIX_U, TITLE_UNKNOWN, POSTFIX_NONE));
			scanChartControl.get().setInput(scanUnknown);
		} else if(scanReference != null) {
			IScanMSD secondScan = scanReference;
			toolbarInfoTop.get().setText(scanDataSupport.getMassSpectrumLabel(secondScan, PREFIX_U, TITLE_UNKNOWN, POSTFIX_NONE));
			scanChartControl.get().setInput(secondScan);
		} else {
			scanChartControl.get().setInput(null);
		}
	}

	private void updateOnFocus() {

		DataUpdateSupport dataUpdateSupport = Activator.getDefault().getDataUpdateSupport();
		String topic = getLastTopic(dataUpdateSupport.getTopics());
		List<Object> objects = dataUpdateSupport.getUpdates(topic);
		if(!objects.isEmpty()) {
			Object last = objects.get(0);
			if(last instanceof IScanMSD scanMSD) {
				IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(scanMSD);
				update(scanMSD, identificationTarget);
			} else if(last instanceof IPeakMSD peakMSD) {
				IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(peakMSD);
				update(peakMSD.getExtractedMassSpectrum(), identificationTarget);
			} else if(last instanceof Object[] values) {
				Object first = values[0];
				Object second = values[1];
				if(IChemClipseEvents.TOPIC_SCAN_TARGET_UPDATE_COMPARISON.equals(topic)) {
					if(first instanceof IScanMSD unknownMassSpectrum && second instanceof IIdentificationTarget identificationTarget) {
						update(unknownMassSpectrum, identificationTarget);
					}
				} else if(IChemClipseEvents.TOPIC_SCAN_REFERENCE_UPDATE_COMPARISON.equals(topic)) {
					if(first instanceof IScanMSD unknownMassSpectrum && second instanceof IScanMSD referenceMassSpectrum) {
						updateInput(unknownMassSpectrum, referenceMassSpectrum);
					}
				}
			}
		}
	}

	private IScanMSD copyScan(IScanMSD scanMSD) {

		return copyScan(scanMSD, useOptimizedSpectrum);
	}

	private IScanMSD copyScan(IScanMSD scanMSD, boolean useOptimizedSpectrum) {

		if(scanMSD != null) {
			try {
				IScanMSD massSpectrum = scanMSD;
				if(useOptimizedSpectrum) {
					IScanMSD massSpectrumOptimized = scanMSD.getOptimizedMassSpectrum();
					if(massSpectrumOptimized != null) {
						massSpectrum = massSpectrumOptimized;
					}
				}
				return massSpectrum.makeDeepCopy().normalize(NORMALIZATION_FACTOR);
			} catch(CloneNotSupportedException e) {
			}
		}

		return null;
	}

	private void updateChart() {

		if(scanUnknown != null && scanReference != null) {
			if(showDifferenceSpectrum) {
				updateScanComparisonDifference();
			} else {
				updateScanComparisonNormal();
			}
			updateStackCharts();
		} else {
			updateScanNormal();
			clearStackCharts();
		}
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
			if(topic.equals(IChemClipseEvents.TOPIC_SCAN_TARGET_UPDATE_COMPARISON)) {
				return topic;
			}
			if(topic.equals(IChemClipseEvents.TOPIC_SCAN_REFERENCE_UPDATE_COMPARISON)) {
				return topic;
			}
		}

		return "";
	}
}