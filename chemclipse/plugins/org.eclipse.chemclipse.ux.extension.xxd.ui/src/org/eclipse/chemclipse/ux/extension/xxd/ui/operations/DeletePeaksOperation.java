/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.operations;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.swt.ui.notifier.UpdateNotifierUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

public class DeletePeaksOperation extends AbstractOperation {

	private IChromatogramSelection chromatogramSelection;
	private Display display;
	private List<IChromatogramPeak> peaksToDelete;

	public DeletePeaksOperation(Display display, IChromatogramSelection chromatogramSelection, List<IChromatogramPeak> peaksToDelete) {

		super(ExtensionMessages.deletePeaks);
		this.display = display;
		this.chromatogramSelection = chromatogramSelection;
		this.peaksToDelete = peaksToDelete;
	}

	@Override
	public boolean canExecute() {

		return !peaksToDelete.isEmpty() && chromatogramSelection != null;
	}

	@Override
	public boolean canRedo() {

		return !peaksToDelete.isEmpty();
	}

	@Override
	public boolean canUndo() {

		return !peaksToDelete.isEmpty();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		chromatogram.getPeaks().removeAll(peaksToDelete);
		chromatogram.setDirty(true);
		update(ExtensionMessages.peaksDeleted);
		return Status.OK_STATUS;
	}

	private void update(String message) {

		chromatogramSelection.setSelectedPeak(null);
		UpdateNotifierUI.update(display, chromatogramSelection);
		UpdateNotifierUI.update(display, IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION, message);
	}

	@Override
	public String getLabel() {

		return ExtensionMessages.deletePeaks;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		chromatogram.getPeaks().addAll(peaksToDelete);
		chromatogram.setDirty(true);
		update(ExtensionMessages.peaksUndeleted);
		return Status.OK_STATUS;
	}
}
