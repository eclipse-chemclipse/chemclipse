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

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.ui.keys.IBindingService;

public class ChromatogramMoveAbundanceKeyHandler extends AbstractTriggerSequenceHandler {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private boolean up;

	public ChromatogramMoveAbundanceKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, IBindingService bindingService, String command, boolean up) {

		super(bindingService, command);
		this.extendedChromatogramUI = extendedChromatogramUI;
		this.up = up;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		handleArrowMoveWindowSelection();
	}

	private void handleArrowMoveWindowSelection() {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null) {
			/*
			 * Doesn't work if auto adjust signals is enabled.
			 */
			float stopAbundance = chromatogramSelection.getStopAbundance();
			float newStopAbundance;
			if(PreferenceSupplier.useAlternateWindowMoveDirection()) {
				newStopAbundance = up ? stopAbundance - stopAbundance / 20.0f : stopAbundance + stopAbundance / 20.0f;
			} else {
				newStopAbundance = up ? stopAbundance + stopAbundance / 20.0f : stopAbundance - stopAbundance / 20.0f;
			}

			int startRetentionTime = chromatogramSelection.getStartRetentionTime();
			int stopRetentionTime = chromatogramSelection.getStopRetentionTime();
			float startAbundance = chromatogramSelection.getStartAbundance();
			extendedChromatogramUI.setChromatogramSelectionRange(startRetentionTime, stopRetentionTime, startAbundance, newStopAbundance);
			extendedChromatogramUI.updateSelection();
		}
	}
}
