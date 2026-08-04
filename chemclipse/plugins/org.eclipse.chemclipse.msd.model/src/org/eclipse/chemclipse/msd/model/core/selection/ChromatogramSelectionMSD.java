/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - reset selected peak in case of deletion
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.selection.AbstractChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IIonTransitionSettings;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIonTransitions;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIonTransitions;
import org.eclipse.chemclipse.support.traces.ITrace;

/**
 * You can define within a {@link ChromatogramSelectionMSD} instance a certain view
 * on a chromatogram.<br/>
 * This is appropriate if you like to apply a filter to a specific chromatogram
 * area.<br/>
 * Start and stop scan are not provided as they can be calculated by the
 * retention time.
 */
public class ChromatogramSelectionMSD extends AbstractChromatogramSelection implements IChromatogramSelectionMSD {

	private List<ITrace> selectedIons;
	private List<ITrace> excludedIons;
	private IMarkedIonTransitions markedIonTransitions;

	private static final Logger logger = Logger.getLogger(ChromatogramSelectionMSD.class);

	public ChromatogramSelectionMSD(IChromatogramMSD chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	public ChromatogramSelectionMSD(IChromatogramMSD chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		super(chromatogram);
		/*
		 * Create instances of selected and excluded ions.
		 */
		selectedIons = new ArrayList<>();
		excludedIons = new ArrayList<>();
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
		selectedIons = null;
		excludedIons = null;
	}

	@Override
	public IChromatogramMSD getChromatogram() {

		IChromatogram chromatogram = super.getChromatogram();
		if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
			return chromatogramMSD;
		}
		return null;
	}

	@Override
	public IScanMSD getSelectedScan() {

		if(super.getSelectedScan() instanceof IScanMSD scanMSD) {
			return scanMSD;
		}

		return null;
	}

	@Override
	public List<ITrace> getExcludedIons() {

		return excludedIons;
	}

	@Override
	public List<ITrace> getSelectedIons() {

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
		IChromatogram chromatogram = getChromatogram();
		/*
		 * Scan
		 */
		if(chromatogram.getNumberOfScans() >= 1) {
			/*
			 * Chromatogram MSD
			 */
			if(chromatogram instanceof IChromatogramMSD chromatogramMSD) {
				setSelectedScan(chromatogramMSD.getScan(1), fireUpdate);
			}
		} else {
			setSelectedScan(null, fireUpdate);
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
	public void setSelectedScan(IScanMSD selectedScan) {

		/*
		 * FireUpdateChange will be called in the validate method.
		 */
		super.setSelectedScan(selectedScan, true);
	}

	@Override
	public void setSelectedScan(IRegularMassSpectrum selectedScan, boolean update) {

		if(selectedScan != null) {
			setSelectedScan(selectedScan);
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
			logger.error(e);
		}
	}

	@Override
	public void update(boolean forceReload) {

		super.update(forceReload);

		fireUpdateChange(forceReload);
	}
}