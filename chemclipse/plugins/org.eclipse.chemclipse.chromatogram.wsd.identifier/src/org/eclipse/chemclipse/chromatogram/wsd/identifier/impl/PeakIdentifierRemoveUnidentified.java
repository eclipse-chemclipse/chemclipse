/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.impl;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.peak.AbstractPeakIdentifierWSD;
import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.IPeakIdentifierSettingsWSD;
import org.eclipse.chemclipse.model.identifier.IPeakIdentificationResults;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IPeakWSD;
import org.eclipse.core.runtime.IProgressMonitor;

public class PeakIdentifierRemoveUnidentified extends AbstractPeakIdentifierWSD {

	@Override
	public IProcessingInfo<IPeakIdentificationResults> identify(List<? extends IPeakWSD> peaks, IPeakIdentifierSettingsWSD peakIdentifierSettings, IProgressMonitor monitor) {

		IProcessingInfo<IPeakIdentificationResults> processingInfo = new ProcessingInfo<>();
		/*
		 * Remove all unidentified peaks.
		 */
		try {
			for(IPeakWSD peak : peaks) {
				if(peak instanceof IChromatogramPeakWSD chromatogramPeakWSD) {
					IChromatogramWSD chromatogramWSD = chromatogramPeakWSD.getChromatogram();
					if(chromatogramWSD != null) {
						if(chromatogramPeakWSD.getTargets().isEmpty()) {
							chromatogramWSD.removePeak(chromatogramPeakWSD);
						}
					}
				}
			}
			processingInfo.addInfoMessage("Identifier", "Unidentified peaks have been removed.");
		} catch(Exception e) {
			processingInfo.addErrorMessage("Identifier", "Failed to remove unidentified peaks.", e);
		}
		return processingInfo;
	}
}
