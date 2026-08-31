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
package org.eclipse.chemclipse.msd.model.operations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.notifier.MassSpectrumUpdateNotifier;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DeleteMassSpectrumPeaksOperation extends AbstractOperation {

	private IStandaloneMassSpectrum massSpectrum;
	private List<IMassSpectrumPeak> peaksToDelete;

	public DeleteMassSpectrumPeaksOperation(IStandaloneMassSpectrum massSpectrum, List<IMassSpectrumPeak> peaksToDelete) {

		super("Delete Peaks");
		this.massSpectrum = massSpectrum;
		this.peaksToDelete = new ArrayList<>(peaksToDelete);
	}

	@Override
	public boolean canExecute() {

		return !peaksToDelete.isEmpty() && massSpectrum != null;
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

		massSpectrum.getPeaks().removeAll(peaksToDelete);
		updateMassSpectrum();
		return Status.OK_STATUS;
	}

	@Override
	public String getLabel() {

		return "Delete Peaks";
	}

	private void updateMassSpectrum() {

		massSpectrum.setDirty(true);
		MassSpectrumUpdateNotifier.update(massSpectrum);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		massSpectrum.getPeaks().addAll(peaksToDelete);
		updateMassSpectrum();
		return Status.OK_STATUS;
	}
}
