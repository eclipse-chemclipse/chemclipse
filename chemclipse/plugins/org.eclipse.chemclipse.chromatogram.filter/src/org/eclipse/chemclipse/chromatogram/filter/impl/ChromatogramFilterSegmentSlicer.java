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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.impl.settings.FilterSettingsSegmentSlicer;
import org.eclipse.chemclipse.chromatogram.filter.model.SegmentSlicerOption;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.implementation.ChromatogramCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.HeaderUtil;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.implementation.ChromatogramVSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ChromatogramWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramFilterSegmentSlicer extends AbstractChromatogramFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof FilterSettingsSegmentSlicer filterSettings) {
				/*
				 * Settings
				 */
				IChromatogram chromatogram = chromatogramSelection.getChromatogram();
				HeaderField headerField = filterSettings.getHeaderField();
				boolean resetRetentionTimes = filterSettings.isResetRetentionTimes();
				boolean keepPeakInCenter = filterSettings.isKeepPeakInCenter();
				SegmentSlicerOption segmentSlicerOption = filterSettings.getCutterOption();
				List<List<IScan>> stackedScans = new ArrayList<>();
				if(segmentSlicerOption == SegmentSlicerOption.ANALYSIS_SEGMENTS) {
					/*
					 * Use the exact start/stop retention time of each analysis segment.
					 */
					stackedScans = getStackedScansFromSegments(chromatogram);
					if(stackedScans.size() < 3) {
						return processingInfo;
					}
				} else {
					/*
					 * Collect peak positions (retention times of peak maxima).
					 */
					List<Integer> positions = getPeakPositions(chromatogram);
					if(positions.size() < 3) {
						return processingInfo;
					}
					Collections.sort(positions);
					int size = positions.size();
					if(keepPeakInCenter) {
						/*
						 * For each inner peak compute the largest symmetric half-width that keeps
						 * the peak at the exact center of its section without overlapping either
						 * neighbour's midpoint: halfWidth = min(spacing_left, spacing_right) / 2.
						 * The global halfWidth is the minimum across all inner peaks so that every
						 * section has the same width and every peak sits at its center.
						 */
						int halfWidth = Integer.MAX_VALUE;
						for(int i = 1; i <= size - 2; i++) {
							int retentionTime = positions.get(i);
							int retentionTimePrevious = positions.get(i - 1);
							int retentionTimeNext = positions.get(i + 1);
							halfWidth = Math.min(halfWidth, Math.min((retentionTime - retentionTimePrevious) / 2, (retentionTimeNext - retentionTime) / 2));
						}
						/*
						 * Collect scans for each peak in [peakRT - halfWidth, peakRT + halfWidth].
						 * Scans that fall outside every window are not transferred.
						 */
						for(int peakRT : positions) {
							int windowStart = peakRT - halfWidth;
							int windowStop = peakRT + halfWidth;
							List<IScan> scans = new ArrayList<>();
							for(IScan scan : chromatogram.getScans()) {
								int retentionTime = scan.getRetentionTime();
								if(retentionTime >= windowStart && retentionTime <= windowStop) {
									scans.add(scan);
								}
							}
							if(!scans.isEmpty()) {
								stackedScans.add(scans);
							}
						}
					} else {
						/*
						 * Sliding-window approach: delta = minimum spacing between consecutive
						 * positions (inner positions only). Trim to [first - delta, last + delta]
						 * then segment by a window of width 2 * delta.
						 */
						int delta = Integer.MAX_VALUE;
						for(int i = 1; i <= size - 2; i++) {
							int retentionTime = positions.get(i);
							int retentionTimePrevious = positions.get(i - 1);
							int retentionTimeNext = positions.get(i + 1);
							delta = Math.min(delta, Math.min(retentionTime - retentionTimePrevious, retentionTimeNext - retentionTime));
						}

						int startPosition = positions.get(0) - delta;
						int stopPosition = positions.get(size - 1) + delta;
						List<IScan> trimmedScans = new ArrayList<>();
						for(IScan scan : chromatogram.getScans()) {
							int retentionTime = scan.getRetentionTime();
							if(retentionTime >= startPosition && retentionTime <= stopPosition) {
								trimmedScans.add(scan);
							}
						}

						int segmentWidth = 2 * delta;
						List<IScan> currentScans = null;
						int offset = 0;
						for(IScan scan : trimmedScans) {
							int retentionTime = scan.getRetentionTime();
							if(currentScans == null) {
								currentScans = new ArrayList<>();
								currentScans.add(scan);
								stackedScans.add(currentScans);
								offset = retentionTime;
							} else if((retentionTime - offset) > segmentWidth) {
								currentScans = new ArrayList<>();
								currentScans.add(scan);
								stackedScans.add(currentScans);
								offset = retentionTime;
							} else {
								currentScans.add(scan);
							}
						}
					}
				}
				/*
				 * Assign master and references.
				 */
				if(!stackedScans.isEmpty()) {
					int scanInterval = chromatogram.getScanInterval();
					chromatogram.getPeaks().clear();
					chromatogram.replaceAllScans(stackedScans.get(0));
					HeaderUtil.setHeaderData(chromatogram, headerField, "Cut 1");
					calculateScanIntervalAndDelay(chromatogram, resetRetentionTimes, 0, scanInterval);
					for(int i = 1; i < stackedScans.size(); i++) {
						IChromatogram chromatogramReference = createChromatogramReference(chromatogram);
						HeaderUtil.setHeaderData(chromatogramReference, headerField, "Cut " + (i + 1));
						chromatogramReference.addScans(stackedScans.get(i));
						calculateScanIntervalAndDelay(chromatogramReference, resetRetentionTimes, 0, scanInterval);
						chromatogram.addReferencedChromatogram(chromatogramReference);
					}
				}
			}
		}

		return processingInfo;
	}

	private List<Integer> getPeakPositions(IChromatogram chromatogram) {

		List<Integer> positions = new ArrayList<>();
		for(IPeak peak : chromatogram.getPeaks()) {
			positions.add(peak.getPeakModel().getPeakMaximum().getRetentionTime());
		}
		return positions;
	}

	private List<List<IScan>> getStackedScansFromSegments(IChromatogram chromatogram) {

		List<List<IScan>> stackedScans = new ArrayList<>();
		for(IAnalysisSegment segment : chromatogram.getAnalysisSegments()) {
			List<IScan> scans = new ArrayList<>();
			for(IScan scan : chromatogram.getScans()) {
				if(segment.containsRetentionTime(scan.getRetentionTime())) {
					scans.add(scan);
				}
			}
			if(!scans.isEmpty()) {
				stackedScans.add(scans);
			}
		}
		return stackedScans;
	}

	private void calculateScanIntervalAndDelay(IChromatogram chromatogram, boolean resetRetentionTimes, int scanDelay, int scanInterval) {

		if(resetRetentionTimes) {
			chromatogram.setScanDelay(0);
			chromatogram.setScanInterval(scanInterval);
			chromatogram.recalculateRetentionTimes();
		}

		int startRetentionTime = chromatogram.getStartRetentionTime();
		int stopRetentionTime = chromatogram.getStopRetentionTime();
		float deltaRetentionTime = stopRetentionTime - startRetentionTime + 1;
		int numberOfScans = chromatogram.getNumberOfScans();
		if(startRetentionTime > 0) {
			scanDelay = startRetentionTime;
		}

		if(numberOfScans > 0 && deltaRetentionTime > 0) {
			float calculation = deltaRetentionTime / numberOfScans / 10.0f;
			scanInterval = Math.round(calculation) * 10;
		}

		chromatogram.setScanDelay(scanDelay);
		chromatogram.setScanInterval(scanInterval);
		chromatogram.recalculateRetentionTimes();
	}

	private IChromatogram createChromatogramReference(IChromatogram chromatogram) {

		IChromatogram chromatogramReference = null;
		if(chromatogram instanceof IChromatogramCSD) {
			chromatogramReference = new ChromatogramCSD();
		} else if(chromatogram instanceof IChromatogramMSD) {
			chromatogramReference = new ChromatogramMSD();
		} else if(chromatogram instanceof IChromatogramWSD) {
			chromatogramReference = new ChromatogramWSD();
		} else if(chromatogram instanceof IChromatogramVSD) {
			chromatogramReference = new ChromatogramVSD();
		}
		if(chromatogramReference != null) {
			chromatogramReference.setFile(chromatogram.getFile());
			chromatogramReference.setConverterId(chromatogram.getConverterId());
		}
		return chromatogramReference;
	}
}