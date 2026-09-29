/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import java.util.List;

import org.eclipse.chemclipse.model.comparator.PeakRetentionTimeComparator;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramPeak;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

public class PeakSelectionSupport {

	/**
	 * Selects the peak that follows or precedes the currently selected one and notifies
	 * the parts about it. The selection wraps around at the ends of the peak list.
	 *
	 * @return the newly selected peak or null if there is none
	 */
	public static IPeak selectNeighborPeak(Display display, IChromatogramSelection chromatogramSelection, boolean next) {

		if(chromatogramSelection == null) {
			return null;
		}
		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		List<? extends IChromatogramPeak> peaks = chromatogram.getPeaks();
		if(peaks.isEmpty()) {
			return null;
		}
		peaks.sort(new PeakRetentionTimeComparator());
		int index = peaks.indexOf(chromatogramSelection.getSelectedPeak());
		if(index == -1) {
			return null;
		}
		int nextIndex;
		if(next) {
			nextIndex = (index < peaks.size() - 1) ? index + 1 : 0;
		} else {
			nextIndex = (index > 0) ? index - 1 : peaks.size() - 1;
		}
		IPeak nextPeak = peaks.get(nextIndex);
		chromatogramSelection.setSelectedPeak(nextPeak);
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		boolean moveRetentionTimeOnPeakSelection = preferenceStore.getBoolean(PreferenceSupplier.P_MOVE_RETENTION_TIME_ON_PEAK_SELECTION);
		if(moveRetentionTimeOnPeakSelection) {
			ChromatogramDataSupport.adjustChromatogramSelection(nextPeak, chromatogramSelection);
		}
		UpdateNotifierUI.update(display, nextPeak);
		IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(nextPeak);
		UpdateNotifierUI.update(display, identificationTarget);
		return nextPeak;
	}
}
