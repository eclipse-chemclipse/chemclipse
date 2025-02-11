/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.settings.NoiseChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.core.ChromatogramAnalysisSegment;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.results.ChromatogramSegmentation;
import org.eclipse.chemclipse.model.results.NoiseSegmentMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.model.support.INoiseSegment;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.NoiseSegment;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.support.model.SegmentWidth;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class NoiseChromatogramSupport {

	public static List<INoiseSegment> getNoiseSegments(IChromatogram chromatogram, IScanRange range, boolean includeBorders, IProgressMonitor monitor) {

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

	public static NoiseSegmentMeasurementResult applyNoiseSettings(IChromatogram chromatogram, NoiseChromatogramClassifierSettings settings, IProgressMonitor monitor) {

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

	public static String addNoiseSegment(IChromatogramSelection chromatogramSelection, boolean useOnlyNewSegment) {

		String message = null;
		//
		if(chromatogramSelection != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			if(chromatogram != null) {
				INoiseCalculator noiseCalculator = chromatogram.getNoiseCalculator();
				if(noiseCalculator != null) {
					/*
					 * Scan Range
					 */
					int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
					int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
					IScanRange scanRange = new ScanRange(startScan, stopScan);
					if(scanRange.getWidth() % 2 == 0) {
						scanRange = new ScanRange(startScan, stopScan - 1);
					}
					//
					if(scanRange.getWidth() >= 5) {
						/*
						 * Validity Check
						 */
						IAnalysisSegment analysisSegment = new ChromatogramAnalysisSegment(scanRange, chromatogram, null);
						INoiseSegment noiseSegment = new NoiseSegment(analysisSegment, 0.0d);
						noiseSegment.setUse(true);
						noiseSegment.setUserSelection(true);
						//
						NoiseSegmentMeasurementResult noiseSegmentMeasurementResult = chromatogram.getMeasurementResult(NoiseSegmentMeasurementResult.class);
						if(noiseSegmentMeasurementResult == null) {
							INoiseCalculatorSupplier noiseCalculatorSupplier = getNoiseCalculatorSupplier(chromatogram);
							if(noiseCalculatorSupplier != null) {
								/*
								 * New Result - useOnlyNewSegment check not needed here
								 */
								ChromatogramSegmentation chromatogramSegmentation = new ChromatogramSegmentation(chromatogram, noiseSegment.getWidth());
								String noiseCalculatorId = noiseCalculatorSupplier.getId();
								noiseSegmentMeasurementResult = new NoiseSegmentMeasurementResult(Arrays.asList(noiseSegment), chromatogramSegmentation, noiseCalculatorId);
								chromatogram.addMeasurementResult(noiseSegmentMeasurementResult);
							}
						} else {
							/*
							 * Only use the new segment
							 */
							if(useOnlyNewSegment) {
								for(INoiseSegment segment : noiseSegmentMeasurementResult.getResult()) {
									segment.setUse(false);
								}
							}
							/*
							 * Add the noise segment
							 */
							noiseSegmentMeasurementResult.getResult().add(noiseSegment);
							chromatogram.recalculateTheNoiseFactor();
						}
						//
						chromatogram.setDirty(true);
					} else {
						message = "Select a range of odd width having at least 5 scans.";
					}
				}
			}
		}
		//
		return message;
	}

	private static INoiseCalculatorSupplier getNoiseCalculatorSupplier(IChromatogram chromatogram) {

		Collection<INoiseCalculatorSupplier> noiseCalculatorSuppliers = NoiseCalculator.getNoiseCalculatorSupport().getCalculatorSupplier();
		//
		INoiseCalculator noiseCalculator = chromatogram.getNoiseCalculator();
		if(noiseCalculator != null) {
			for(INoiseCalculatorSupplier noiseCalculatorSupplier : noiseCalculatorSuppliers) {
				INoiseCalculator reference = NoiseCalculator.getNoiseCalculator(noiseCalculatorSupplier.getId());
				if(noiseCalculator.getClass().equals(reference.getClass())) {
					return noiseCalculatorSupplier;
				}
			}
		}
		//
		return null;
	}
}
