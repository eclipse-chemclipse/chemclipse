/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.filter.peaks.operations;

import java.util.List;

import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.csd.model.core.IChromatogramPeakCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DeletePeaksOperation extends AbstractOperation {

	private IChromatogramSelection chromatogramSelection;
	private List<IPeak> peaksToDelete;

	public DeletePeaksOperation(IChromatogramSelection chromatogramSelection, List<IPeak> peaksToDelete) {

		super("Delete Peaks");
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
		updateChromatogramSelection();
		return Status.OK_STATUS;
	}

	@Override
	public String getLabel() {

		return "Delete Peaks";
	}

	private void updateChromatogramSelection() {

		chromatogramSelection.setSelectedPeak(null);
		chromatogramSelection.update(true);
		chromatogramSelection.getChromatogram().setDirty(true);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		IChromatogram chromatogram = chromatogramSelection.getChromatogram();
		if(chromatogram instanceof IChromatogramCSD chromatogramCSD) {
			for(IPeak peak : peaksToDelete) {
				if(peak instanceof IChromatogramPeakCSD chromatogramPeakCSD) {
					chromatogramCSD.getPeaks().add(chromatogramPeakCSD);
				}
			}
		}
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			for(IPeak peak : peaksToDelete) {
				if(peak instanceof IChromatogramPeakMSD chromatogramPeakMSD) {
					chromatogramMSD.getPeaks().add(chromatogramPeakMSD);
				}
			}
		}
		if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
			for(IPeak peak : peaksToDelete) {
				if(peak instanceof IChromatogramPeakWSD chromatogramPeakWSD) {
					chromatogramWSD.getPeaks().add(chromatogramPeakWSD);
				}
			}
		}
		updateChromatogramSelection();
		return Status.OK_STATUS;
	}
}
