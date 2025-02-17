/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.impl;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.impl.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.filter.impl.settings.PeakTargetsToReferencesSettings;
import org.eclipse.chemclipse.chromatogram.filter.l10n.Messages;
import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.TargetTransferSupport;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class PeakTargetsToReferencesFilter extends AbstractTransferFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof PeakTargetsToReferencesSettings settings) {
				transferTargets(chromatogramSelection, settings);
				processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, Messages.targetsTransferredSuccessfully));
			}
		}
		//
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) {

		PeakTargetsToReferencesSettings settings = PreferenceSupplier.getPeaksToReferencesTransferSettings();
		return applyFilter(chromatogramSelection, settings, monitor);
	}

	private void transferTargets(IChromatogramSelection chromatogramSelection, PeakTargetsToReferencesSettings settings) {

		TargetTransferSupport targetTransferSupport = new TargetTransferSupport();
		List<IChromatogram> referencedChromatograms = chromatogramSelection.getChromatogram().getReferencedChromatograms();
		if(!referencedChromatograms.isEmpty()) {
			int retentionTimeDelta = (int)(settings.getDeltaRetentionTime() * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
			boolean useBestTargetOnly = settings.isUseBestTargetOnly();
			List<? extends IPeak> peaksSource = extractPeaks(chromatogramSelection);
			for(IChromatogram referencedChromatogram : referencedChromatograms) {
				List<? extends IPeak> peaksSink = extractPeaks(referencedChromatogram);
				targetTransferSupport.transferPeakTargets(peaksSource, peaksSink, retentionTimeDelta, useBestTargetOnly);
			}
		}
	}
}
