/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - reset selected peak in case of deletion
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core.selection;

import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.selection.AbstractChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIonTransitions;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIonTransitions;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;

/**
 * You can define within a {@link ChromatogramSelectionMSD} instance a certain view
 * on a chromatogram.<br/>
 * This is appropriate if you like to apply a filter to a specific chromatogram
 * area.<br/>
 * Start and stop scan are not provided as they can be calculated by the
 * retention time.
 */
public class ChromatogramSelectionMSD extends AbstractChromatogramSelection<IChromatogramPeakMSD, IChromatogramMSD> implements IChromatogramSelectionMSD {

	private IRegularMassSpectrum selectedScan;
	private IMarkedIons selectedIons;
	private IMarkedIons excludedIons;
	private IMarkedIonTransitions markedIonTransitions;

	public ChromatogramSelectionMSD(IChromatogramMSD chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	public ChromatogramSelectionMSD(IChromatogramMSD chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		super(chromatogram);
		/*
		 * Create instances of selected and excluded ions.
		 */
		selectedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		excludedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
		/*
		 * Marked ion transitions.
		 */
		if(chromatogram instanceof IChromatogramMSD) {
			IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
			if(ionTransitionSettings == null) {
				markedIonTransitions = new MarkedIonTransitions();
			} else {
				markedIonTransitions = new MarkedIonTransitions(ionTransitionSettings.getIonTransitions());
			}
		} else {
			markedIonTransitions = new MarkedIonTransitions();
		}
		/*
		 * Set all members to default values.<br/> This includes also to set a
		 * valid scan and if exists a valid peak.
		 */
		reset(fireUpdate);
	}

	@Override
	public void dispose() {

		super.dispose();
		selectedScan = null;
		selectedIons = null;
		excludedIons = null;
	}

	@Override
	public IChromatogramMSD getChromatogram() {

		IChromatogram<?> chromatogram = super.getChromatogram();
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			return chromatogramMSD;
		}
		return null;
	}

	@Override
	public IRegularMassSpectrum getSelectedScan() {

		return selectedScan;
	}

	@Override
	public IMarkedIons getExcludedIons() {

		return excludedIons;
	}

	@Override
	public IMarkedIons getSelectedIons() {

		return selectedIons;
	}

	@Override
	public IMarkedIonTransitions getMarkedIonTransitions() {

		return markedIonTransitions;
	}

	@Override
	public void reset() {

		reset(true);
	}

	@Override
	public void reset(boolean fireUpdate) {

		super.reset(fireUpdate);
		IChromatogram<?> chromatogram = getChromatogram();
		/*
		 * Scan
		 */
		if(chromatogram.getNumberOfScans() >= 1) {
			/*
			 * Chromatogram MSD
			 */
			if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
				selectedScan = chromatogramMSD.getSupplierScan(1);
			}
		} else {
			selectedScan = null;
		}
		/*
		 * Selected Identified Scan
		 */
		setSelectedIdentifiedScan(null);
		/*
		 * Peak
		 */
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			List<IChromatogramPeakMSD> peaks = chromatogramMSD.getPeaks();
			if(peaks != null && !peaks.isEmpty()) {
				setSelectedPeak(peaks.get(0));
			} else {
				setSelectedPeak(null);
			}
		}
		/*
		 * Fire an update.
		 */
		if(fireUpdate) {
			fireUpdateChange(false);
		}
	}

	@Override
	public void setSelectedScan(IScan selectedScan) {

		if(selectedScan instanceof IRegularMassSpectrum vendorMassSpectrum) {
			setSelectedScan(vendorMassSpectrum);
		}
	}

	@Override
	public void setSelectedScan(IScan selectedScan, boolean update) {

		if(selectedScan instanceof IRegularMassSpectrum vendorMassSpectrum) {
			setSelectedScan(vendorMassSpectrum, update);
		}
	}

	@Override
	public void setSelectedScan(IRegularMassSpectrum selectedScan) {

		/*
		 * FireUpdateChange will be called in the validate method.
		 */
		setSelectedScan(selectedScan, true);
	}

	@Override
	public void setSelectedScan(IRegularMassSpectrum selectedScan, boolean update) {

		if(selectedScan != null) {
			this.selectedScan = selectedScan;
			/*
			 * Fire update change if neccessary.
			 */
			if(update) {
				fireUpdateChange(false);
			}
		}
	}

	@Override
	public void fireUpdateChange(boolean forceReload) {

		try {
			UpdateNotifier.update(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(boolean forceReload) {

		super.update(forceReload);
		//
		setSelectedScan(selectedScan, false);
		//
		fireUpdateChange(forceReload);
	}
}
