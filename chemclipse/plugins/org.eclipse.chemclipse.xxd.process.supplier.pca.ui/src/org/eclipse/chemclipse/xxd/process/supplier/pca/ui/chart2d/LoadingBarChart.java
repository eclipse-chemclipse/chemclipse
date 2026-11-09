/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Lorenz Gerber- initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SeriesData;

public class LoadingBarChart extends BarChart {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00E0", new DecimalFormatSymbols(Locale.ENGLISH)); //$NON-NLS-1$
	private String title = StatisticsMessages.loadingBarChart;
	private int currentPC = 0;
	private ICustomPaintListener currentPaintListener = null;

	public LoadingBarChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitle(title);
		chartSettings.setTitleVisible(true);
		chartSettings.setBackground(null);
		chartSettings.setBackgroundChart(null);
		chartSettings.setBackgroundPlotArea(null);
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);

		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setRestrictFrame(true);
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtend(0.1d);
		rangeRestriction.setRestrictSelectX(true);
		rangeRestriction.setReferenceZoomZeroX(true);

		chartSettings.setShowAxisZeroMarker(true);
		chartSettings.setCreateMenu(true);
		chartSettings.setEnableCompress(false);

		// X-axis settings (sample names)
		IPrimaryAxisSettings xAxisSettings = chartSettings.getPrimaryAxisSettingsX();
		xAxisSettings.setTitle(StatisticsMessages.variablesSortedByLoadings);
		xAxisSettings.setVisible(true);
		xAxisSettings.setDecimalFormat(DECIMAL_FORMAT);

		// Y-axis settings (scores)
		IPrimaryAxisSettings yAxisSettings = chartSettings.getPrimaryAxisSettingsY();
		yAxisSettings.setTitle(StatisticsMessages.loading);
		yAxisSettings.setVisible(true);
		yAxisSettings.setDecimalFormat(DECIMAL_FORMAT);

		applySettings(chartSettings);
	}

	public void setInput(EvaluationPCA evaluationPCA, int principalComponent) {

		deleteSeries();
		currentPC = principalComponent;

		if(evaluationPCA == null) {
			getBaseChart().redraw();
			return;
		}

		IResultsMVA results = evaluationPCA.getResults();

		String chartTitle = NLS.bind(StatisticsMessages.loadingPlotPc, this.currentPC + 1);
		getChartSettings().setTitle(chartTitle);

		List<VariableLoading> variableLoadings = new ArrayList<>();
		for(int i = 0; i < results.getExtractedVariables().size(); i++) {
			if(this.currentPC <= results.getLoadingVectors().size()) {
				double loading = results.getLoadingVectors().get(this.currentPC)[i];
				String variableName = results.getExtractedVariables().get(i).getValue();
				variableLoadings.add(new VariableLoading(variableName, loading));
			}
		}

		variableLoadings.sort(Comparator.comparingDouble(VariableLoading::getLoading).reversed());

		int numberOfBars = variableLoadings.size();
		double[] xValues = new double[numberOfBars];
		double[] yValues = new double[numberOfBars];
		String[] labels = new String[numberOfBars];

		// Store colors for each bar
		final Color[] barColors = new Color[numberOfBars];
		final Set<String> highlightedNames = evaluationPCA.getHighlightedVariables().stream().map(IVariable::getValue).collect(Collectors.toSet());

		for(int i = 0; i < numberOfBars; i++) {
			xValues[i] = i;
			yValues[i] = variableLoadings.get(i).getLoading();
			labels[i] = ""; //$NON-NLS-1$

			String variableName = variableLoadings.get(i).getVariableName();
			barColors[i] = highlightedNames.contains(variableName) ? Colors.BLUE : Colors.RED;
		}

		// Create a SINGLE series
		List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
		IBarSeriesData barData = new BarSeriesData(new SeriesData(xValues, yValues, "Loadings")); //$NON-NLS-1$
		IBarSeriesSettings settings = barData.getSettings();
		settings.setBarColor(Colors.RED);
		settings.setBarWidthStyle(BarWidthStyle.STRETCHED);
		settings.setBarPadding(15);
		barSeriesDataList.add(barData);

		addSeriesData(barSeriesDataList);

		if(currentPaintListener != null) {
			getBaseChart().getPlotArea().removeCustomPaintListener(currentPaintListener);
		}

		getBaseChart().getPlotArea().addCustomPaintListener(new ICustomPaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				ISeries<?> series = getBaseChart().getSeriesSet().getSeries("Loadings"); //$NON-NLS-1$
				if(series instanceof IBarSeries) {
					IBarSeries<?> barSeries = (IBarSeries<?>)series;
					Rectangle[] bounds = barSeries.getBounds();

					GC gc = e.gc;
					for(int i = 0; i < bounds.length && i < barColors.length; i++) {
						if(bounds[i] != null) {
							gc.setBackground(barColors[i]);
							gc.fillRectangle(bounds[i]);
						}
					}
				}
			}

			@Override
			public boolean drawBehindSeries() {

				return false;
			}
		});

		// Set custom category labels for X-axis
		IAxis xAxis = getBaseChart().getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(labels);
		xAxis.enableCategory(true);

		// Adjust Y-axis to center zero
		IAxis yAxis = getBaseChart().getAxisSet().getYAxis(0);
		double maxAbs = Math.max(Math.abs(getMaxValue(yValues)), Math.abs(getMinValue(yValues)));
		if(maxAbs > 0) {
			yAxis.setRange(new Range(-maxAbs * 1.1, maxAbs * 1.1));
		}

		getBaseChart().redraw();

	}

	private double getMaxValue(double[] values) {

		double max = Double.NEGATIVE_INFINITY;
		for(double v : values) {
			if(v > max) {
				max = v;
			}
		}
		return max;
	}

	private double getMinValue(double[] values) {

		double min = Double.POSITIVE_INFINITY;
		for(double v : values) {
			if(v < min) {
				min = v;
			}
		}
		return min;
	}

	private static class VariableLoading {

		private final String variableName;
		private final double loading;

		public VariableLoading(String variableName, double loading) {

			this.variableName = variableName;
			this.loading = loading;
		}

		public String getVariableName() {

			return variableName;
		}

		public double getLoading() {

			return loading;
		}
	}

}
