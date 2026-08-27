/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.supplier.IScanProcessSupplier;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverterSupport;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.DefaultProcessingResult;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IMessageProvider;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.supplier.AbstractProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier.SupplierType;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.IProcessorPreferences;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.processing.system.ProcessSettingsSupport;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.support.formats.MagnitudeScaledDecimalFormat;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.msd.ui.handlers.DynamicHandler;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.UpdateMenuEntry;
import org.eclipse.chemclipse.ux.extension.ui.editors.ProcessorSupplierMenuEntry;
import org.eclipse.chemclipse.ux.extension.ui.methods.SettingsWizard;
import org.eclipse.chemclipse.ux.extension.ui.support.AuditTrailSupport;
import org.eclipse.chemclipse.xxd.process.comparators.CategoryNameComparator;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class MassSpectrumChartCentroid extends BarChart implements IMassSpectrumChart {

	private static final Logger logger = Logger.getLogger(MassSpectrumChartCentroid.class);

	private IScanMSD menuCache = null;
	private final List<IChartMenuEntry> cachedMenuEntries = new ArrayList<>();

	private IProcessSupplierContext processTypeSupport = new ProcessTypeSupport();

	private ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);

	private static final int MAX_NUMBER_MZ = 50000;
	private static final int LABEL_COUNT = 5;
	private static final DecimalFormatSymbols ENGLISH_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);

	public enum LabelOption {
		NOMIMAL, EXACT, CUSTOM;
	}

	private final DecimalFormat decimalFormatMZ = new DecimalFormat("0", ENGLISH_SYMBOLS);
	private final LabelPaintListener labelPaintListener = new LabelPaintListener();

	private IScanMSD massSpectrum = null;

	public MassSpectrumChartCentroid() {

		super();
		initialize();
	}

	public MassSpectrumChartCentroid(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	@Override
	public void update(IScanMSD massSpectrum) {

		this.massSpectrum = massSpectrum;
		update();
	}

	@Override
	public void update() {

		deleteSeries();
		if(massSpectrum != null) {
			List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
			ISeriesData seriesData = getMassSpectrum(massSpectrum);
			IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
			barSeriesDataList.add(barSeriesData);
			modifyRangeRestriction(false);
			addSeriesData(barSeriesDataList, MAX_NUMBER_MZ);
			updateAxis();
			updateMenu();
		}
	}

	public void update(IScanMSD combinedMassSpectrum, IScanMSD mirroredMassSpectrum) {

		deleteSeries();
		List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
		if(combinedMassSpectrum != null) {
			combinedMassSpectrum.normalize(1000);
			IBarSeriesData barSeriesData = new BarSeriesData(getMassSpectrum(combinedMassSpectrum, "Combined", false));
			barSeriesData.getSettings().setBarOverlay(true);
			barSeriesDataList.add(barSeriesData);
		}
		if(mirroredMassSpectrum != null) {
			mirroredMassSpectrum.normalize(1000);
			IBarSeriesData barSeriesData = new BarSeriesData(getMassSpectrum(mirroredMassSpectrum, "Mirrored", true));
			IBarSeriesSettings settings = barSeriesData.getSettings();
			settings.setBarColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			settings.setBarOverlay(true);
			barSeriesDataList.add(barSeriesData);
		}
		modifyRangeRestriction(mirroredMassSpectrum != null);
		if(!barSeriesDataList.isEmpty()) {
			addSeriesData(barSeriesDataList, MAX_NUMBER_MZ);
		}
	}

	private void updateAxis() {

		IChartSettings chartSettings = getChartSettings();
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		int exponent = MagnitudeScaledDecimalFormat.orderOfMagnitude(massSpectrum.getHighestAbundance().getAbundance());
		primaryAxisSettingsY.setDecimalFormat(new MagnitudeScaledDecimalFormat("0.#", ENGLISH_SYMBOLS, exponent));
		primaryAxisSettingsY.setHorizontalLabel("×10" + MagnitudeScaledDecimalFormat.toSuperscript(String.valueOf(exponent)));
		applySettings(chartSettings);
	}

	private void modifyRangeRestriction(boolean mirrored) {

		IChartSettings chartSettings = getChartSettings();
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		if(mirrored) {
			rangeRestriction.setExtendMinY(0.25d);
			rangeRestriction.setExtendMaxY(0.25d);
		} else {
			rangeRestriction.setExtendMinY(0.0d);
			rangeRestriction.setExtendMaxY(0.1d);
		}
		applySettings(chartSettings);
	}

	private void initialize() {

		setLayoutData(new GridData(GridData.FILL_BOTH));

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.setCreateMenu(true);

		chartSettings.addMenuEntry(new UpdateMenuEntry());
		addMassSpectrumExport(chartSettings);

		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setRestrictFrame(true);
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.ABSOLUTE);
		rangeRestriction.setExtendMinX(2.0d);
		rangeRestriction.setExtendMaxX(2.0d);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMaxY(0.1d);

		setPrimaryAxisSet(chartSettings);

		Color white = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		chartSettings.setBackground(white);
		chartSettings.setBackgroundChart(white);
		chartSettings.setBackgroundPlotArea(white);

		applySettings(chartSettings);

		IPlotArea plotArea = getBaseChart().getPlotArea();
		plotArea.addCustomPaintListener(labelPaintListener);
	}

	private void updateMenu() {

		IChartSettings chartSettings = getChartSettings();
		if(processTypeSupport != null && menuCache != massSpectrum) {
			/*
			 * Clean the Menu
			 */
			for(IChartMenuEntry cachedEntry : cachedMenuEntries) {
				chartSettings.removeMenuEntry(cachedEntry);
			}
			cachedMenuEntries.clear();
			/*
			 * Dynamic Menu Items
			 */
			List<IProcessSupplier<?>> processSupplierList = new ArrayList<>(processTypeSupport.getSupplier(this::isValidSupplier));
			Collections.sort(processSupplierList, new CategoryNameComparator());
			for(IProcessSupplier<?> processSupplier : processSupplierList) {
				IChartMenuEntry chartMenuEntry = new ProcessorSupplierMenuEntry<>(processSupplier, processTypeSupport, this::executeSupplier);
				cachedMenuEntries.add(chartMenuEntry);
				chartSettings.addMenuEntry(chartMenuEntry);
				addCommand(processSupplier, chartMenuEntry);
			}
			/*
			 * Apply the menu items.
			 */
			applySettings(chartSettings);
			menuCache = massSpectrum;
		}
	}

	private boolean isValidSupplier(IProcessSupplier<?> supplier) {

		if(supplier.getType() == SupplierType.STRUCTURAL) {
			return false;
		}

		return supplier.getCategory() == ICategories.MASS_SPECTRUM_IDENTIFIER;
	}

	private void addCommand(IProcessSupplier<?> supplier, IChartMenuEntry cachedEntry) {

		Command command = commandService.getCommand(supplier.getId());
		Category category = commandService.getCategory(supplier.getCategory());
		command.define(supplier.getName(), supplier.getDescription(), category);
		command.setHandler(new DynamicHandler(cachedEntry, this));
	}

	private <C> void executeSupplier(IProcessSupplier<C> processSupplier, IProcessSupplierContext processSupplierContext) {

		try {
			Shell shell = getShell();
			IProcessorPreferences<C> settings = SettingsWizard.getSettings(shell, ProcessSettingsSupport.getWorkspacePreferences(processSupplier), true);
			if(settings == null) {
				return;
			}
			/*
			 * Apply
			 */
			processMassSpectrum(monitor -> executeMethod(massSpectrum, scanMSD -> {

				DefaultProcessingResult<Object> processingInfo = new DefaultProcessingResult<>();
				ProcessExecutionContext processExecutionContext = new ProcessExecutionContext(monitor, processingInfo, processSupplierContext);
				IScan result = AbstractProcessSupplier.applyProcessor(settings, IScanProcessSupplier.createConsumer(scanMSD), processExecutionContext);
				updateResult(processingInfo);
				if(scanMSD instanceof IStandaloneMassSpectrum standaloneMassSpectrum) {
					AuditTrailSupport.updateAuditTrail(standaloneMassSpectrum, processingInfo, processSupplier);
				}
				UpdateNotifierUI.update(getDisplay(), result);
			}), shell);
		} catch(IOException e) {
			DefaultProcessingResult<Object> processingInfo = new DefaultProcessingResult<>();
			processingInfo.addErrorMessage(processSupplier.getName(), "The process method can't be applied.");
			logger.error(e);
			updateResult(processingInfo);
		}
	}

	private void processMassSpectrum(IRunnableWithProgress runnable, Shell shell) {

		ProgressMonitorDialog monitor = new ProgressMonitorDialog(shell);
		try {
			monitor.run(true, true, runnable);
			massSpectrum.setDirty(true);
			update();
		} catch(InterruptedException e) {
			logger.error(e);
			Thread.currentThread().interrupt();
		} catch(InvocationTargetException e) {
			logger.warn(e);
			logger.warn(e.getCause());
		}
	}

	public void updateResult(IMessageProvider processingInfo) {

		getDisplay().asyncExec(() -> ProcessingInfoPartSupport.getInstance().update(processingInfo, true));
	}

	private void executeMethod(IScanMSD scanMSD, Consumer<IScanMSD> consumer) {

		if(scanMSD != null) {
			consumer.accept(scanMSD);
		}
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("m/z");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0"), ENGLISH_SYMBOLS));

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
	}

	private class LabelPaintListener implements ICustomPaintListener {

		@Override
		public void paintControl(PaintEvent e) {

			List<double[]> positiveValues = new ArrayList<>();
			List<double[]> negativeValues = new ArrayList<>();
			for(double[] entry : getVisibleBarSeriesValues()) {
				if(entry[1] >= 0) {
					positiveValues.add(entry);
				} else {
					negativeValues.add(entry);
				}
			}
			/*
			 * Positive series: label above bar tip.
			 */
			positiveValues.sort((a, b) -> Double.compare(b[1], a[1]));
			int limitPositive = Math.min(LABEL_COUNT, positiveValues.size());
			for(int i = 0; i < limitPositive; i++) {
				double[] entry = positiveValues.get(i);
				String label = decimalFormatMZ.format(entry[0]);
				Point labelSize = e.gc.textExtent(label);
				int x = (int)(entry[2] + 0.5d - labelSize.x / 2.0d);
				int y = (int)entry[3] - labelSize.y;
				e.gc.drawText(label, x, y, true);
			}
			/*
			 * Mirrored (negative) series: label below bar tip.
			 * Sort ascending so the most intense (most negative) peaks come first.
			 */
			negativeValues.sort((a, b) -> Double.compare(a[1], b[1]));
			int limitNegative = Math.min(LABEL_COUNT, negativeValues.size());
			for(int i = 0; i < limitNegative; i++) {
				double[] entry = negativeValues.get(i);
				String label = decimalFormatMZ.format(entry[0]);
				Point labelSize = e.gc.textExtent(label);
				int x = (int)(entry[2] + 0.5d - labelSize.x / 2.0d);
				int y = (int)entry[3];
				e.gc.drawText(label, x, y, true);
			}
		}

		@Override
		public boolean drawBehindSeries() {

			return false;
		}
	}

	private List<double[]> getVisibleBarSeriesValues() {

		List<double[]> values = new ArrayList<>();
		BaseChart baseChart = getBaseChart();
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		ISeries<?>[] seriesArray = baseChart.getSeriesSet().getSeries();
		for(ISeries<?> series : seriesArray) {
			if(series != null) {
				double[] xSeries = series.getXSeries();
				double[] ySeries = series.getYSeries();
				int size = xSeries.length;
				for(int i = 0; i < size; i++) {
					Point point = series.getPixelCoordinates(i);
					if(point.x >= 0 && point.x <= widthPlotArea) {
						values.add(new double[]{xSeries[i], ySeries[i], point.x, point.y});
					}
				}
			}
		}
		return values;
	}

	private ISeriesData getMassSpectrum(IScanMSD massSpectrum) {

		return getMassSpectrum(massSpectrum, "Mass Spectrum", false);
	}

	private ISeriesData getMassSpectrum(IScanMSD massSpectrum, String id, boolean mirrored) {

		List<IIon> ions = massSpectrum.getIons();
		int size = ions.size();
		double[] xSeries = new double[size];
		double[] ySeries = new double[size];

		for(int i = 0; i < size; i++) {
			IIon ion = ions.get(i);
			xSeries[i] = ion.getIon();
			ySeries[i] = mirrored ? ion.getAbundance() * -1 : ion.getAbundance();
		}

		return new SeriesData(xSeries, ySeries, id);
	}

	private void addMassSpectrumExport(IChartSettings chartSettings) {

		MassSpectrumConverterSupport converterSupport = MassSpectrumConverter.getMassSpectrumConverterSupport();
		List<ISupplier> exportSupplier = converterSupport.getExportSupplier();
		for(ISupplier supplier : exportSupplier) {
			chartSettings.addMenuEntry(new IChartMenuEntry() {

				@Override
				public String getName() {

					return supplier.getFilterName();
				}

				@Override
				public String getToolTipText() {

					return supplier.getDescription();
				}

				@Override
				public String getCategory() {

					return "Export";
				}

				@Override
				public void execute(Shell shell, ScrollableChart scrollableChart) {

					if(massSpectrum == null) {
						return;
					}

					FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
					fileDialog.setText("Mass Spectrum Export");
					fileDialog.setFileName("Mass Spectrum." + supplier.getFileExtension());
					fileDialog.setFilterExtensions("*" + supplier.getFileExtension());
					fileDialog.setFilterNames(supplier.getFilterName());
					String pathname = fileDialog.open();
					if(pathname != null) {
						File file = new File(pathname);
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
						try {
							dialog.run(true, true, monitor -> {
								IProcessingInfo<File> convert = MassSpectrumConverter.convert(file, massSpectrum, false, supplier.getId(), monitor);
								ProcessingInfoPartSupport.getInstance().update(convert);
							});
						} catch(InvocationTargetException e) {
							IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
							processingInfo.addErrorMessage("MS Export", "Export failed");
							logger.error(e.getCause());
							ProcessingInfoPartSupport.getInstance().update(processingInfo);
						} catch(InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}
			});
		}
	}
}