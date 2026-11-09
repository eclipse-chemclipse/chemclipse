/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IAnalysisSettings;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SeriesData;

public class FilterDistributionChart extends BarChart {

	private IAnalysisSettings settings;

	public FilterDistributionChart() {

		super();
		createControl();
	}

	public FilterDistributionChart(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setInput(IAnalysisSettings settings) {

		this.settings = settings;
		updateChart();
	}

	private void createControl() {

		IChartSettings chartSettings = getChartSettings();

		chartSettings.setTitleVisible(false);
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(false);
		chartSettings.setVerticalSliderVisible(false);

		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setForceZeroMinY(true);
		rangeRestriction.setRestrictFrame(true);

		chartSettings.setShowAxisZeroMarker(true);
		chartSettings.setShowSeriesLabelMarker(false);
		chartSettings.setCreateMenu(true);
		chartSettings.setEnableCompress(false);

		setPrimaryAxisSet(chartSettings);

		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(StatisticsMessages.numberOfOverlaps);
		primaryAxisSettingsX.setDecimalFormat(ValueFormat.getDecimalFormatEnglish());

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(StatisticsMessages.count);
		primaryAxisSettingsY.setDecimalFormat(ValueFormat.getDecimalFormatEnglish());
	}

	private void updateChart() {

		deleteSeries();
		if(settings != null) {
			settings.getFilterDistribution();

			IChartSettings chartSettings = getChartSettings();
			IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
			primaryAxisSettingsX.setEnableCategory(true);
			primaryAxisSettingsX.setCategorySeries(getCategories(settings));
			applySettings(chartSettings);

			List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
			ISeriesData seriesData = getSeries(settings);
			IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
			IBarSeriesSettings barSeriesSettings = barSeriesData.getSettings();
			barSeriesSettings.setBarColor(Colors.RED);
			barSeriesSettings.setBarWidthStyle(BarWidthStyle.STRETCHED);
			barSeriesDataList.add(barSeriesData);
			addSeriesData(barSeriesDataList);
		} else {
			getBaseChart().redraw();
		}
	}

	private String[] getCategories(IAnalysisSettings settings) {

		Set<Integer> keys = settings.getFilterDistribution().reversed().keySet();
		return keys.stream().map(Object::toString).toArray(String[]::new);
	}

	private ISeriesData getSeries(IAnalysisSettings settings) {

		String label = StatisticsMessages.counts;
		double[] ySeries = new double[settings.getFilterDistribution().values().size()];
		int index = settings.getFilterDistribution().values().size() - 1;
		for(Map.Entry<Integer, Integer> entry : settings.getFilterDistribution().entrySet()) {
			ySeries[index--] = entry.getValue();
		}
		getChartSettings().getPrimaryAxisSettingsY().setTitle(label);
		applySettings(getChartSettings());
		double[] xSeries = new double[ySeries.length];
		return new SeriesData(xSeries, ySeries, label);
	}
}
