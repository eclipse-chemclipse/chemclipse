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
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

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
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.ui.keys.IBindingService;

public class PeakSelectionArrowKeyHandler extends AbstractTriggerSequenceHandler {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private boolean next;

	public PeakSelectionArrowKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, IBindingService bindingService, String command, boolean next) {

		super(bindingService, command);
		this.extendedChromatogramUI = extendedChromatogramUI;
		this.next = next;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection == null) {
			return;
		}
		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		List<? extends IChromatogramPeak> peaks = chromatogram.getPeaks();
		if(peaks.isEmpty()) {
			return;
		}
		peaks.sort(new PeakRetentionTimeComparator());
		int index = peaks.indexOf(chromatogramSelection.getSelectedPeak());
		if(index == -1) {
			return;
		}
		int nextIndex;
		if(next) {
			nextIndex = (index < peaks.size() - 1) ? index + 1 : 0;
		} else {
			nextIndex = (index > 0) ? index - 1 : peaks.size() - 1;
		}
		IPeak nextPeak = peaks.get(nextIndex);
		chromatogramSelection.setSelectedPeak(nextPeak);
		extendedChromatogramUI.updateSelectedPeak();
		boolean moveRetentionTimeOnPeakSelection = preferenceStore.getBoolean(PreferenceSupplier.P_MOVE_RETENTION_TIME_ON_PEAK_SELECTION);
		if(moveRetentionTimeOnPeakSelection) {
			ChromatogramDataSupport.adjustChromatogramSelection(nextPeak, chromatogramSelection);
		}
		extendedChromatogramUI.updateSelection();
		UpdateNotifierUI.update(event.display, nextPeak);
		IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(nextPeak);
		UpdateNotifierUI.update(event.display, identificationTarget);
	}
}
