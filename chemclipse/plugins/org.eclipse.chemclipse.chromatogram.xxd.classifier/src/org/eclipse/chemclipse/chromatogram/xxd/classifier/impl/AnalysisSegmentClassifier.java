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
package org.eclipse.chemclipse.chromatogram.xxd.classifier.impl;

import org.eclipse.chemclipse.chromatogram.xxd.classifier.core.AbstractChromatogramClassifier;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.IChromatogramClassifierResult;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.settings.IChromatogramClassifierSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.IScanRange;
import org.eclipse.chemclipse.model.support.ScanRange;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class AnalysisSegmentClassifier extends AbstractChromatogramClassifier {

	public AnalysisSegmentClassifier() {

		super(DataType.CSD, DataType.MSD, DataType.WSD, DataType.VSD);
	}

	@Override
	public IProcessingInfo<IChromatogramClassifierResult> applyClassifier(IChromatogramSelection chromatogramSelection, IChromatogramClassifierSettings chromatogramClassifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramClassifierResult> processingInfo = new ProcessingInfo<>();
		/*
		 * Settings
		 */
		AnalysisSegmentSettings settings = getSettings(chromatogramClassifierSettings);
		int analysisSegmentWidth = settings.getAnalysisSegmentWidth();
		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		chromatogram.clearAnalysisSegments();
		/*
		 * Calculate segments by fixed time blocks.
		 */
		int currentScan = 1;
		int startScan = 1;
		int offset = 0;
		for(IScan scan : chromatogram.getScans()) {
			int retentionTime = scan.getRetentionTime();
			int delta = retentionTime - offset;
			if(delta >= analysisSegmentWidth) {
				offset = retentionTime;
				IScanRange scanRange = new ScanRange(startScan, currentScan - 1);
				chromatogram.defineAnalysisSegment(scanRange);
				startScan = currentScan;
			}
			currentScan++;
		}
		/*
		 * Result
		 */
		chromatogram.addMeasurementResult(new AnalysisSegmentsResult(chromatogram.getAnalysisSegments()));
		processingInfo.setProcessingResult(new IChromatogramClassifierResult() {

			@Override
			public ResultStatus getResultStatus() {

				return ResultStatus.OK;
			}

			@Override
			public String getDescription() {

				return "Analysis Segements (Fixed Width) has been calculated successfully.";
			}
		});

		return processingInfo;
	}

	private AnalysisSegmentSettings getSettings(IChromatogramClassifierSettings chromatogramClassifierSettings) {

		AnalysisSegmentSettings settings;
		if(chromatogramClassifierSettings instanceof AnalysisSegmentSettings classifierSettings) {
			settings = classifierSettings;
		} else {
			settings = new AnalysisSegmentSettings();
		}

		return settings;
	}
}