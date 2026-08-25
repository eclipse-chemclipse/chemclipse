/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - add a variant that uses double precision and does not modify the input, make access static
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.calculator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * SNIP: Statistics-sensitive Non-linear Iterative Peak-clipping
 * 
 * as described in
 * 
 * C.G. Ryan, E. Clayton, W.L. Griffin, S.H. Sie, D.R. Cousens
 * SNIP, a statistics-sensitive background treatment for the quantitative analysis of PIXE spectra in geoscience applications
 * http://dx.doi.org/10.1016/0168-583X(88)90063-8
 */
public class SnipCalculator {

	/**
	 * Calculates the intensity values.
	 */
	public static float[] calculateBaselineIntensityValues(float[] intensityValues, int iterations) {

		int size = intensityValues.length;
		float[] tmp = new float[size];
		for(int i = 1; i <= iterations; ++i) {
			for(int j = i; j < size - i; ++j) {
				float a = intensityValues[j];
				float b = (intensityValues[j - i] + intensityValues[j + i]) / 2;
				if(b < a) {
					a = b;
				}
				tmp[j] = a;
			}
			for(int j = i; j < size - i; ++j) {
				intensityValues[j] = tmp[j];
			}
		}
		return intensityValues;
	}

	/**
	 * Calculates the intensity values.
	 * 
	 * @param intensityValues
	 *            the intensity values to perform the baseline operation to
	 * @param iterations
	 *            the number of iterations
	 * @param monitor
	 *            the monitor to use for progress reporting (might be null)
	 */
	public static double[] calculateBaselineIntensityValues(double[] intensityValues, int iterations, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, iterations);
		double[] result = intensityValues.clone();
		int size = result.length;
		double[] tmp = new double[size];
		for(int i = 1; i <= iterations; ++i) {
			for(int j = i; j < size - i; ++j) {
				double a = result[j];
				double b = (result[j - i] + result[j + i]) / 2;
				if(b < a) {
					a = b;
				}
				tmp[j] = a;
			}
			for(int j = i; j < size - i; ++j) {
				result[j] = tmp[j];
			}
			subMonitor.worked(1);
		}
		return result;
	}
}
