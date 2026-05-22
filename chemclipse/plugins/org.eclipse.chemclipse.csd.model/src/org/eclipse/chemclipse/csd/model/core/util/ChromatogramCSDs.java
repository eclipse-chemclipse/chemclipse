/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.model.core.util;

import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.implementation.ChromatogramPeakCSD;
import org.eclipse.chemclipse.model.core.IIntegrationEntry;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

/**
 * Utility class for {@link IChromatogramCSD} related stuff.
 *
 * @author Alexander Kerner
 *
 */
public class ChromatogramCSDs {

	/**
	 * Convenience method to add a {@link IPeakCSD} to an {@link IChromatogramCSD}.
	 * <br>
	 * Internally a {@link IChromatogramPeakCSD} is created, added to given chromatogram and finally returned.
	 * <br>
	 * {@link IIntegrationEntry integration entries} and {@link IIdentificationTarget peak targets} are copied from given peak.
	 *
	 * @param chromatogram
	 *            {@link IChromatogramCSD} to which given peak should be added
	 * @param peak
	 *            {@link IPeakCSD} peak to add to given chromatogram
	 *
	 * @return {@link IChromatogramPeakCSD} which was created and added to given {@link IChromatogramCSD}
	 *
	 * @see IPeakCSD
	 * @see IChromatogramCSD
	 * @see IChromatogramPeakCSD
	 * @see IIdentificationTarget
	 * @see IIntegrationEntry
	 */
	public static ChromatogramPeakCSD addPeakToChromatogram(IChromatogramCSD chromatogram, IPeakCSD peak) {

		// TODO: find common super type implementation (MSD, CSD, WSD). -> Generic ChromatogramPeak type is needed
		ChromatogramPeakCSD chromatogramPeak = new ChromatogramPeakCSD(peak.getPeakModel(), chromatogram);
		chromatogramPeak.addAllIntegrationEntries(peak.getIntegrationEntries());
		chromatogramPeak.getTargets().addAll(peak.getTargets());
		chromatogram.getPeaks().add(chromatogramPeak);
		return chromatogramPeak;
	}
}
