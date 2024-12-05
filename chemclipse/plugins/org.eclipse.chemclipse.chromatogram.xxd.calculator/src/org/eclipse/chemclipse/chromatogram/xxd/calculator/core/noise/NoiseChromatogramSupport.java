/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Matthias Mailänder - remove noise enums
 * Philip Wenig - refactoring classifier
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.core.noise;

import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.NoiseChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.support.model.SegmentWidth;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class NoiseChromatogramSupport {

	public static List<INoiseSegment> getNoiseSegments(IChromatogram<?> chromatogram, IScanRange range, boolean includeBorders, IProgressMonitor monitor) {

		NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
		if(noiseSegmentMeasurementResult == null) {
			noiseSegmentMeasurementResult = applyNoiseSettings(chromatogram, new NoiseChromatogramClassifierSettings(), monitor);
			if(noiseSegmentMeasurementResult == null) {
				return Collections.emptyList();
			}
		}
		//
		return noiseSegmentMeasurementResult.getSegments(range, includeBorders);
	}

	public static NoiseSegmentMeasurementResult applyNoiseSettings(IChromatogram<?> chromatogram, NoiseChromatogramClassifierSettings settings, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		//
		String noiseCalculatorId = settings.getNoiseCalculatorId();
		INoiseCalculator noiseCalculator = settings.getNoiseCalculator();
		if(noiseCalculator == null) {
			throw new IllegalArgumentException("The noise calculator with the given id '" + noiseCalculatorId + "' is not available.");
		}
		//
		int segmentWidth = settings.getSegmentWidth();
		do {
			ChromatogramSegmentation chromatogramSegmentation = new ChromatogramSegmentation(chromatogram, segmentWidth);
			chromatogram.addMeasurementResult(chromatogramSegmentation);
			//
			List<INoiseSegment> noiseSegments = noiseCalculator.getNoiseSegments(chromatogram, subMonitor.split(80));
			if(noiseSegments.isEmpty()) {
				segmentWidth = SegmentWidth.getLower(segmentWidth);
				subMonitor.setWorkRemaining(100);
			} else {
				/*
				 * Propose the noise segments.
				 */
				NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = new NoiseSegmentMeasurementResult(noiseSegments, chromatogramSegmentation, noiseCalculatorId);
				chromatogram.addMeasurementResult(noiseSegmentMeasurementResult);
				chromatogram.setDirty(true);
				//
				return noiseSegmentMeasurementResult;
			}
		} while(segmentWidth != 0);
		//
		return null;
	}
}