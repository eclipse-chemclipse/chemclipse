/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.chemclipse.chromatogram.msd.identifier.massspectrum.IMassSpectrumIdentifierSupplier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.massspectrum.IMassSpectrumIdentifierSupport;
import org.eclipse.chemclipse.chromatogram.msd.identifier.massspectrum.MassSpectrumIdentifier;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverterSupport;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.support.ui.workbench.PreferencesSupport;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.UpdateMenuEntry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class MassSpectrumChartCentroid extends BarChart implements IMassSpectrumChart {

	private static final int MAX_NUMBER_MZ = 50000;

	public enum LabelOption {
		NOMIMAL, EXACT, CUSTOM;
	}

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
			addSeriesData(barSeriesDataList, MAX_NUMBER_MZ);
		}
	}

	private void initialize() {

		setLayoutData(new GridData(GridData.FILL_BOTH));

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.setCreateMenu(true);

		chartSettings.addMenuEntry(new UpdateMenuEntry());
		addMassSpectrumIdentifier(chartSettings);
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
		addSecondaryAxisSet(chartSettings);
		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("m/z");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
		if(PreferencesSupport.isDarkTheme()) {
			primaryAxisSettingsX.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		} else {
			primaryAxisSettingsX.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		if(PreferencesSupport.isDarkTheme()) {
			primaryAxisSettingsY.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		} else {
			primaryAxisSettingsY.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}
	}

	private void addSecondaryAxisSet(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings("Relative Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		if(PreferencesSupport.isDarkTheme()) {
			secondaryAxisSettingsY.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		} else {
			secondaryAxisSettingsY.setColor(DisplayUtils.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}

	private ISeriesData getMassSpectrum(IScanMSD massSpectrum) {

		List<IIon> ions = massSpectrum.getIons();
		int size = ions.size();
		double[] xSeries = new double[size];
		double[] ySeries = new double[size];
		//
		for(int i = 0; i < size; i++) {
			IIon ion = ions.get(i);
			xSeries[i] = ion.getIon();
			ySeries[i] = ion.getAbundance();
		}

		return new SeriesData(xSeries, ySeries, "Mass Spectrum");
	}

	private void addMassSpectrumIdentifier(IChartSettings chartSettings) {

		IMassSpectrumIdentifierSupport massSpectrumIdentifierSupport = MassSpectrumIdentifier.getMassSpectrumIdentifierSupport();
		for(IMassSpectrumIdentifierSupplier supplier : massSpectrumIdentifierSupport.getSuppliers()) {
			chartSettings.addMenuEntry(new IChartMenuEntry() {

				@Override
				public String getName() {

					return supplier.getIdentifierName();
				}

				@Override
				public String getCategory() {

					return ICategories.IDENTIFIER;
				}

				@Override
				public Image getIcon() {

					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_IDENTIFY_MASS_SPECTRUM, IApplicationImageProvider.SIZE_16x16);
				}

				@Override
				public void execute(Shell shell, ScrollableChart scrollableChart) {

					if(massSpectrum != null) {
						MassSpectrumIdentifier.identify(massSpectrum, supplier.getId(), new NullProgressMonitor());
						update();
					}
				}
			});
		}
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
					fileDialog.setFilterExtensions(new String[]{"*" + supplier.getFileExtension()});
					fileDialog.setFilterNames(new String[]{supplier.getFilterName()});
					String pathname = fileDialog.open();
					if(pathname != null) {
						File file = new File(pathname);
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
						try {
							dialog.run(true, true, new IRunnableWithProgress() {

								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

									IProcessingInfo<File> convert = MassSpectrumConverter.convert(file, massSpectrum, false, supplier.getId(), monitor);
									ProcessingInfoPartSupport.getInstance().update(convert);
								}
							});
						} catch(InvocationTargetException e) {
							IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
							processingInfo.addErrorMessage("MS Export", "Export failed", e.getCause());
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
