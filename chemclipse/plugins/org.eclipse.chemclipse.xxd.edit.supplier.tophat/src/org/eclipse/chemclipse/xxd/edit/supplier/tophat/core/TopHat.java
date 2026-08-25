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
package org.eclipse.chemclipse.xxd.edit.supplier.tophat.core;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.MathArrays;

/**
 * Implements the top-hat transformation for baseline correctedion as described in:
 *
 * Pérez-Pueyo, R., Soneira, M. J., &amp; Ruiz-Moreno, S. (2010).
 * Morphology-Based Automated Baseline Removal for Raman Spectra of Artistic Pigments.
 * Applied Spectroscopy, 64(6), 595-600. https://doi.org/10.1366/000370210791414281
 */
public final class TopHat {

	public static final double DEFAULT_EPSILON = 1.0e-6d;

	private TopHat() {

	}

	/**
	 * Removes the baseline using the corrected top-hat transform:
	 *
	 * <pre>
	 * rho_opt(f) = f - gamma_opt(f)                                   (7)
	 * </pre>
	 *
	 * @param y
	 *            the signal
	 * @param halfWindowSize
	 *            half the width of the structuring element, in data points
	 * @return the baseline corrected signal
	 */
	public static double[] topHat(double[] y, int halfWindowSize) {

		return MathArrays.ebeSubtract(y, baseline(y, halfWindowSize));
	}

	/**
	 * Removes the baseline using the corrected top-hat transform, determining the
	 * size of the structuring element automatically.
	 *
	 * @param y
	 *            the signal
	 * @return the baseline corrected signal
	 */
	public static double[] topHat(double[] y) {

		return topHat(y, optimizeHalfWindowSize(y));
	}

	/**
	 * Estimates the baseline using a predetermined half-window size:
	 *
	 * <pre>
	 * gamma_opt(f) = min[gamma'(f), gamma(f)]                         (6)
	 * </pre>
	 *
	 * @param y
	 *            the signal
	 * @param halfWindowSize
	 *            half the width of the structuring element, in data points
	 * @return the estimated baseline
	 */
	public static double[] baseline(double[] y, int halfWindowSize) {

		double[] opening = opening(y, halfWindowSize);
		double[] corrected = correction(opening, halfWindowSize);
		double[] result = new double[y.length];
		for(int i = 0; i < y.length; i++) {
			result[i] = Math.min(corrected[i], opening[i]);
		}
		return result;
	}

	/**
	 * <pre>
	 * gamma_Y(f) = dilation_Y[erosion_Y(f)]                           (3)
	 * </pre>
	 */
	private static double[] opening(double[] y, int halfWindowSize) {

		return dilation(erosion(y, halfWindowSize), halfWindowSize);
	}

	/**
	 * The correction of the opening:
	 *
	 * <pre>
	 * gamma'(f) = (dilation[gamma_Y(f)] + erosion[gamma_Y(f)]) / 2    (5)
	 * </pre>
	 *
	 * @param opening
	 *            the opening of the signal, not the signal
	 */
	private static double[] correction(double[] opening, int halfWindowSize) {

		double[] dilated = dilation(opening, halfWindowSize);
		double[] eroded = erosion(opening, halfWindowSize);
		return MathArrays.scale(0.5d, MathArrays.ebeAdd(dilated, eroded));
	}

	/**
	 * Determines the size of the structuring element automatically, using the
	 * default tolerance and no explicit upper limit.
	 *
	 * @param y
	 *            the signal
	 * @return half the width of the structuring element, in data points
	 */
	public static int optimizeHalfWindowSize(double[] y) {

		return optimizeHalfWindowSize(y, DEFAULT_EPSILON, y.length / 2);
	}

	/**
	 * Determines the size of the structuring element automatically.
	 *
	 * @param y
	 *            the signal
	 * @param epsilon
	 *            relative tolerance for the comparison of two openings
	 * @param maxHalfWindowSize
	 *            upper limit for the returned value; if the procedure does not
	 *            converge below this limit, the limit itself is returned
	 * @return half the width of the structuring element, in data points
	 */
	public static int optimizeHalfWindowSize(double[] y, double epsilon, int maxHalfWindowSize) {

		if(y.length < 3 || maxHalfWindowSize < 1) {
			return 1;
		}
		double tolerance = epsilon * span(y);
		int limit = Math.min(maxHalfWindowSize, y.length / 2);
		double[] previous = opening(y, 1);
		int runStart = 1;
		int runLength = 1;
		for(int halfWindowSize = 2; halfWindowSize <= limit; halfWindowSize++) {
			double[] current = opening(y, halfWindowSize);
			if(MathArrays.distanceInf(previous, current) <= tolerance) {
				runLength++;
				if(runLength == 3) {
					return runStart;
				}
			} else {
				runStart = halfWindowSize;
				runLength = 1;
			}
			previous = current;
		}
		return limit;
	}

	/**
	 * <pre>
	 * erosion_Y(f)(x) = min f(x + s), s in Y                          (1)
	 * </pre>
	 */
	private static double[] erosion(double[] y, int halfWindowSize) {

		return extreme(y, halfWindowSize, true);
	}

	/**
	 * <pre>
	 * dilation_Y(f)(x) = max f(x + s), s in Y                         (2)
	 * </pre>
	 */
	private static double[] dilation(double[] y, int halfWindowSize) {

		return extreme(y, halfWindowSize, false);
	}

	private static double[] extreme(double[] y, int halfWindowSize, boolean minimum) {

		int size = y.length;
		if(halfWindowSize < 1 || size == 0) {
			return y.clone();
		}
		int window = 2 * halfWindowSize + 1;
		int padded = size + 2 * halfWindowSize;
		double[] values = new double[padded];
		for(int i = 0; i < padded; i++) {
			values[i] = y[Math.clamp(i - halfWindowSize, 0, size - 1)];
		}
		double[] result = new double[size];
		int[] queue = new int[padded];
		int head = 0;
		int tail = 0;
		for(int i = 0; i < padded; i++) {
			while(tail > head && dominated(values[queue[tail - 1]], values[i], minimum)) {
				tail--;
			}
			queue[tail++] = i;
			if(queue[head] <= i - window) {
				head++;
			}
			if(i >= window - 1) {
				result[i - window + 1] = values[queue[head]];
			}
		}
		return result;
	}

	private static boolean dominated(double candidate, double value, boolean minimum) {

		return minimum ? candidate >= value : candidate <= value;
	}

	private static double span(double[] y) {

		double span = StatUtils.max(y) - StatUtils.min(y);
		return span > 0.0d ? span : 1.0d;
	}
}