/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.numeric.geometry;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.equations.Equations;

/**
 * Calculates the slope between two points.
 */
public class Slope implements ISlope {

	private double slope;

	public Slope(IPoint p1, IPoint p2) {

		slope = Equations.calculateSlope(p1, p2);
	}

	@Override
	public double getSlope() {

		return slope;
	}

	@Override
	public void setSlope(double slope) {

		this.slope = slope;
	}

	// --------------equals, hashCode, toString
	@Override
	public boolean equals(Object other) {

		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		if(getClass() != other.getClass()) {
			return false;
		}
		Slope otherSlope = (Slope)other;
		return getSlope() == otherSlope.getSlope();
	}

	@Override
	public int hashCode() {

		return 7 * Double.hashCode(slope);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("slope=" + slope);
		builder.append("]");
		return builder.toString();
	}
	// --------------equals, hashCode, toString
}
