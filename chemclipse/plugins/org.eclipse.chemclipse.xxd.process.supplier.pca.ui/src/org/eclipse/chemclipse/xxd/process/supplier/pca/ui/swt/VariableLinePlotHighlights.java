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
 * Lorenz Gerber- initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.swt;

import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;

public class VariableLinePlotHighlights extends AbstractBaseChartPaintListener {

	private int[] highlightedIndices;

	public VariableLinePlotHighlights(BaseChart baseChart, int[] highlightedIndices) {

		super(baseChart);
		this.highlightedIndices = highlightedIndices;
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(!getBaseChart().isBufferActive()) {
			GC gc = e.gc;
			plotHighlights(gc);
		} else {
			plotHighlights(e.gc);
		}

	}

	private void plotHighlights(GC gc) {

		BaseChart baseChart = getBaseChart();
		if(!baseChart.getSeriesIds().isEmpty()) {
			double[] xSeries = baseChart.getSeriesSet().getSeries("main").getXSeries();
			double[] ySeries = baseChart.getSeriesSet().getSeries("main").getYSeries();

			for(int index : highlightedIndices) {
				int[] highlightPoint = convertDataToPixelCoordinates(baseChart, xSeries[index], ySeries[index]);
				int size = 15;
				gc.setForeground(Colors.RED);
				gc.setLineWidth(2);
				gc.setLineStyle(SWT.LINE_SOLID);
				gc.drawOval(highlightPoint[0] - size / 2, highlightPoint[1] - size / 2, size, size);
			}

		}

	}

	private static int[] convertDataToPixelCoordinates(Chart chart, double xValue, double yValue) {

		int[] pixelCoordinates = new int[2];
		Range xRange = chart.getAxisSet().getXAxis(0).getRange();
		Range yRange = chart.getAxisSet().getYAxis(0).getRange();
		int plotAreaWidth = chart.getPlotArea().getBounds().width;
		int plotAreaHeight = chart.getPlotArea().getBounds().height;

		double unitX = plotAreaWidth / (xRange.upper - xRange.lower + 1);
		pixelCoordinates[0] = (int)(unitX * 0.5 + (xValue - xRange.lower) * unitX);
		pixelCoordinates[1] = (int)(plotAreaHeight - ((yValue - yRange.lower) / (yRange.upper - yRange.lower) * plotAreaHeight));

		return pixelCoordinates;
	}

}
