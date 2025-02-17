/*******************************************************************************
 * Copyright (c) 2020, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.result.ChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.exceptions.FilterException;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.scan.settings.FilterSettingsDeleteIdentifier;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class FilterDeleteIdentifier extends AbstractChromatogramFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			try {
				if(chromatogramFilterSettings instanceof FilterSettingsDeleteIdentifier settings) {
					if(settings.isDeleteScanIdentifications()) {
						removeScanIdentifications(chromatogramSelection, monitor);
						processingInfo.addMessage(new ProcessingMessage(MessageType.INFO, "Delete Scan Targets", "Scan Targets have been removed successfully."));
						processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.OK, "Scan identifications have been removed successfully."));
						chromatogramSelection.getChromatogram().setDirty(true);
					}
				}
			} catch(FilterException e) {
				processingInfo.setProcessingResult(new ChromatogramFilterResult(ResultStatus.EXCEPTION, e.getMessage()));
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) {

		FilterSettingsDeleteIdentifier filterSettings = PreferenceSupplier.getDeleteIdentifierFilterSettings();
		return applyFilter(chromatogramSelection, filterSettings, monitor);
	}

	private void removeScanIdentifications(IChromatogramSelection chromatogramSelection, IProgressMonitor monitor) throws FilterException {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		int startScan = chromatogram.getScanNumber(chromatogramSelection.getStartRetentionTime());
		int stopScan = chromatogram.getScanNumber(chromatogramSelection.getStopRetentionTime());
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Delete Scan Targets from chromatogram.", stopScan - startScan);
		/*
		 * Iterate through all selected scans and mark those to be removed.
		 */
		try {
			for(int i = startScan; i <= stopScan; i++) {
				IScan scan = chromatogram.getScan(i);
				scan.getTargets().clear();
				subMonitor.worked(1);
			}
		} finally {
			SubMonitor.done(subMonitor);
		}
	}
}
