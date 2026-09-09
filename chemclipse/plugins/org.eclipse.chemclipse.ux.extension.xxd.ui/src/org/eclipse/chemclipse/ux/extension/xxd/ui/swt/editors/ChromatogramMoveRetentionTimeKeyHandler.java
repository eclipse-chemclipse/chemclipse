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

import org.eclipse.chemclipse.model.selection.ChromatogramSelectionSupport;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.selection.MoveDirection;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.ui.keys.IBindingService;

public class ChromatogramMoveRetentionTimeKeyHandler extends AbstractTriggerSequenceHandler {

	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private ExtendedChromatogramUI extendedChromatogramUI;
	private boolean right;

	public ChromatogramMoveRetentionTimeKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, IBindingService bindingService, String command, boolean right) {

		super(bindingService, command);
		this.extendedChromatogramUI = extendedChromatogramUI;
		this.right = right;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		handleArrowMoveWindowSelection();
	}

	private void handleArrowMoveWindowSelection() {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection != null) {
			boolean useAlternateWindowMoveDirection = preferenceStore.getBoolean(PreferenceSupplier.P_ALTERNATE_WINDOW_MOVE_DIRECTION);
			if(right) {
				MoveDirection moveDirection = (useAlternateWindowMoveDirection) ? MoveDirection.LEFT : MoveDirection.RIGHT;
				ChromatogramSelectionSupport.moveRetentionTimeWindow(chromatogramSelection, moveDirection, 20);
			} else {
				MoveDirection moveDirection = (useAlternateWindowMoveDirection) ? MoveDirection.RIGHT : MoveDirection.LEFT;
				ChromatogramSelectionSupport.moveRetentionTimeWindow(chromatogramSelection, moveDirection, 20);
			}
			extendedChromatogramUI.updateSelection();
		}
	}
}
