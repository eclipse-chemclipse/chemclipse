/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt.editors;

import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.selection.ChromatogramSelectionSupport;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.selection.MoveDirection;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;
import org.eclipse.swtchart.extensions.events.AbstractHandledEventProcessor;

public class ScanSelectionArrowKeyHandler extends AbstractHandledEventProcessor {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private int keyCode;

	public ScanSelectionArrowKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, int keyCode) {

		this.extendedChromatogramUI = extendedChromatogramUI;
		this.keyCode = keyCode;
	}

	@Override
	public int getEvent() {

		return IKeyboardSupport.EVENT_KEY_UP;
	}

	@Override
	public int getButton() {

		return keyCode;
	}

	@Override
	public int getStateMask() {

		return SWT.MOD1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		handleControlScanSelection(event.display, keyCode);
	}

	protected void handleControlScanSelection(Display display, int keyCode) {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null) {
			/*
			 * Select the next or previous scan.
			 */
			int scanNumber = chromatogramSelection.getSelectedScan().getScanNumber();
			if(keyCode == SWT.ARROW_RIGHT) {
				scanNumber++;
			} else {
				scanNumber--;
			}
			/*
			 * Set and fire an update.
			 */
			IScan selectedScan = chromatogramSelection.getChromatogram().getScan(scanNumber);
			UpdateNotifierUI.update(display, selectedScan);

			if(selectedScan != null) {
				/*
				 * The selection should slide with the selected scans.
				 */
				int scanRetentionTime = selectedScan.getRetentionTime();
				int startRetentionTime = chromatogramSelection.getStartRetentionTime();
				int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
				/*
				 * Left or right slide on demand.
				 */
				if(scanRetentionTime <= startRetentionTime) {
					ChromatogramSelectionSupport.moveRetentionTimeWindow(chromatogramSelection, MoveDirection.LEFT, 5);
				} else if(scanRetentionTime >= stopRetentionTime) {
					ChromatogramSelectionSupport.moveRetentionTimeWindow(chromatogramSelection, MoveDirection.RIGHT, 5);
				}

				chromatogramSelection.setSelectedScan(selectedScan, false);
				extendedChromatogramUI.updateSelection();
			}
		}
	}
}
