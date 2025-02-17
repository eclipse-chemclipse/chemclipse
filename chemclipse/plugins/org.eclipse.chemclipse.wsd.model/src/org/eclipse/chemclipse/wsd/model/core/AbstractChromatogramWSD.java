/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - update to reflect changes in {@link INoiseCalculator} API
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.baseline.BaselineModel;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.wsd.model.core.selection.ChromatogramSelectionWSD;

public abstract class AbstractChromatogramWSD extends AbstractChromatogram implements IChromatogramWSD {

	private static final long serialVersionUID = -7048942996283330150L;

	private final Map<Double, IBaselineModel> baselineModels;

	protected AbstractChromatogramWSD() {

		baselineModels = new HashMap<>();
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
	public Set<Float> getWavelengths() {

		Set<Float> wavelengths = new HashSet<>();
		for(int i = 1; i < getNumberOfScans(); i++) {
			getSupplierScan(i).getScanSignals().forEach(signal -> wavelengths.add(signal.getWavelength()));
		}
		return wavelengths;
	}

	@Override
	public IScanWSD getSupplierScan(int scan) {

		int position = scan;
		if(position > 0 && position <= getScans().size()) {
			IScan storedScan = getScans().get(--position);
			if(storedScan instanceof IScanWSD scanWSD) {
				return scanWSD;
			}
		}
		return null;
	}

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

		/*
		 * Fire an update to inform all listeners.
		 */
		if(chromatogramSelection instanceof ChromatogramSelectionWSD chromatogramSelectionWSD) {
			chromatogramSelectionWSD.update(true);
		}
	}

	@Override
	public IBaselineModel getBaselineModel(double wavelength) {

		baselineModels.putIfAbsent(wavelength, new BaselineModel(this, Float.NaN));
		return baselineModels.get(wavelength);
	}

	@Override
	public void removeBaselineModel(double wavelength) {

		baselineModels.remove(wavelength);
	}

	@Override
	public Map<Double, IBaselineModel> getBaselineModels() {

		return Collections.unmodifiableMap(baselineModels);
	}

	@Override
	public boolean containsBaseline(double wavelength) {

		return baselineModels.containsKey(wavelength);
	}

	@Override
	public <ResultType extends IMeasurementResult<?>> ResultType getMeasurementResult(Class<ResultType> type) {

		ResultType result = super.getMeasurementResult(type);
		if(result == null && type == ChromatogramSegmentation.class) {
			return type.cast(new ChromatogramSegmentation(this, PreferenceSupplier.getSelectedSegmentWidth()));
		}
		return result;
	}

	@Override
	public void addMeasurementResult(IMeasurementResult<?> chromatogramResult) {

		super.addMeasurementResult(chromatogramResult);
		if(chromatogramResult instanceof NoiseSegmentMeasurementResult) {
			recalculateTheNoiseFactor();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<IChromatogramPeakWSD> getPeaks() {

		return (List<IChromatogramPeakWSD>)super.getPeaks();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IChromatogramPeakWSD> getPeaks(int startRetentionTime, int stopRetentionTime) {

		return (List<IChromatogramPeakWSD>)super.getPeaks(startRetentionTime, stopRetentionTime);
	}
}
