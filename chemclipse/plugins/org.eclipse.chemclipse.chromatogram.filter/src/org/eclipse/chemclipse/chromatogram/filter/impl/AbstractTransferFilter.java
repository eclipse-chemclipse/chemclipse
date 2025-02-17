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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.AbstractChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilter;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.selection.ChromatogramSelectionCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.ChromatogramSelectionWSD;

public abstract class AbstractTransferFilter extends AbstractChromatogramFilter implements IChromatogramFilter {

	protected List<IScan> extractIdentifiedScans(IChromatogramSelection chromatogramSelection) {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		//
		int startRetentionTime = chromatogramSelection.getStartRetentionTime();
		int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
		int startScan = chromatogram.getScanNumber(startRetentionTime);
		int stopScan = chromatogram.getScanNumber(stopRetentionTime);
		//
		List<IScan> identifiedScans = new ArrayList<>();
		for(int i = startScan; i <= stopScan; i++) {
			IScan scan = chromatogram.getScan(i);
			if(!scan.getTargets().isEmpty()) {
				identifiedScans.add(scan);
			}
		}
		//
		return identifiedScans;
	}

	protected List<? extends IPeak> extractPeaks(IChromatogram chromatogram) {

		if(chromatogram instanceof IChromatogramCSD chromatogramCSD) {
			return extractPeaks(new ChromatogramSelectionCSD(chromatogramCSD));
		} else if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			return extractPeaks(new ChromatogramSelectionMSD(chromatogramMSD));
		} else if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
			return extractPeaks(new ChromatogramSelectionWSD(chromatogramWSD));
		} else {
			return new ArrayList<>();
		}
	}

	protected List<? extends IPeak> extractPeaks(IChromatogramSelection chromatogramSelection) {

		return chromatogramSelection.getChromatogram().getPeaks(chromatogramSelection);
	}
}
