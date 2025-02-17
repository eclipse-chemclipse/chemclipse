/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - use IRetentionTimeRange instead of ChromatogramSelection
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogramPeaks;
import org.eclipse.chemclipse.model.support.IRetentionTimeRange;

public interface IChromatogramPeaksMSD extends IChromatogramPeaks {

	/**
	 * returns all peaks that are inside the given retention time, that means the retention time is within the start/stop retention time of the peak
	 * 
	 * @param retentionTime
	 * @return a list of peaks at the given retention time, ordered by the start retention time of the peak
	 */
	@Override
	List<IChromatogramPeakMSD> getPeaks(int startRetentionTime, int stopRetentionTime);

	/**
	 * Returns a list.
	 * Modification does not change the chromatogram peak list.
	 */
	@Override
	List<IChromatogramPeakMSD> getPeaks();

	/**
	 * Returns a list.
	 * Modification does not change the chromatogram peak list.
	 */
	@Override
	default List<IChromatogramPeakMSD> getPeaks(IRetentionTimeRange range) {

		if(range == null) {
			return getPeaks();
		}
		return getPeaks(range.getStartRetentionTime(), range.getStopRetentionTime());
	}
}
