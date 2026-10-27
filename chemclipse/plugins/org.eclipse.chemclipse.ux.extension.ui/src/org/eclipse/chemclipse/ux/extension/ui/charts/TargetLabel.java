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
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.charts;

import org.eclipse.swt.graphics.Font;

public class TargetLabel {

	// constant values
	private final String label;
	private final String id;
	private final boolean isActive;
	private double x;
	private double y;
	private final Font font;
	/*
	 * Cached values used for calculation
	 */
	private LabelBounds bounds;

	public TargetLabel(String label, String id, Font font, boolean isActive, double x, double y) {

		this.label = label;
		this.id = id;
		this.font = font;
		this.isActive = isActive;
		this.x = x;
		this.y = y;
	}

	public String getLabel() {

		return label;
	}

	public String getId() {

		return id;
	}

	public boolean isActive() {

		return isActive;
	}

	public double getX() {

		return x;
	}

	/**
	 * Adjust the y value.
	 * Handle this method carefully.
	 * 
	 * @param x
	 */
	public void adjustX(double x) {

		this.x = x;
	}

	public double getY() {

		return y;
	}

	/**
	 * Adjust the y value.
	 * Handle this method carefully.
	 * 
	 * @param y
	 */
	public void adjustY(double y) {

		this.y = y;
	}

	public Font getFont() {

		return font;
	}

	public LabelBounds getBounds() {

		return bounds;
	}

	public void setBounds(LabelBounds bounds) {

		this.bounds = bounds;
	}
}