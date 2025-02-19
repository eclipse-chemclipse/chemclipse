/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lorenz Gerber - initial API and implementation
 * Christoph Läubrich - fix pre-checks
 * Philip Wenig - refactoring
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.model;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.xxd.process.supplier.pca.exception.MathIllegalArgumentException;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

public abstract class AbstractMultivariateCalculator implements IMultivariateCalculator {

	static final int SEED = 10;
	private DMatrixRMaj loadings;
	private DMatrixRMaj scores;
	private DMatrixRMaj scoresPrediction;
	private double[] mean;
	private int numComps;
	private DMatrixRMaj sampleData;
	private DMatrixRMaj predictionData;
	private ArrayList<ISample> sampleKeys = new ArrayList<>();
	private ArrayList<ISample> sampleKeysPrediction = new ArrayList<>();
	private ArrayList<String> groupNames = new ArrayList<>();
	private ArrayList<String> groupNamesPrediction = new ArrayList<>();
	private ArrayList<String> classificationNames = new ArrayList<>();
	private ArrayList<String> classificationNamesPrediction = new ArrayList<>();
	private int sampleIndex;
	private int sampleIndexPrediction;
	private boolean computeSuccess;

	public AbstractMultivariateCalculator(int numSamples, int numVars, int numComponents, int numPredictionSamples) throws MathIllegalArgumentException {

		if(numComponents > numVars) {
			throw new MathIllegalArgumentException("Number of components must be smaller than number of variables.");
		}
		if(numVars <= 0) {
			throw new MathIllegalArgumentException("Number of variables must be larger than zero");
		}
		if(numSamples <= 0) {
			throw new MathIllegalArgumentException("Number of samples must be larger than zero.");
		}
		if(numComponents <= 0) {
			throw new MathIllegalArgumentException("Number of components must be larger than zero.");
		}
		sampleData = new DMatrixRMaj(numSamples, numVars);
		predictionData = new DMatrixRMaj(numPredictionSamples, numVars);
		this.mean = new double[numVars];
		sampleIndex = 0;
		sampleIndexPrediction = 0;
		this.numComps = numComponents;
		computeSuccess = false;
	}

	@Override
	public void setComputeSuccess() {

		computeSuccess = true;
	}

	@Override
	public boolean getComputeStatus() {

		return computeSuccess;
	}

	@Override
	public void addObservation(double[] obsData, ISample sampleKey, String groupName, String classificationName) {
		/*
		 * if(obsData.length < sampleData.getNumCols()) {
		 * this.invalidatePca();
		 * }
		 */

		for(int i = 0; i < obsData.length; i++) {
			sampleData.set(sampleIndex, i, obsData[i]);
		}
		sampleKeys.add(sampleKey);
		groupNames.add(groupName);
		classificationNames.add(classificationName);
		sampleIndex++;
	}

	@Override
	public void addPrediction(double[] obsData, ISample sampleKey, String groupName, String classificationName) {
		/*
		 * if(obsData.length < sampleData.getNumCols()) {
		 * this.invalidatePca();
		 * }
		 */

		for(int i = 0; i < obsData.length; i++) {
			predictionData.set(sampleIndexPrediction, i, obsData[i]);
		}
		sampleKeysPrediction.add(sampleKey);
		groupNamesPrediction.add(groupName);
		classificationNamesPrediction.add(classificationName);
		sampleIndexPrediction++;
	}

	public void setSampleData(DMatrixRMaj sampleData) {

		if(sampleData.getNumRows() != this.sampleData.getNumRows() || sampleData.getNumCols() != this.sampleData.getNumCols()) {
			throw new IllegalArgumentException("Invalid matrix dimensions");
		}
		this.sampleData = sampleData;
	}

	protected ArrayList<String> getGroupNames() {

		return groupNames;
	}

	protected ArrayList<String> getClassificationNames() {

		return classificationNames;
	}

	public DMatrixRMaj getScores() {

		return scores;
	}

	public DMatrixRMaj getScoresPreidction() {

		return scoresPrediction;
	}

	/**
	 * appplyLoadings
	 * 
	 * Observation(/Sample)-wise calculation of score vectors.
	 * 
	 * @param obs
	 *            one observation / sample
	 */
	private double[] applyLoadings(double[] obs) {

		DMatrixRMaj mean = DMatrixRMaj.wrap(sampleData.getNumCols(), 1, this.mean);
		DMatrixRMaj sample = new DMatrixRMaj(sampleData.getNumCols(), 1, true, obs);
		DMatrixRMaj rotated = new DMatrixRMaj(numComps, 1);
		CommonOps_DDRM.subtract(sample, mean, sample);
		CommonOps_DDRM.mult(loadings, sample, rotated);
		return rotated.data;
	}

	/**
	 * getErrorMetric
	 * 
	 * This is currently the implementation for DmodX.
	 * 
	 * @param obs
	 *            observation
	 */
	@Override
	public double getErrorMetric(double[] obs) {

		if(!getComputeStatus()) {
			return 0.0;
		}
		double[] eig = applyLoadings(obs);
		double[] reproj = reproject(eig);
		double total = 0;
		for(int i = 0; i < reproj.length; i++) {
			double d = obs[i] - reproj[i];
			total += d * d;
		}
		return Math.sqrt(total);
	}

	public DMatrixRMaj getLoadings() {

		return loadings;
	}

	/**
	 * getLoadingVector
	 * 
	 * Convenience accessor to extract the loading
	 * vector of a specific component.
	 * 
	 * @param component
	 *            component to extract the loading from
	 */
	@Override
	public double[] getLoadingVector(int component) {

		if(component <= 0 || component > numComps) {
			throw new IllegalArgumentException("Invalid component");
		}
		DMatrixRMaj loadingVector = new DMatrixRMaj(1, sampleData.numCols);
		CommonOps_DDRM.extract(loadings, component - 1, component, 0, sampleData.numCols, loadingVector, 0, 0);
		return loadingVector.data;
	}

	@Override
	public double getSummedVariance() {

		// calculate means of variables
		DMatrixRMaj colMeans = new DMatrixRMaj(1, sampleData.numCols);
		CommonOps_DDRM.sumCols(sampleData, colMeans);
		CommonOps_DDRM.divide(colMeans, sampleData.numRows);
		// subtract col means from col values and square them
		DMatrixRMaj varTemp = sampleData.copy();
		DMatrixRMaj colTemp = new DMatrixRMaj(varTemp.numRows, 1);
		for(int i = 0; i < varTemp.numCols; i++) {
			CommonOps_DDRM.extractColumn(varTemp, i, colTemp);
			CommonOps_DDRM.add(colTemp, colMeans.get(i) * -1);
			for(int j = 0; j < varTemp.numRows; j++) {
				varTemp.set(j, i, Math.pow(colTemp.get(j), 2));
			}
		}
		// sum along Columns and divide by 1-N
		DMatrixRMaj colSums = new DMatrixRMaj(1, sampleData.numCols);
		CommonOps_DDRM.sumCols(varTemp, colSums);
		CommonOps_DDRM.divide(colSums, (sampleData.numRows - 1));
		// sum all row variances
		return CommonOps_DDRM.elementSum(colSums);
	}

	@Override
	public double getExplainedVariance(int var) {

		DMatrixRMaj component = new DMatrixRMaj(sampleData.getNumRows(), 1);
		CommonOps_DDRM.extractColumn(getScores(), var, component);
		double colMean = CommonOps_DDRM.elementSum(component) / sampleData.getNumRows();
		CommonOps_DDRM.add(component, colMean * -1);
		for(int i = 0; i < component.numRows; i++) {
			component.set(i, 0, Math.pow(component.get(i), 2));
		}
		CommonOps_DDRM.divide(component, (sampleData.numRows - 1));
		return CommonOps_DDRM.elementSum(component) / 100;
	}

	protected double[] getMean() {

		return mean;
	}

	protected int getNumComps() {

		return numComps;
	}

	protected DMatrixRMaj getSampleData() {

		return sampleData;
	}

	protected DMatrixRMaj getPredictionData() {

		return predictionData;
	}

	@Override
	public double[] getScoreVector(ISample sampleId) {

		DMatrixRMaj scoreVector = new DMatrixRMaj(1, numComps);
		if(sampleId.isPredicted()) {
			int obs = sampleKeysPrediction.indexOf(sampleId);
			CommonOps_DDRM.extract(scoresPrediction, obs, obs + 1, 0, numComps, scoreVector, 0, 0);
		} else {
			int obs = sampleKeys.indexOf(sampleId);
			CommonOps_DDRM.extract(scores, obs, obs + 1, 0, numComps, scoreVector, 0, 0);
		}
		return scoreVector.data;
	}

	protected double[] reproject(double[] scoreVector) {

		DMatrixRMaj sample = new DMatrixRMaj(sampleData.getNumCols(), 1);
		DMatrixRMaj rotated = DMatrixRMaj.wrap(numComps, 1, scoreVector);
		CommonOps_DDRM.multTransA(loadings, rotated, sample);
		DMatrixRMaj mean = DMatrixRMaj.wrap(sampleData.getNumCols(), 1, this.mean);
		CommonOps_DDRM.add(sample, mean, sample);
		return sample.data;
	}

	protected void setLoadings(DMatrixRMaj loadings) {

		this.loadings = loadings;
	}

	protected void setScores(DMatrixRMaj scores) {

		this.scores = scores;
	}

	protected void setScoresPrediction(DMatrixRMaj scoresPrediction) {

		this.scoresPrediction = scoresPrediction;
	}

	public void replaceZeroColsWithSmallRandom() {

		DMatrixRMaj matrix = getSampleData();
		DMatrixRMaj colSums = CommonOps_DDRM.sumCols(matrix, null);
		DMatrixRMaj randCol = new DMatrixRMaj(matrix.numRows, 1);
		final Random rand = new Random(SEED);
		for(int i = 0; i < matrix.numRows; i++) {
			randCol.set(i, 0, rand.nextDouble(1.e-20, 1.e-19));
		}
		for(int i = 0; i < matrix.numCols; i++) {
			if(colSums.get(i) == 0 || Double.isNaN(colSums.get(i))) {
				CommonOps_DDRM.insert(randCol, matrix, 0, i);
			}
		}
	}

	@Override
	public void predict() {

		// fix zero/NaNs
		DMatrixRMaj predictions = getPredictionData();
		DMatrixRMaj colSums = CommonOps_DDRM.sumCols(predictions, null);
		DMatrixRMaj randCol = new DMatrixRMaj(predictions.numRows, 1);
		final Random rand = new Random(SEED);
		for(int i = 0; i < predictions.numRows; i++) {
			randCol.set(i, 0, rand.nextDouble(1.e-20, 1.e-19));
		}
		for(int i = 0; i < predictions.numCols; i++) {
			if(colSums.get(i) == 0 || Double.isNaN(colSums.get(i))) {
				CommonOps_DDRM.insert(randCol, predictions, 0, i);
			}
		}
		// prediction Score matrix
		setScoresPrediction(new DMatrixRMaj(predictions.numRows, getNumComps()));
		for(int i = 0; i < getNumComps(); i++) {
			DMatrixRMaj loading_vector = new DMatrixRMaj(getLoadingVector(i + 1));
			DMatrixRMaj predScore = CommonOps_DDRM.mult(predictions, loading_vector, null);
			CommonOps_DDRM.insert(predScore, getScoresPreidction(), 0, i);
		}
	}
}
