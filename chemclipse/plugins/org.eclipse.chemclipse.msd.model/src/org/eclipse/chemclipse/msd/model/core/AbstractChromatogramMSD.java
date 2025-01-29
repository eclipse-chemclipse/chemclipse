/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 * Christoph Läubrich - adjust to new {@link INoiseCalculator} API
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.updates.IChromatogramUpdateListener;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.implementation.ImmutableZeroIon;
import org.eclipse.chemclipse.msd.model.implementation.IonTransitionSettings;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The abstract chromatogram is responsible to handle as much jobs concerning a
 * chromatogram independent of the specific supplier.<br/>
 * AbstractChromatogram extends ({@link IChromatogramMSD}) which implements (
 * {@link IChromatogramOverview}). ({@link IChromatogramOverview}) should enable
 * accessing some values of a chromatogram or a short overview. Some values like
 * amount of scans, min/max signal, min/max retention time and total ion
 * chromatogram signals, without accessing all scans. This should be more faster
 * than parsing all scans if they are not needed. On the other hand,
 * AbstractChromatogram implements ({@link IChromatogramMSD}) which itself
 * extends ({@link IChromatogramOverview}). Why? When working with an
 * IChromatogram instance all the values like min/max signal, min/max retention
 * time should be accessible with out implementing them twice.<br/>
 * But now IChromatogramOverview can be used. It is less confusing to use only
 * those method which are needed for an overview than to select from all the
 * IChromatogram methods.<br/>
 * For instance, a value could be stored for minSignal in an instance of the
 * extended AbstractChromatogram. If no scans are added to the chromatogram,
 * minSignal as stored will be returned, otherwise minSignal will be calculated.
 * <br/>
 * <br/>
 * IUpdater is implemented which takes care that all registered listeners (
 * {@link IChromatogramUpdateListener}) will be informed if values of the
 * chromatogram has been changed.
 */
public abstract class AbstractChromatogramMSD extends AbstractChromatogram<IChromatogramPeakMSD> implements IChromatogramMSD {

	private static final long serialVersionUID = 6481555040060687481L;
	//
	public static final int DEFAULT_SEGMENT_WIDTH = 10;
	//
	private final IIonTransitionSettings ionTransitionSettings;
	private ImmutableZeroIon immutableZeroIon;
	private IScanMSD combinedMassSpectrum;

	protected AbstractChromatogramMSD() {

		ionTransitionSettings = new IonTransitionSettings();
		immutableZeroIon = new ImmutableZeroIon();
		updateNoiseFactor();
	}

	@Override
	public void updateNoiseFactor() {

		String noiseCalculatorId = getNoiseCalculatorId();
		INoiseCalculator noiseCalculator = NoiseCalculator.getNoiseCalculator(noiseCalculatorId);
		if(noiseCalculator != null) {
			noiseCalculator.reset();
		}
		//
		setNoiseCalculator(noiseCalculator);
	}

	@Override
	public <ResultType extends IMeasurementResult<?>> ResultType getMeasurementResult(Class<ResultType> type) {

		ResultType result = super.getMeasurementResult(type);
		if(result == null && type == ChromatogramSegmentation.class) {
			return type.cast(new ChromatogramSegmentation(this, PreferenceSupplier.getSelectedSegmentWidth()));
		}
		//
		return result;
	}

	@Override
	public void addMeasurementResult(IMeasurementResult<?> chromatogramResult) {

		super.addMeasurementResult(chromatogramResult);
		if(chromatogramResult instanceof NoiseSegmentMeasurementResult) {
			recalculateTheNoiseFactor();
		}
	}

	@Override
	public int getNumberOfScanIons() {

		int amount = 0;
		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				amount += scanMSD.getNumberOfIons();
			}
		}
		return amount;
	}

	@Override
	public void enforceLoadScanProxies(IProgressMonitor monitor) {

		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD && !isUnloaded()) {
				scanMSD.enforceLoadScanProxy();
			}
		}
	}

	@Override
	public void fireUpdate(IChromatogramSelection<?, ?> chromatogramSelection) {

		/*
		 * Fire an update to inform all listeners.
		 */
		if(chromatogramSelection instanceof ChromatogramSelectionMSD chromatogramSelectionMSD) {
			chromatogramSelectionMSD.update(true);
		}
	}

	@Override
	public IScanMSD getScan(int scan, IMarkedIons excludedIons) {

		IRegularMassSpectrum supplierMassSpectrum = getSupplierScan(scan);
		if(supplierMassSpectrum == null) {
			return null;
		}
		return supplierMassSpectrum.getMassSpectrum(excludedIons);
	}

	@Override
	public IRegularMassSpectrum getSupplierScan(int scan) {

		int position = scan;
		if(position > 0 && position <= getScans().size()) {
			IScan storedScan = getScans().get(--position);
			if(storedScan instanceof IRegularMassSpectrum regularMassSpectrum) {
				return regularMassSpectrum;
			}
		}
		return null;
	}

	@Override
	public float getMinIonAbundance() {

		IIon ion;
		float minAbundance = Float.MAX_VALUE;
		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				ion = scanMSD.getLowestAbundance();
				if(!isZeroImmutableIon(ion)) {
					if(ion.getAbundance() < minAbundance) {
						minAbundance = ion.getAbundance();
					}
				}
			}
		}
		return minAbundance;
	}

	@Override
	public float getMaxIonAbundance() {

		IIon ion;
		float maxAbundance = Float.MIN_VALUE;
		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				ion = scanMSD.getHighestAbundance();
				if(!isZeroImmutableIon(ion)) {
					if(ion.getAbundance() > maxAbundance) {
						maxAbundance = ion.getAbundance();
					}
				}
			}
		}
		return maxAbundance;
	}

	@Override
	public double getStartIon() {

		/*
		 * Return 0 if no scan is stored.
		 */
		if(getScans().isEmpty()) {
			return 0;
		}
		double lowestIon = Double.MAX_VALUE;
		double actualIon;
		/*
		 * Check all scans.
		 */
		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				IIon ion = scanMSD.getLowestIon();
				if(!isZeroImmutableIon(ion)) {
					actualIon = ion.getIon();
					if(actualIon < lowestIon) {
						lowestIon = actualIon;
					}
				}
			}
		}
		return lowestIon;
	}

	@Override
	public double getStopIon() {

		/*
		 * Return 0 if no scan is stored.
		 */
		if(getScans().isEmpty()) {
			return 0;
		}
		double highestIon = Double.MIN_VALUE;
		double actualIon;
		/*
		 * Check all scans.
		 */
		for(IScan scan : getScans()) {
			if(scan instanceof IScanMSD scanMSD) {
				IIon ion = scanMSD.getHighestIon();
				if(!isZeroImmutableIon(ion)) {
					actualIon = ion.getIon();
					if(actualIon > highestIon) {
						highestIon = actualIon;
					}
				}
			}
		}
		return highestIon;
	}

	@Override
	public IIonTransitionSettings getIonTransitionSettings() {

		return ionTransitionSettings;
	}

	@Override
	public IScanMSD getCombinedMassSpectrum() {

		return combinedMassSpectrum;
	}

	@Override
	public void setCombinedMassSpectrum(IScanMSD combinedMassSpectrum) {

		this.combinedMassSpectrum = combinedMassSpectrum;
	}

	private boolean isZeroImmutableIon(IIon ion) {

		if(immutableZeroIon.equals(ion)) {
			return true;
		} else {
			return false;
		}
	}

	private String getNoiseCalculatorId() {

		NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = getMeasurementResult(NoiseSegmentMeasurementResult.class);
		if(noiseSegmentMeasurementResult != null) {
			return noiseSegmentMeasurementResult.getNoiseCalculatorId();
		} else {
			return PreferenceSupplier.getSelectedNoiseCalculatorId();
		}
	}
}
