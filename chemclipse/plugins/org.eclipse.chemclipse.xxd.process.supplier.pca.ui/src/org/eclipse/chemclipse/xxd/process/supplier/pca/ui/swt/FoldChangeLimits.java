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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;

public class FoldChangeLimits extends AbstractBaseChartPaintListener {

	private static final int INVISIBLE = -1;

	public FoldChangeLimits(BaseChart baseChart) {

		super(baseChart);
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(!getBaseChart().isBufferActive()) {
			GC gc = e.gc;
			plotMarker(gc);
		} else {
			plotMarker(e.gc);
		}

	}

	private void plotMarker(GC gc) {

		int lineWidth = gc.getLineWidth();
		int lineStyle = gc.getLineStyle();
		Color lineColor = gc.getForeground();
		BaseChart baseChart = getBaseChart();
		IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);

		Range rangeX = xAxis.getRange();
		Range rangeY = yAxis.getRange();
		IPlotArea plotArea = baseChart.getPlotArea();
		Point rectangle = plotArea instanceof Scrollable scrollable ? scrollable.getSize() : plotArea.getSize();

		printVerticalLine(gc, rectangle, rangeX, 1.0, Colors.RED, 2);
		printVerticalLine(gc, rectangle, rangeX, 0.0, Colors.RED, 2);
		printVerticalLine(gc, rectangle, rangeX, -1.0, Colors.RED, 2);
		printHorizontalLine(gc, rectangle, rangeY, 1.3, Colors.RED, 2);
		gc.setLineStyle(lineStyle);
		gc.setLineWidth(lineWidth);
		gc.setForeground(lineColor);

	}

	private int printVerticalLine(GC gc, Point rectangle, Range rangeX, double position, Color color, int lineWidth) {

		int x = INVISIBLE;
		x = calculatePositionX(rangeX, rectangle, position);
		gc.setAlpha(255);
		gc.setForeground(color);
		gc.setLineWidth(lineWidth);
		gc.setLineStyle(SWT.LINE_DASHDOT);
		gc.drawLine(x, 0, x, rectangle.y);
		return x;
	}

	private int printHorizontalLine(GC gc, Point rectangle, Range rangeY, double position, Color color, int lineWidth) {

		int y = calculatePositionY(rangeY, rectangle, position);
		gc.setAlpha(255);
		gc.setForeground(color);
		gc.setLineWidth(lineWidth);
		gc.setLineStyle(SWT.LINE_DASHDOT);
		gc.drawLine(0, y, rectangle.x, y);
		return y;

	}

	private int calculatePositionX(Range range, Point point, double x) {

		int position = 0;
		double deltaRange = range.upper - range.lower;
		if(deltaRange != 0) {
			double partSize = point.x / deltaRange;
			double deltaX = x - range.lower;
			position = (int)(deltaX * partSize);
		}
		return position;
	}

	private int calculatePositionY(Range range, Point point, double y) {

		int position = 0;
		double deltaRange = range.upper - range.lower;
		if(deltaRange != 0) {
			double partSize = point.y / deltaRange;
			double deltaY = range.upper - y;
			position = (int)(deltaY * partSize);
		}
		return position;
	}

}
