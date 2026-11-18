/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Lorenz Gerber - initial API and implementation, advanced options
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.AbstractChromatogramFilterMSD;
import org.eclipse.chemclipse.chromatogram.msd.filter.impl.settings.FilterSettingsZeroValueRemoval;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * Fills missing values (zeros) in the ion channels of the selected range, see
 * {@link IonChannelGapFiller}.
 */
public class ChromatogramFilterZeroValueRemoval extends AbstractChromatogramFilterMSD {

	private static final String DESCRIPTION = "Remove Zero Values in Ion Channels";

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelectionMSD chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validate(chromatogramSelection, chromatogramFilterSettings));
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof FilterSettingsZeroValueRemoval settings) {
				IChromatogramMSD chromatogram = chromatogramSelection.getChromatogram();
				/*
				 * Scans may be lazily loaded proxies - load them for good, so the added ions stay.
				 */
				chromatogram.enforceLoadScanProxies(monitor != null ? monitor : new NullProgressMonitor());
				IonChannelGapFiller.Result result = IonChannelGapFiller.fill(getScans(chromatogramSelection), settings.getMinCoverage(), settings.getCoverageReference(), settings.getMaxShortGap());
				if(result.highResolution()) {
					processingInfo.addWarnMessage(DESCRIPTION, "High resolution data are not supported - nothing was filled.");
					processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.EXCEPTION, "High resolution data are not supported."));
				} else if(result.filled() > 0) {
					chromatogram.setDirty(true);
					String message = result.filled() + " missing value(s) filled - " + result.describeChannels(5) + ".";
					processingInfo.addInfoMessage(DESCRIPTION, message);
					processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, message));
				} else {
					processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, "No missing values to fill."));
				}
			} else {
				processingInfo.addErrorMessage(DESCRIPTION, "The filter settings instance is not a type of: " + FilterSettingsZeroValueRemoval.class);
			}
		}
		return processingInfo;
	}

	private List<IScanMSD> getScans(IChromatogramSelectionMSD chromatogramSelection) {

		int startScan = chromatogramSelection.getStartScan();
		int stopScan = chromatogramSelection.getStopScan();
		List<IScanMSD> scans = new ArrayList<>();
		for(IScan scan : chromatogramSelection.getChromatogram().getScans()) {
			if(scan instanceof IScanMSD scanMSD && scan.getScanNumber() >= startScan && scan.getScanNumber() <= stopScan) {
				scans.add(scanMSD);
			}
		}
		return scans;
	}
}
