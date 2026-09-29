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

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakSelectionSupport;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.ui.keys.IBindingService;

public class PeakSelectionArrowKeyHandler extends AbstractTriggerSequenceHandler {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private boolean next;

	public PeakSelectionArrowKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, IBindingService bindingService, String command, boolean next) {

		super(bindingService, command);
		this.extendedChromatogramUI = extendedChromatogramUI;
		this.next = next;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(PeakSelectionSupport.selectNeighborPeak(event.display, chromatogramSelection, next) != null) {
			extendedChromatogramUI.updateSelectedPeak();
			extendedChromatogramUI.updateSelection();
		}
	}
}
