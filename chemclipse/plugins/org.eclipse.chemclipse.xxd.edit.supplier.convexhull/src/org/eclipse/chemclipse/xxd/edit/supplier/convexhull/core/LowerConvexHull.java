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
package org.eclipse.chemclipse.xxd.edit.supplier.convexhull.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;

public final class LowerConvexHull {

	private LowerConvexHull() {

	}

	public static double[] baseline(double[] x, double[] y, double tolerance) {

		int n = x.length;
		if(n <= 2) {
			return y.clone();
		}
		List<Vector2D> points = new ArrayList<>(n);
		for(int i = 0; i < n; i++) {
			points.add(new Vector2D(x[i], y[i]));
		}
		ConvexHull2D hull = new MonotoneChain(false, tolerance).generate(points);
		List<Vector2D> chain = lowerChain(hull.getVertices());
		if(chain.size() < 2) {
			return chord(x, y);
		}
		double[] knotX = new double[chain.size()];
		double[] knotY = new double[chain.size()];
		for(int i = 0; i < chain.size(); i++) {
			knotX[i] = chain.get(i).getX();
			knotY[i] = chain.get(i).getY();
		}
		PolynomialSplineFunction baseline = new LinearInterpolator().interpolate(knotX, knotY);
		double[] output = new double[n];
		for(int i = 0; i < n; i++) {
			output[i] = baseline.value(x[i]);
		}
		output[0] = y[0];
		output[n - 1] = y[n - 1];
		return output;
	}

	/**
	 * Selects the vertices of the lower chain and returns them ordered by abscissa.
	 */
	private static List<Vector2D> lowerChain(Vector2D[] vertices) {

		Vector2D left = vertices[0];
		Vector2D right = vertices[0];
		for(Vector2D v : vertices) {
			if(v.getX() < left.getX()) {
				left = v;
			}
			if(v.getX() > right.getX()) {
				right = v;
			}
		}
		double dx = right.getX() - left.getX();
		double dy = right.getY() - left.getY();
		List<Vector2D> chain = new ArrayList<>(vertices.length);
		for(Vector2D v : vertices) {
			double side = dx * (v.getY() - left.getY()) - dy * (v.getX() - left.getX());
			if(side <= 0.0) {
				chain.add(v);
			}
		}
		chain.sort(Comparator.comparingDouble(Vector2D::getX));
		return chain;
	}

	private static double[] chord(double[] x, double[] y) {

		int n = x.length;
		double slope = (y[n - 1] - y[0]) / (x[n - 1] - x[0]);
		double[] output = new double[n];
		for(int i = 0; i < n; i++) {
			output[i] = y[0] + slope * (x[i] - x[0]);
		}
		output[n - 1] = y[n - 1];
		return output;
	}
}