/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.selection.ChromatogramSelection;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.ILibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.util.FileListUtil;
import org.eclipse.chemclipse.ux.extension.msd.ui.support.DatabaseImportRunnable;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.ui.swt.IExtendedPartUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.charts.ChartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.EditorUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageChromatogram;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScanBrowse;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageScans;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisIntensity;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisIon;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.ScanChartAxisRelativeIntensity;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ScanDataSupport;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;

public class ExtendedScanBrowseUI extends Composite implements IExtendedPartUI {

	private static final Logger logger = Logger.getLogger(ExtendedScanBrowseUI.class);

	private Label labelInfo;

	private Button buttonPreviousScan;
	private ComboViewer comboViewerType;
	private ComboScanSource comboScanSource;
	private Button buttonNextScan;
	private ScanChartUI scanChartUI;

	private IChromatogramSelection chromatogramSelection;
	private int masterRetentionTime;
	private List<IScan> libraryScans = null;

	private final ScanDataSupport scanDataSupport = new ScanDataSupport();

	private enum Type {

		EXTERNAL("Editors"), //
		INTERNAL("References"), //
		BOTH("Both"), //
		LIBRARY("Library"); //

		private String label = "";

		private Type(String label) {

			this.label = label;
		}

		public String getLabel() {

			return label;
		}
	}

	public ExtendedScanBrowseUI(Composite parent, int style) {

		super(parent, style);
		createControl();
		adjustAxisSettings();
	}

	public void update(IChromatogramSelection chromatogramSelection) {

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

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		createToolbarMain(composite);
		Composite toolbarInfo = createToolbarInfo(composite);
		scanChartUI = createScanChart(composite);

		PartSupport.setCompositeVisibility(toolbarInfo, true);
	}

	private Composite createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(5, false));

		comboViewerType = createComboViewerType(composite);
		buttonPreviousScan = createPreviousReferenceScanButton(composite);
		comboScanSource = createComboScanSource(composite);
		buttonNextScan = createNextReferenceScanButton(composite);
		createSettingsButton(composite);

		return composite;
	}

	private ComboViewer createComboViewerType(Composite parent) {

		ComboViewer comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
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

		comboViewer.setInput(Type.values());
		combo.select(0);

		return comboViewer;
	}

	private Button createPreviousReferenceScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Get the scan of the previous source.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PREVIOUS_YELLOW, IApplicationImageProvider.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectScan(-1);
			}
		});
		return button;
	}

	private ComboScanSource createComboScanSource(Composite parent) {

		ComboScanSource comboSource = new ComboScanSource(parent, SWT.NONE);
		comboSource.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboSource.setLabelFunction(element -> {
			String label = "";
			if(element instanceof IChromatogramSelection chromatogramSelectionSource) {
				IChromatogram chromatogram = chromatogramSelectionSource.getChromatogram();
				label = ChromatogramDataSupport.getReferenceLabel(chromatogram, -1, false);
			} else if(element instanceof ILibraryMassSpectrum libraryMassSpectrum) {
				ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
				label = (libraryInformation != null) ? libraryInformation.getReferenceIdentifier() : "";
			}
			return label;
		});
		comboSource.setSelectionListener(_ -> updateSource());

		return comboSource;
	}

	private Button createNextReferenceScanButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Get the scan of the next source.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_NEXT_YELLOW, IApplicationImageProvider.SIZE_16x16));
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
				PreferencePageScanBrowse.class, //
				PreferencePageScans.class, //
				ScanChartAxisIon.class, //
				ScanChartAxisIntensity.class, //
				ScanChartAxisRelativeIntensity.class, //
				PreferencePageChromatogram.class //
		), _ -> applySettings());
	}

	private void applySettings() {

		adjustAxisSettings();
	}

	private Composite createToolbarInfo(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		labelInfo = createLabel(composite);

		return composite;
	}

	private Label createLabel(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return label;
	}

	private ScanChartUI createScanChart(Composite parent) {

		ScanChartUI scanChartUI = new ScanChartUI(parent, SWT.BORDER);
		scanChartUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		return scanChartUI;
	}

	private void selectScan(int moveIndex) {

		comboScanSource.moveSelection(moveIndex);
		updateSource();
	}

	private void updateSource() {

		/*
		 * Update the chart and label
		 */
		IScan referenceScan = null;
		Object object = comboScanSource.getSelection();
		if(object instanceof IChromatogramSelection chromatogramSelectionSource) {
			IChromatogram chromatogram = chromatogramSelectionSource.getChromatogram();
			int scanNumber = chromatogram.getScanNumber(masterRetentionTime);
			referenceScan = chromatogram.getScan(scanNumber);
		} else if(object instanceof IScan scan) {
			referenceScan = scan;
		}

		updateLabel(referenceScan);
		updateChart(referenceScan);
		updatePreviousAndNextButton();
	}

	private void updateScan(IScan scan) {

		updateLabel(scan);
		updateChart(scan);
		updateComboViewer();
		updatePreviousAndNextButton();
		adjustAxisSettings();
	}

	private void updateLabel(IScan scan) {

		if(scan instanceof ILibraryMassSpectrum libraryMassSpectrum) {
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			labelInfo.setText((libraryInformation != null) ? libraryInformation.getName() : "");
		} else {
			labelInfo.setText(scanDataSupport.getScanLabel(scan));
		}
	}

	private void updateChart(IScan scan) {

		scanChartUI.setInput(scan);
		scanChartUI.getBaseChart().redraw();
	}

	private void updateComboViewer() {

		Type type = getSelectedType();
		List<Object> sources = new ArrayList<>();
		if(Type.LIBRARY.equals(type)) {
			sources.addAll(extractLibrary());
		} else {
			/*
			 * Add this selection as the first entry.
			 */
			if(chromatogramSelection != null) {
				sources.add(chromatogramSelection);
			}
			/*
			 * Add the references
			 */
			switch(type) {
				case INTERNAL:
					sources.addAll(extractInternal());
					break;
				case EXTERNAL:
					sources.addAll(extractExternal());
					break;
				default:
					sources.addAll(extractInternal());
					sources.addAll(extractExternal());
					break;
			}
		}
		/*
		 * Set and select the references. Type to search for libraries.
		 */
		comboScanSource.setEditable(Type.LIBRARY.equals(type));
		comboScanSource.setInput(sources);
		if(!sources.isEmpty()) {
			comboScanSource.selectFirst();
			if(Type.LIBRARY.equals(type)) {
				updateSource();
			}
		}
	}

	private void updatePreviousAndNextButton() {

		buttonPreviousScan.setEnabled(comboScanSource.hasPrevious());
		buttonNextScan.setEnabled(comboScanSource.hasNext());
	}

	private Type getSelectedType() {

		Object object = comboViewerType.getStructuredSelection().getFirstElement();
		if(object instanceof Type type) {
			return type;
		}
		return Type.BOTH;
	}

	private List<IChromatogramSelection> extractInternal() {

		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		if(chromatogramSelection != null) {
			Object object = chromatogramSelection.getChromatogram();
			if(object instanceof IChromatogram chromatogram) {
				List<IChromatogram> chromatograms = chromatogram.getReferencedChromatograms();
				for(IChromatogram chromatogramReference : chromatograms) {
					chromatogramSelections.add(new ChromatogramSelection(chromatogramReference));
				}
			}
		}
		return chromatogramSelections;
	}

	private List<IChromatogramSelection> extractExternal() {

		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		EditorUpdateSupport editorUpdateSupport = new EditorUpdateSupport();
		if(chromatogramSelection != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			for(IChromatogramSelection chromatogramSelectionEditor : editorUpdateSupport.getChromatogramSelections()) {
				if(chromatogram != chromatogramSelectionEditor.getChromatogram()) {
					chromatogramSelections.add(chromatogramSelectionEditor);
				}
			}
		} else {
			chromatogramSelections.addAll(editorUpdateSupport.getChromatogramSelections());
		}

		return chromatogramSelections;
	}

	private List<IScan> extractLibrary() {

		if(libraryScans != null) {
			return null;
		}
		libraryScans = new ArrayList<>();
		String path = PreferenceSupplier.getScanBrowseLibraryFile();
		if(path == null || path.isEmpty()) {
			return null;
		}
		File libraryFile = new File(path);
		if(libraryFile.isDirectory()) {
			FileListUtil fileListUtil = new FileListUtil();
			List<String> files = fileListUtil.getFiles(FileListUtil.getAllContainingFilesAbsolutePath(libraryFile));
			for(String file : files) {
				importDatabase(new File(file));
			}
		} else if(libraryFile.exists()) {
			importDatabase(libraryFile);
			logger.warn("The library is not available: " + libraryFile.getAbsolutePath());
		}
		return libraryScans;
	}

	private void importDatabase(File file) {

		DatabaseImportRunnable runnable = new DatabaseImportRunnable(file);
		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
			dialog.run(true, true, runnable);
			IMassSpectra massSpectra = runnable.getMassSpectra();
			if(massSpectra != null) {
				libraryScans.addAll(massSpectra.getList());
			}
			dialog.close();
		} catch(InvocationTargetException e) {
			logger.warn(e);
		} catch(InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		}
	}

	private void adjustAxisSettings() {

		adjustAxisIons();
		adjustAxisIntensity();
		adjustAxisRelativeIntensity();

		IChartSettings chartSettings = scanChartUI.getChartSettings();
		scanChartUI.applySettings(chartSettings);
	}

	private void adjustAxisIons() {

		IChartSettings chartSettings = scanChartUI.getChartSettings();
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

		IChartSettings chartSettings = scanChartUI.getChartSettings();
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

		IChartSettings chartSettings = scanChartUI.getChartSettings();
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
}
