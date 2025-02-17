/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramPeak;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.support.PeakSupport;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.events.AbstractHandledEventProcessor;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;

public class PeakSelectionHandler extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	public PeakSelectionHandler(ExtendedChromatogramUI extendedChromatogramUI) {

		this.extendedChromatogramUI = extendedChromatogramUI;
	}

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_DOUBLE_CLICK;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_LEFT;
	}

	@Override
	public int getStateMask() {

		return SWT.MOD3;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null) {
			IChromatogram chromatogram = chromatogramSelection.getChromatogram();
			int retentionTime = (int)baseChart.getSelectedPrimaryAxisValue(event.x, IExtendedChart.X_AXIS);
			int retentionTimeDelta = preferenceStore.getInt(PreferenceSupplier.P_DELTA_MILLISECONDS_PEAK_SELECTION);
			int startRetentionTime = retentionTime - retentionTimeDelta;
			int stopRetentiontime = retentionTime + retentionTimeDelta;
			List<? extends IChromatogramPeak> peaks = chromatogram.getPeaks(startRetentionTime, stopRetentiontime);
			if(peaks != null && !peaks.isEmpty()) {
				/*
				 * Fire an update.
				 */
				IPeak peak = PeakSupport.selectNearestPeak(peaks, retentionTime);
				if(peak != null) {

					chromatogramSelection.setSelectedPeak(peak);
					extendedChromatogramUI.updateSelectedPeak();

					boolean moveRetentionTimeOnPeakSelection = preferenceStore.getBoolean(PreferenceSupplier.P_MOVE_RETENTION_TIME_ON_PEAK_SELECTION);
					if(moveRetentionTimeOnPeakSelection) {
						ChromatogramDataSupport.adjustChromatogramSelection(peak, chromatogramSelection);
					}

					extendedChromatogramUI.updateSelection();

					UpdateNotifierUI.update(event.display, peak);
					IIdentificationTarget identificationTarget = IIdentificationTarget.getIdentificationTarget(peak);
					UpdateNotifierUI.update(event.display, identificationTarget);

					showClickbindingHelp(baseChart, "Peak Selection", "Select nearest peak.");
				}
			}
		}
	}
}
