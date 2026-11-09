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
 * Lorenz Gerber - Specific result type for multivariate analysis (MVA)
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.chart2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.IResultsMVA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.ui.l10n.StatisticsMessages;
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
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SeriesData;

public class ErrorResidueChart extends BarChart {

	private ICustomPaintListener currentPaintListener = null;

	public ErrorResidueChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitle(StatisticsMessages.errorResidues);
		chartSettings.setTitleVisible(true);
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

	public void setInput(EvaluationPCA evaluationPCA) {

		deleteSeries();

		if(evaluationPCA == null) {
			getBaseChart().redraw();
			return;
		}

		IResultsMVA results = evaluationPCA.getResults();
		List<IResultMVA> resultList = results.getPcaResultList();

		int numberOfBars = resultList.size();
		double[] xValues = new double[numberOfBars];
		double[] yValues = new double[numberOfBars];
		String[] labels = new String[numberOfBars];

		// Store colors for each bar
		final Color[] barColors = new Color[numberOfBars];
		final Set<String> highlightedNames = evaluationPCA.getHighlightedSamples().stream().map(ISample::getSampleName).collect(Collectors.toSet());

		for(int i = 0; i < numberOfBars; i++) {
			xValues[i] = i;
			yValues[i] = resultList.get(i).getErrorMetric();
			labels[i] = ""; //$NON-NLS-1$

			String sampleName = resultList.get(i).getSample().getSampleName();
			barColors[i] = highlightedNames.contains(sampleName) ? Colors.BLUE : Colors.RED;
		}

		// Create a SINGLE series
		List<IBarSeriesData> barSeriesDataList = new ArrayList<>();
		IBarSeriesData barData = new BarSeriesData(new SeriesData(xValues, yValues, "Scores")); //$NON-NLS-1$
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

				ISeries<?> series = getBaseChart().getSeriesSet().getSeries("Scores"); //$NON-NLS-1$
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

		getBaseChart().redraw();

	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(StatisticsMessages.sampleName);
		primaryAxisSettingsX.setDecimalFormat(ValueFormat.getDecimalFormatEnglish());

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(StatisticsMessages.errorValues);
		primaryAxisSettingsY.setDecimalFormat(ValueFormat.getDecimalFormatEnglish());
	}

}
