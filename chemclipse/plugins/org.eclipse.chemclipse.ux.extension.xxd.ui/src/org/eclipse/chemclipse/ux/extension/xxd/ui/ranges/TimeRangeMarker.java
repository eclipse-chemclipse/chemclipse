/*******************************************************************************
 * Copyright (c) 2019, 2020 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.ranges;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;
import org.eclipse.swtchart.extensions.marker.IBaseChartPaintListener;

public class TimeRangeMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	private static final int INVISIBLE = -1;
	//
	private Set<TimeRange> timeRanges = new HashSet<>();
	private Transform transform = new Transform(Display.getDefault());

	public TimeRangeMarker(BaseChart baseChart) {
		super(baseChart);
		transform.rotate(-90);
	}

	public Set<TimeRange> getTimeRanges() {

		return timeRanges;
	}

	@Override
	public void paintControl(PaintEvent e) {

		GC gc = e.gc;
		for(TimeRange timeRange : timeRanges) {
			plotMarker(gc, timeRange);
		}
		gc.setAlpha(255);
	}

	@Override
	protected void finalize() throws Throwable {

		transform.dispose();
		super.finalize();
	}

	private void plotMarker(GC gc, TimeRange timeRange) {

		BaseChart baseChart = getBaseChart();
		if(baseChart.getSeriesSet().getSeries().length > 0) {
			IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			IAxis yAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			//
			if(xAxis != null && yAxis != null) {
				/*
				 * Settings
				 */
				Range rangeX = xAxis.getRange();
				IPlotArea plotArea = baseChart.getPlotArea();
				Point rectangle = (plotArea instanceof Scrollable) ? ((Scrollable)plotArea).getSize() : plotArea.getSize();
				/*
				 * Print lines, rectangle and label
				 */
				int xStart = printLine(gc, rectangle, rangeX, timeRange.getStart(), Colors.DARK_GRAY, 1);
				printLine(gc, rectangle, rangeX, timeRange.getCenter(), Colors.GRAY, 1);
				int xStop = printLine(gc, rectangle, rangeX, timeRange.getStop(), Colors.DARK_GRAY, 1);
				fillRectangle(gc, rectangle, xStart, xStop, Colors.GRAY);
				printLabel(gc, rectangle, timeRange.getIdentifier(), xStop, Colors.DARK_GRAY);
			}
		}
	}

	private int printLine(GC gc, Point rectangle, Range rangeX, int position, Color color, int lineWidth) {

		int x = INVISIBLE;
		if(isMarkerInRange(rangeX, position)) {
			x = calculatePositionX(rangeX, rectangle, position);
			gc.setAlpha(255);
			gc.setForeground(color);
			gc.setLineWidth(lineWidth);
			gc.drawLine(x, 0, x, rectangle.y);
		}
		return x;
	}

	private void fillRectangle(GC gc, Point rectangle, int xStart, int xStop, Color color) {

		if(xStart > INVISIBLE || xStop > INVISIBLE) {
			int start = (xStart == INVISIBLE) ? 0 : xStart;
			int stop = (xStop == INVISIBLE) ? rectangle.x : xStop;
			gc.setBackground(color);
			gc.setAlpha(50);
			gc.fillRectangle(start, 0, stop - start, rectangle.y);
		}
	}

	private void printLabel(GC gc, Point rectangle, String label, int xStop, Color color) {

		if(xStop > INVISIBLE) {
			gc.setTransform(transform);
			Point labelSize = gc.textExtent(label);
			int xLabel = -labelSize.x - (rectangle.y - labelSize.x) + (rectangle.y / 20);
			/*
			 * Use this position to set the label before the start line:
			 * int yLabel = xStart - labelSize.y;
			 */
			int yLabel = xStop;
			gc.setAlpha(255);
			gc.setForeground(color);
			gc.setLineWidth(1);
			gc.drawText(label, xLabel, yLabel, true);
			gc.setTransform(null);
		}
	}

	private boolean isMarkerInRange(Range range, int x) {

		return x >= range.lower && x <= range.upper;
	}

	private int calculatePositionX(Range range, Point point, int x) {

		int position = 0;
		double deltaRange = range.upper - range.lower + 1;
		if(deltaRange != 0) {
			double partSize = point.x / deltaRange;
			double deltaX = x - range.lower;
			position = (int)(deltaX * partSize);
		}
		return position;
	}
}
