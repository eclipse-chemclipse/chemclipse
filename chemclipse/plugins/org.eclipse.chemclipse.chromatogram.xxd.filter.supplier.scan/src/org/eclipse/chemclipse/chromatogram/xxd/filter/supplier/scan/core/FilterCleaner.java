/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * This filter removes empty scans.
 *
 */
public class FilterCleaner extends AbstractChromatogramFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(!applyChromatogramCleanerFilter(chromatogramSelection, monitor)) {
				processingInfo.addMessage(new ProcessingMessage(MessageType.WARN, "Chromatogram Cleaner", "The filter has been cancelled, no scans have been removed."));
				return processingInfo;
			}
			processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, "Chromatogram Cleaner", "Empty scans have been removed successfully."));
			processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, "Empty scans have been removed successfully."));
			chromatogramSelection.getChromatogram().setDirty(true);
		}
		chromatogramSelection.getChromatogram().setDirty(true);
		return processingInfo;
	}

	/**
	 * Returns false if the user cancelled before the scans were removed. Nothing is removed then.
	 */
	private boolean applyChromatogramCleanerFilter(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
		List<Integer> scansToRemove = new ArrayList<>();
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Remove empty scans.", 100);
		/*
		 * Import all scans.
		 */
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			chromatogramMSD.enforceLoadScanProxies(subMonitor.newChild(80));
		}
		if(subMonitor.isCanceled()) {
			return false;
		}
		/*
		 * Iterate through all selected scans and mark those to be removed.
		 */
		SubMonitor subMonitorLoad = SubMonitor.convert(subMonitor.newChild(10), "Check scans.", stopScan - startScan + 1);
		for(int scan = startScan; scan <= stopScan; scan++) {
			if(subMonitorLoad.isCanceled()) {
				return false;
			}
			IScan chromatogramScan = chromatogram.getScan(scan);
			if(chromatogramScan instanceof IScanMSD scanMSD) {
				/*
				 * MSD
				 */
				if(scanMSD.isEmpty()) {
					scansToRemove.add(scan);
				}
			} else if(chromatogramScan instanceof IScanCSD scanCSD) {
				/*
				 * CSD
				 */
				if(scanCSD.getTotalSignal() == 0) {
					scansToRemove.add(scan);
				}
			} else if(chromatogramScan instanceof IScanWSD scanWSD) {
				/*
				 * WSD
				 */
				if(scanWSD.getScanSignals().isEmpty()) {
					scansToRemove.add(scan);
				}
			}
			subMonitorLoad.worked(1);
		}
		/*
		 * Use a remove counter, because each time a scan will be removed, the chromatogram contains one scan less.
		 */
		SubMonitor subMonitorRemove = SubMonitor.convert(subMonitor.newChild(10), "Remove empty scans.", scansToRemove.size());
		try {
			int removeCounter = 0;
			for(Integer scan : scansToRemove) {
				scan -= removeCounter;
				chromatogram.removeScan(scan);
				removeCounter++;
				subMonitorRemove.worked(1);
			}
		} finally {
			SubMonitor.done(subMonitorRemove);
		}

		chromatogram.recalculateScanNumbers();
		chromatogramSelection.reset();
		return true;
	}
}
