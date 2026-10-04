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
package org.eclipse.chemclipse.xxd.edit.supplier.asls.core;

import java.util.Arrays;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.ops.DConvertMatrixStruct;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.CommonOps_DSCC;
import org.ejml.sparse.csc.factory.LinearSolverFactory_DSCC;

/**
 * Implements baseline correction by asymmetric least squares (AsLS) as described in:
 *
 * P.H. Eilers and H. F. M. Boelens. Baseline Correction with Asymmetric Least Squares Smoothing.
 * Leiden University Centre Medical Report 1(1) 2005 p. 5.
 */
public final class AsymmetricLeastSquares {

	private AsymmetricLeastSquares() {

	}

	/**
	 * @param p
	 *            asymmetry
	 * @param lambda
	 *            smoothness
	 */
	public static double[] baseline(double[] y, double p, double lambda, int iterations) {

		int n = y.length;

		DMatrixSparseCSC system = secondDifferencePenalty(n, lambda);
		int[] diagonal = diagonalIndexes(system);
		double[] penalty = new double[n];
		for(int i = 0; i < n; i++) {
			penalty[i] = system.nz_values[diagonal[i]];
		}

		double[] weights = new double[n];
		Arrays.fill(weights, 1.0);
		DMatrixRMaj rightHandSide = new DMatrixRMaj(n, 1);
		DMatrixRMaj z = new DMatrixRMaj(n, 1);
		LinearSolverSparse<DMatrixSparseCSC, DMatrixRMaj> solver = LinearSolverFactory_DSCC.cholesky(FillReducing.NONE);

		for(int iteration = 0; iteration < iterations; iteration++) {
			for(int i = 0; i < n; i++) {
				system.nz_values[diagonal[i]] = penalty[i] + weights[i];
				rightHandSide.data[i] = weights[i] * y[i];
			}
			if(!solver.setA(system)) {
				throw new ArithmeticException("Cholesky failed at iteration " + iteration + " (lambda=" + lambda + ")");
			}
			solver.setStructureLocked(true);
			solver.solve(rightHandSide, z);
			for(int i = 0; i < n; i++) {
				weights[i] = y[i] > z.data[i] ? p : 1.0 - p;
			}
		}
		return z.data;
	}

	private static DMatrixSparseCSC secondDifferencePenalty(int n, double lambda) {

		DMatrixSparseTriplet triplet = new DMatrixSparseTriplet(n - 2, n, 3 * (n - 2));
		for(int i = 0; i < n - 2; i++) {
			triplet.addItem(i, i, 1.0);
			triplet.addItem(i, i + 1, -2.0);
			triplet.addItem(i, i + 2, 1.0);
		}
		DMatrixSparseCSC d = DConvertMatrixStruct.convert(triplet, (DMatrixSparseCSC)null);
		DMatrixSparseCSC dTransposed = new DMatrixSparseCSC(n, n - 2, 3 * (n - 2));
		CommonOps_DSCC.transpose(d, dTransposed, null);
		DMatrixSparseCSC penalty = new DMatrixSparseCSC(n, n, 5 * n);
		CommonOps_DSCC.mult(dTransposed, d, penalty);
		CommonOps_DSCC.scale(lambda, penalty, penalty);
		penalty.sortIndices(null);
		return penalty;
	}

	private static int[] diagonalIndexes(DMatrixSparseCSC matrix) {

		int[] diagonal = new int[matrix.numCols];
		for(int column = 0; column < matrix.numCols; column++) {
			for(int k = matrix.col_idx[column]; k < matrix.col_idx[column + 1]; k++) {
				if(matrix.nz_rows[k] == column) {
					diagonal[column] = k;
					break;
				}
			}
		}
		return diagonal;
	}
}