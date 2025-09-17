/*******************************************************************************
 * Copyright (c) 2022, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignals;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DenoiseOperation extends AbstractOperation {

	private static final Logger logger = Logger.getLogger(DenoiseOperation.class);

	private IChromatogramSelection chromatogramSelection;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignals previousExtractedIonSignals;

	public DenoiseOperation(IChromatogramSelection chromatogramSelection, IExtractedIonSignals extractedIonSignals) {

		super("Denoise");
		this.chromatogramSelection = chromatogramSelection;
		this.extractedIonSignals = extractedIonSignals;
	}

	@Override
	public boolean canExecute() {

		return chromatogramSelection != null && chromatogramSelection.getChromatogram() instanceof IChromatogramMSD && extractedIonSignals.size() > 0;
	}

	@Override
	public boolean canRedo() {

		return extractedIonSignals.size() > 0;
	}

	@Override
	public boolean canUndo() {

		return previousExtractedIonSignals.size() > 0;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		IChromatogramMSD chromatogram = (IChromatogramMSD)chromatogramSelection.getChromatogram();
		int startScan = extractedIonSignals.getStartScan();
		int stopScan = extractedIonSignals.getStopScan();
		previousExtractedIonSignals = new ExtractedIonSignals(startScan, stopScan);
		/*
		 * Write the values from the extracted ion signals back to the chromatogram
		 * whilst doing a backup.
		 */
		for(int scan = startScan; scan <= stopScan; scan++) {
			try {
				IExtractedIonSignal extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
				IScanMSD supplierMassSpectrum = chromatogram.getScan(scan);
				previousExtractedIonSignals.add(supplierMassSpectrum.getExtractedIonSignal());
				replaceIons(extractedIonSignal, supplierMassSpectrum);
			} catch(NoExtractedIonSignalStoredException e) {
				logger.warn(e);
			}
		}
		updateChromatogramSelection();
		return Status.OK_STATUS;
	}

	@Override
	public String getLabel() {

		return "Denoise";
	}

	private void updateChromatogramSelection() {

		chromatogramSelection.update(true);
		chromatogramSelection.getChromatogram().setDirty(true);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		IChromatogramMSD chromatogram = (IChromatogramMSD)chromatogramSelection.getChromatogram();
		int startScan = previousExtractedIonSignals.getStartScan();
		int stopScan = previousExtractedIonSignals.getStopScan();
		for(int scan = startScan; scan <= stopScan; scan++) {
			try {
				IExtractedIonSignal extractedIonSignal = previousExtractedIonSignals.getExtractedIonSignal(scan);
				IScanMSD supplierMassSpectrum = chromatogram.getScan(scan);
				replaceIons(extractedIonSignal, supplierMassSpectrum);
			} catch(NoExtractedIonSignalStoredException e) {
				logger.warn(e);
			}
		}
		updateChromatogramSelection();
		return Status.OK_STATUS;
	}

	/**
	 * Replaces all ions in the supplier mass spectrum by the mass
	 * fragments stored in the extracted ion signal.
	 * 
	 * @param extractedIonSignal
	 * @param massSpectrum
	 */
	private static void replaceIons(IExtractedIonSignal extractedIonSignal, IScanMSD massSpectrum) {

		int startIon = extractedIonSignal.getStartIon();
		int stopIon = extractedIonSignal.getStopIon();
		float abundance;
		/*
		 * Remove all ions.
		 */
		massSpectrum.removeAllIons();
		IIon defaultIon;
		/*
		 * Add the new ion values if abundance > 0.0f.
		 */
		for(int ion = startIon; ion <= stopIon; ion++) {
			abundance = extractedIonSignal.getAbundance(ion);
			if(abundance > 0.0f) {
				defaultIon = new Ion(ion, abundance);
				massSpectrum.addIon(defaultIon);
			}
		}
	}
}
