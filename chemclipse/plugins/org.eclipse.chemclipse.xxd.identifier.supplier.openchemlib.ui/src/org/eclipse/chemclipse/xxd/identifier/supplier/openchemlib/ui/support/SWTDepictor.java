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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.identifier.supplier.openchemlib.ui.support;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;

import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.gui.generic.GenericPolygon;

/**
 * Draws an OpenChemLib molecule on an SWT graphics context, without AWT or
 * JavaFX rendering. Call {@link #dispose()} when done to release the fonts.
 */
public class SWTDepictor extends AbstractDepictor<GC> {

	private final Map<Integer, Font> fonts = new HashMap<>();
	private int textSize = 12;
	private double lineWidth = 1.0d;

	public SWTDepictor(StereoMolecule molecule, int displayMode) {

		super(molecule, displayMode);
	}

	public void dispose() {

		for(Font font : fonts.values()) {
			font.dispose();
		}
		fonts.clear();
	}

	@Override
	protected void drawBlackLine(DepictorLine line) {

		drawLine(line);
	}

	@Override
	protected void drawDottedLine(DepictorLine line) {

		float width = (float)lineWidth;
		LineAttributes previous = mContext.getLineAttributes();
		mContext.setLineAttributes(new LineAttributes(width, SWT.CAP_ROUND, SWT.JOIN_ROUND, SWT.LINE_CUSTOM, new float[]{3.0f * width}, 0, 10));
		drawLine(line);
		mContext.setLineAttributes(previous);
	}

	private void drawLine(DepictorLine line) {

		Path path = new Path(mContext.getDevice());
		try {
			path.moveTo((float)line.x1, (float)line.y1);
			path.lineTo((float)line.x2, (float)line.y2);
			mContext.drawPath(path);
		} finally {
			path.dispose();
		}
	}

	@Override
	protected void drawPolygon(GenericPolygon polygon) {

		if(polygon.getSize() == 0) {
			return;
		}

		Path path = new Path(mContext.getDevice());
		try {
			path.moveTo((float)polygon.getX(0), (float)polygon.getY(0));
			for(int i = 1; i < polygon.getSize(); i++) {
				path.lineTo((float)polygon.getX(i), (float)polygon.getY(i));
			}
			path.close();
			mContext.fillPath(path);
			mContext.drawPath(path);
		} finally {
			path.dispose();
		}
	}

	/*
	 * x is the horizontal center, y the vertical center of the label.
	 * The baseline is placed like in Depictor2D at y + textSize / 3.
	 */
	@Override
	protected void drawString(String text, double x, double y) {

		FontMetrics fontMetrics = mContext.getFontMetrics();
		double baseline = y + textSize / 3.0d;
		double top = baseline - fontMetrics.getAscent() - fontMetrics.getLeading();
		double left = x - getStringWidth(text) / 2.0d;
		mContext.drawString(text, (int)Math.round(left), (int)Math.round(top), true);
	}

	@Override
	protected void fillCircle(double x, double y, double diameter) {

		Path path = new Path(mContext.getDevice());
		try {
			path.addArc((float)x, (float)y, (float)diameter, (float)diameter, 0, 360);
			mContext.fillPath(path);
		} finally {
			path.dispose();
		}
	}

	@Override
	protected double getStringWidth(String text) {

		if(mContext == null) {
			return 0;
		}

		return mContext.stringExtent(text).x;
	}

	@Override
	protected int getTextSize() {

		return textSize;
	}

	/*
	 * OpenChemLib passes the label height in pixels, SWT fonts are sized in points.
	 */
	@Override
	protected void setTextSize(int size) {

		textSize = size;
		if(mContext != null && size > 0) {
			mContext.setFont(fonts.computeIfAbsent(size, this::createFont));
		}
	}

	private Font createFont(int pixels) {

		Device device = mContext.getDevice();
		FontData fontData = device.getSystemFont().getFontData()[0];
		float points = pixels * 72.0f / device.getDPI().y;
		return new Font(device, new FontData(fontData.getName(), Math.max(1, Math.round(points)), SWT.NORMAL));
	}

	@Override
	protected double getLineWidth() {

		return lineWidth;
	}

	@Override
	protected void setLineWidth(double width) {

		lineWidth = width;
		mContext.setLineAttributes(new LineAttributes((float)width, SWT.CAP_ROUND, SWT.JOIN_ROUND));
	}

	/*
	 * Lines and text use the foreground, filled shapes the background.
	 */
	@Override
	protected void setRGB(int rgb) {

		Color color = new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
		mContext.setForeground(color);
		mContext.setBackground(color);
	}
}
