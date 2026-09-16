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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.ui.keys.IBindingService;

public class TargetSelectionKeyHandler extends AbstractTriggerSequenceHandler {

	private ExtendedChromatogramUI extendedChromatogramUI;
	private AtomicReference<IIdentificationTarget> selectedTarget;
	private boolean next;

	public TargetSelectionKeyHandler(ExtendedChromatogramUI extendedChromatogramUI, AtomicReference<IIdentificationTarget> selectedTarget, IBindingService bindingService, String command, boolean next) {

		super(bindingService, command);
		this.extendedChromatogramUI = extendedChromatogramUI;
		this.selectedTarget = selectedTarget;
		this.next = next;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		IChromatogramSelection chromatogramSelection = extendedChromatogramUI.getChromatogramSelection();
		if(chromatogramSelection == null) {
			return;
		}

		IPeak peak = chromatogramSelection.getSelectedPeak();
		IScan scan = (peak != null) ? peak.getPeakModel().getPeakMaximum() : chromatogramSelection.getSelectedScan();
		if(scan == null) {
			return;
		}

		// undo before as it changes sorting
		if(selectedTarget.get() != null) {
			selectedTarget.get().setVerified(false);
		}

		Set<IIdentificationTarget> targets = (peak != null) ? peak.getTargets() : scan.getTargets();
		List<IIdentificationTarget> identificationTargets = IIdentificationTarget.getTargetsSorted(targets, scan.getRetentionIndex());
		if(identificationTargets.isEmpty()) {
			return;
		}

		int size = identificationTargets.size();
		int index = Math.max(identificationTargets.indexOf(selectedTarget.get()), 0);
		int nextIndex = next ? (index + 1) % size : (index - 1 + size) % size;
		IIdentificationTarget identificationTarget = identificationTargets.get(nextIndex);
		selectedTarget.set(identificationTarget);
		identificationTarget.setVerified(true);

		UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_IDENTIFICATION_TARGET_UPDATE, identificationTarget);
		UpdateNotifierUI.update(event.display, IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, "Verified target has been toggled.");
	}
}
