/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - update to reflect changes in {@link INoiseCalculator} API
 *******************************************************************************/
package org.eclipse.chemclipse.csd.model.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise.NoiseCalculator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

public abstract class AbstractChromatogramCSD extends AbstractChromatogram implements IChromatogramCSD {

	private static final long serialVersionUID = -1514838958855146167L;

	@Override
	public IScanCSD getScan(int scan) {

		int position = scan;
		if(position > 0 && position <= getScans().size()) {
			IScan storedScan = getScans().get(--position);
			if(storedScan instanceof IScanCSD scanCSD) {
				return scanCSD;
			}
		}
		return null;
	}

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

		/*
		 * Fire an update to inform all listeners.
		 */
		if(chromatogramSelection instanceof ChromatogramSelectionCSD chromatogramSelectionCSD) {
			chromatogramSelectionCSD.update(true);
		}
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
			resetNoiseFactor();
		}
	}

	@Override
	protected String getNoiseCalculatorId() {

		NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = getMeasurementResult(NoiseSegmentMeasurementResult.class);
		if(noiseSegmentMeasurementResult != null) {
			return noiseSegmentMeasurementResult.getNoiseCalculatorId();
		} else {
			return PreferenceSupplier.getSelectedNoiseCalculatorId();
		}
	}

	@Override
	protected INoiseCalculator createNoiseCalculator(String id) {

		return NoiseCalculator.getNoiseCalculator(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IChromatogramPeakCSD> getPeaks() {

		return (List<IChromatogramPeakCSD>)super.getPeaks();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IChromatogramPeakCSD> getPeaks(int startRetentionTime, int stopRetentionTime) {

		return (List<IChromatogramPeakCSD>)super.getPeaks(startRetentionTime, stopRetentionTime);
	}
}
