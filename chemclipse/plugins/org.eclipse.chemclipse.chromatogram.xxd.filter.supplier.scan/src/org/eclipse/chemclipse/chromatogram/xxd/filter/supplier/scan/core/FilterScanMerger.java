/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.settings.FilterSettingsScanMerger;
import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.csd.model.implementation.ScanCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.vsd.model.core.ISignalVSD;
import org.eclipse.chemclipse.vsd.model.implementation.ScanVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.core.implementation.ScanWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class FilterScanMerger extends AbstractChromatogramFilter {

	private static final String MESSAGE = "The scans have been merged.";

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof FilterSettingsScanMerger settings) {
				applyScanMergerFilter(chromatogramSelection, settings, monitor);
				processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, "Scan Merger", MESSAGE));
				processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, MESSAGE));
				chromatogramSelection.getChromatogram().setDirty(true);
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) {

		FilterSettingsScanMerger filterSettings = PreferenceSupplier.getScanMergerFilterSettings();
		return applyFilter(chromatogramSelection, filterSettings, monitor);
	}

	private void applyScanMergerFilter(IChromatogramSelection chromatogramSelection, FilterSettingsScanMerger settings, IProgressMonitor monitor) {

		if(chromatogramSelection != null) {
			/*
			 * The complete range of the chromatogram must be processed.
			 */
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			int newScanInterval = (int)Math.round(chromatogram.getScanInterval() * 2.0d);
			/*
			 * Map the scans.
			 */
			TreeMap<Integer, IScan> scanMap = new TreeMap<>();
			for(IScan scan : chromatogram.getScans()) {
				scanMap.put(scan.getRetentionTime(), scan);
			}
			/*
			 * Calculate the merged scans.
			 */
			List<IScan> scansMerged = new ArrayList<>();
			for(int i = 1; i <= chromatogram.getScans().size(); i++) {
				if(i % 2 == 1) {
					IScan scan = chromatogram.getScan(i);
					Map.Entry<Integer, IScan> scanEntryNext = scanMap.higherEntry(scan.getRetentionTime());
					if(scanEntryNext != null) {
						//
						IScan scanNext = scanEntryNext.getValue();
						int retentionTimeCenter = (int)((scanNext.getRetentionTime() + scan.getRetentionTime()) / 2.0d);
						float totalSignalMerged = (float)((scan.getTotalSignal() + scanNext.getTotalSignal()) / 2.0d);
						//
						if(scan instanceof IScanCSD) {
							/*
							 * CSD
							 */
							if(scanNext instanceof IScanCSD) {
								IScanCSD scanMerged = new ScanCSD(totalSignalMerged);
								scanMerged.setRetentionTime(retentionTimeCenter);
								scansMerged.add(scanMerged);
							}
						} else if(scan instanceof IScanMSD currentScan) {
							/*
							 * MSD
							 */
							if(scanNext instanceof IScanMSD nextScan) {
								IScanMSD scanMerged = new VendorMassSpectrum();
								scanMerged.setRetentionTime(retentionTimeCenter);
								scanMerged.addIons(currentScan.getIons(), true);
								scanMerged.addIons(nextScan.getIons(), true);
								scanMerged.adjustTotalSignal(totalSignalMerged);
								scansMerged.add(scanMerged);
							}
						} else if(scan instanceof IScanWSD currentScan) {
							/*
							 * WSD
							 */
							if(scanNext instanceof IScanWSD nextScan) {
								IScanWSD scanMerged = new ScanWSD();
								scanMerged.setRetentionTime(retentionTimeCenter);
								Map<Float, IScanSignalWSD> scanSignals = getScanSignals(currentScan, nextScan);
								for(IScanSignalWSD scanSignalWSD : scanSignals.values()) {
									scanMerged.addScanSignal(scanSignalWSD);
								}
								scanMerged.adjustTotalSignal(totalSignalMerged);
								scansMerged.add(scanMerged);
							}
						} else if(scan instanceof IScanVSD currentScan) {
							/*
							 * ISD
							 */
							if(scanNext instanceof IScanVSD nextScan) {
								IScanVSD scanMerged = new ScanVSD();
								scanMerged.setRetentionTime(retentionTimeCenter);
								Map<Double, ISignalVSD> scanSignals = getScanSignals(currentScan, nextScan);
								for(ISignalVSD signal : scanSignals.values()) {
									scanMerged.getProcessedSignals().add(signal);
								}
								scanMerged.adjustTotalSignal(totalSignalMerged);
								scansMerged.add(scanMerged);
							}
						}
					}
				}
			}
			/*
			 * Don't recalculate the retention times via
			 * chromatogram.recalculateRetentionTimes();
			 */
			chromatogram.replaceAllScans(scansMerged);
			chromatogram.setScanDelay(chromatogram.getStartRetentionTime());
			chromatogram.setScanInterval(newScanInterval);
			chromatogram.recalculateScanNumbers();
			chromatogram.recalculateTheNoiseFactor();
			chromatogramSelection.reset();
		}
	}

	private Map<Float, IScanSignalWSD> getScanSignals(IScanWSD currentScan, IScanWSD nextScan) {

		Map<Float, IScanSignalWSD> scanSignals = new HashMap<>();
		/*
		 * Initial Scan
		 */
		for(IScanSignalWSD scanSignalWSD : currentScan.getScanSignals()) {
			scanSignals.put(scanSignalWSD.getWavelength(), scanSignalWSD);
		}
		/*
		 * Next Scan
		 */
		for(IScanSignalWSD scanSignalWSD : nextScan.getScanSignals()) {
			IScanSignalWSD scanSignal = scanSignals.get(scanSignalWSD.getWavelength());
			if(scanSignal == null) {
				scanSignals.put(scanSignalWSD.getWavelength(), scanSignalWSD);
			} else {
				scanSignal.setAbsorbance(scanSignal.getAbsorbance() + scanSignalWSD.getAbsorbance());
			}
		}
		//
		return scanSignals;
	}

	private Map<Double, ISignalVSD> getScanSignals(IScanVSD currentScan, IScanVSD nextScan) {

		Map<Double, ISignalVSD> scanSignals = new HashMap<>();
		/*
		 * Initial Scan
		 */
		for(ISignalVSD signal : currentScan.getProcessedSignals()) {
			scanSignals.put(signal.getWavenumber(), signal);
		}
		/*
		 * Next Scan
		 */
		for(ISignalVSD signal : nextScan.getProcessedSignals()) {
			ISignalVSD scanSignal = scanSignals.get(signal.getWavenumber());
			if(scanSignal == null) {
				scanSignals.put(signal.getWavenumber(), signal);
			} else {
				scanSignal.setIntensity(scanSignal.getIntensity() + signal.getIntensity());
			}
		}
		//
		return scanSignals;
	}
}
