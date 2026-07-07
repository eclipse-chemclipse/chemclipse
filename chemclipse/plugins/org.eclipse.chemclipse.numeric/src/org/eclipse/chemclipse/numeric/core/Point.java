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
package org.eclipse.chemclipse.numeric.core;

public class Point implements IPoint {

	private double x;
	private double y;

	public Point(double x, double y) {

		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {

		return x;
	}

	@Override
	public void setX(double x) {

		this.x = x;
	}

	@Override
	public double getY() {

		return y;
	}

	@Override
	public void setY(double y) {

		this.y = y;
	}

	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		IPoint other = (IPoint)otherObject;
		return getX() == other.getX() && getY() == other.getY();
	}

	@Override
	public int hashCode() {

		return Double.hashCode(x) + Double.hashCode(y);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("x=" + x);
		builder.append(",");
		builder.append("y=" + y);
		builder.append("]");
		return builder.toString();
	}
}
