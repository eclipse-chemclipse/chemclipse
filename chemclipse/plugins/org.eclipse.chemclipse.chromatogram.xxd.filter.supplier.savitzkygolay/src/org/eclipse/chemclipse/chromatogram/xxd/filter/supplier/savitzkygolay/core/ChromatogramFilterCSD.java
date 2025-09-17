/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Lorenz Gerber - Ion-wise msd chromatogram filter
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.core;

import java.util.Iterator;

import org.eclipse.chemclipse.chromatogram.csd.filter.core.chromatogram.AbstractChromatogramFilterCSD;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.processor.SavitzkyGolayProcessor;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.savitzkygolay.settings.ChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.signals.ITotalScanSignal;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.model.signals.TotalScanSignalExtractor;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramFilterCSD extends AbstractChromatogramFilterCSD {

	private IChromatogramFilterResult process(IChromatogramSelectionCSD chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IChromatogramCSD chromatogramCSD = chromatogramSelection.getChromatogram();
		TotalScanSignalExtractor totalScanSignalExtractor = new TotalScanSignalExtractor(chromatogramCSD);
		ITotalScanSignals totalSignals = totalScanSignalExtractor.getTotalScanSignals(chromatogramSelection, false);
		IChromatogramFilterResult chromatogramFilterResult = SavitzkyGolayProcessor.apply(totalSignals, (ChromatogramFilterSettings)chromatogramFilterSettings, monitor);
		chromatogramSelection.getChromatogram().setDirty(true);
		if(chromatogramFilterResult.getResultStatus().equals(ResultStatus.OK)) {
			updateSignal(totalSignals, chromatogramCSD);
		}
		return chromatogramFilterResult;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelectionCSD chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.setProcessingResult(process(chromatogramSelection, chromatogramFilterSettings, monitor));
		chromatogramSelection.getChromatogram().setDirty(true);
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelectionCSD chromatogramSelection, IProgressMonitor monitor) {

		ChromatogramFilterSettings chromatogramFilterSettings = PreferenceSupplier.getFilterSettings();
		IProcessingInfo<IChromatogramFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.setProcessingResult(process(chromatogramSelection, chromatogramFilterSettings, monitor));
		chromatogramSelection.getChromatogram().setDirty(true);
		return processingInfo;
	}

	private void updateSignal(ITotalScanSignals totalSignals, IChromatogramCSD chromatogram) {

		Iterator<Integer> iteratorScans = totalSignals.iterator();
		while(iteratorScans.hasNext()) {
			Integer scan = iteratorScans.next();
			IScanCSD scanCSD = chromatogram.getScan(scan);
			ITotalScanSignal totalscanSignal = totalSignals.getTotalScanSignal(scan);
			scanCSD.adjustTotalSignal(totalscanSignal.getTotalSignal());
		}
	}
}
