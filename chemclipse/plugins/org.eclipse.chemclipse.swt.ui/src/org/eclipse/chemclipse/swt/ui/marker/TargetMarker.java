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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.marker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;

public class TargetMarker extends AbstractBaseChartPaintListener {

	private static final int SIZE = 8;
	private static final int Y_OFFSET = 6;
	private static final int T_STEM_HEIGHT = 5;
	private static final int T_LINE_WIDTH = 2;
	private static final int LABEL_GAP = 2;
	private static final int MARKER_Y = Y_OFFSET + T_LINE_WIDTH / 2 + T_STEM_HEIGHT + LABEL_GAP;

	private record PeakEntry(int retentionTime, float peakMaximum, boolean identified) {
	}

	private record ScanEntry(int retentionTime, float signal) {
	}

	private List<PeakEntry> peakEntries = new ArrayList<>();
	private List<ScanEntry> scanEntries = new ArrayList<>();

	public TargetMarker(BaseChart baseChart) {

		super(baseChart);
	}

	public void clear() {

		peakEntries.clear();
		scanEntries.clear();
		super.setDraw(false);
	}

	public void addPeak(int retentionTime, float peakMaximum, boolean identified) {

		peakEntries.add(new PeakEntry(retentionTime, peakMaximum, identified));
	}

	public void addScan(int retentionTime, float signal) {

		scanEntries.add(new ScanEntry(retentionTime, signal));
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(isDraw()) {
			BaseChart baseChart = getBaseChart();
			IAxis axisX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			IAxis axisY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
			if(axisX != null && axisY != null) {
				Range rangeX = axisX.getRange();
				Range rangeY = axisY.getRange();
				double xMin = rangeX.lower;
				double xMax = rangeX.upper;
				double widthX = xMax - xMin + 1;
				int width = e.width;
				GC gc = e.gc;
				Color colorBackground = gc.getBackground();
				Color colorForeground = gc.getForeground();
				int lineWidth = gc.getLineWidth();
				int lineStyle = gc.getLineStyle();
				gc.setBackground(Colors.DARK_GRAY);
				gc.setForeground(Colors.DARK_GRAY);
				gc.setLineWidth(T_LINE_WIDTH);
				gc.setLineStyle(SWT.LINE_SOLID);
				for(PeakEntry entry : peakEntries) {
					int retentionTime = entry.retentionTime();
					if(retentionTime >= xMin && retentionTime <= xMax) {
						if(entry.peakMaximum() >= rangeY.lower && entry.peakMaximum() <= rangeY.upper) {
							continue;
						}
						double percent = 1.0d / widthX * (retentionTime - xMin);
						int x = (int)(width * percent);
						if(x > 0) {
							if(entry.identified()) {
								drawTSymbol(gc, x);
							}
							drawInvertedTriangle(gc, x);
						}
					}
				}
				for(ScanEntry entry : scanEntries) {
					int retentionTime = entry.retentionTime();
					if(retentionTime >= xMin && retentionTime <= xMax) {
						if(entry.signal() >= rangeY.lower && entry.signal() <= rangeY.upper) {
							continue;
						}
						double percent = 1.0d / widthX * (retentionTime - xMin);
						int x = (int)(width * percent);
						if(x > 0) {
							drawTSymbol(gc, x);
							drawCircle(gc, x);
						}
					}
				}
				gc.setLineWidth(lineWidth);
				gc.setLineStyle(lineStyle);
				gc.setBackground(colorBackground);
				gc.setForeground(colorForeground);
			}
		}
	}

	private void drawTSymbol(GC gc, int x) {

		gc.drawLine(x - SIZE / 2, Y_OFFSET, x + SIZE / 2, Y_OFFSET);
		gc.drawLine(x, Y_OFFSET, x, Y_OFFSET + T_STEM_HEIGHT);
	}

	private void drawInvertedTriangle(GC gc, int x) {

		int[] triangle = { //
				x - SIZE / 2, MARKER_Y, //
				x + SIZE / 2, MARKER_Y, //
				x, MARKER_Y + SIZE //
		};
		gc.fillPolygon(triangle);
	}

	private void drawCircle(GC gc, int x) {

		gc.fillOval(x - SIZE / 2, MARKER_Y, SIZE, SIZE);
	}
}