/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.impl;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.impl.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.filter.impl.settings.FilterSettingsHeaderTransfer;
import org.eclipse.chemclipse.chromatogram.filter.result.IChromatogramFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.support.HeaderField;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.support.HeaderUtil;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramFilterHeaderTransfer extends AbstractChromatogramFilter implements IChromatogramFilter {

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection<?, ?> chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramFilterResult> processingInfo = validate(chromatogramSelection, chromatogramFilterSettings);
		if(!processingInfo.hasErrorMessages()) {
			if(chromatogramFilterSettings instanceof FilterSettingsHeaderTransfer filterSettings) {
				/*
				 * Settings
				 */
				IChromatogram<?> chromatogram = chromatogramSelection.getChromatogram();
				HeaderField headerFieldSource = filterSettings.getHeaderFieldSource();
				String dataSoure = HeaderUtil.getHeaderData(chromatogram, headerFieldSource, "");
				//
				HeaderField headerFieldSink = filterSettings.getHeaderFieldSink();
				for(IChromatogram<?> chromatogramReference : chromatogram.getReferencedChromatograms()) {
					HeaderUtil.setHeaderData(chromatogramReference, headerFieldSink, dataSoure);
				}
			}
		}
		//
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramFilterResult> applyFilter(IChromatogramSelection<?, ?> chromatogramSelection, IProgressMonitor monitor) {

		FilterSettingsHeaderTransfer filterSettings = PreferenceSupplier.getFilterSettingsHeaderTransfer();
		return applyFilter(chromatogramSelection, filterSettings, monitor);
	}
}