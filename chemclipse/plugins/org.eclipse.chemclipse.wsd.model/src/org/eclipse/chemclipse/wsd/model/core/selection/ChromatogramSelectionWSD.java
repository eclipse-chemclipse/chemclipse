/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - refined the wavelength selection
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.selection.AbstractChromatogramSelection;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceRasteredWSD;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;

public class ChromatogramSelectionWSD extends AbstractChromatogramSelection implements IChromatogramSelectionWSD {

	private List<ITrace> selectedWavelengths = new ArrayList<>();

	public ChromatogramSelectionWSD(IChromatogramWSD chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	public ChromatogramSelectionWSD(IChromatogramWSD chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		/*
		 * Set all members to default values.<br/> This includes also to set a
		 * valid scan and if exists a valid peak.
		 */
		super(chromatogram);

		populateWavelengths(chromatogram);
		reset(fireUpdate);
	}

	@Override
	public void populateWavelengths(IChromatogramWSD chromatogram) {

		Optional<IScan> scan = chromatogram.getScans().stream().findFirst();
		if(!scan.isEmpty() && scan.get() instanceof IScanWSD scanWSD) {
			selectedWavelengths.clear();
			for(IScanSignalWSD signal : scanWSD.getScanSignals()) {
				selectedWavelengths.add(new TraceRasteredWSD(signal.getWavelength()));
			}
		}
	}

	@Override
	public IChromatogramWSD getChromatogram() {

		IChromatogram chromatogram = super.getChromatogram();
		if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
			return chromatogramWSD;
		}
		return null;
	}

	@Override
	public IScanWSD getSelectedScan() {

		if(super.getSelectedScan() instanceof IScanWSD scanWSD) {
			return scanWSD;
		}

		return null;
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
			 * Chromatogram WSD
			 */
			if(chromatogram instanceof IChromatogramWSD chromatogramWSD) {
				setSelectedScan(chromatogramWSD.getScan(1), fireUpdate);
			}
		} else {
			setSelectedScan(null, fireUpdate);
		}
		/*
		 * Fire an update.
		 */
		if(fireUpdate) {
			UpdateNotifier.update(this);
		}
	}

	@Override
	public void setSelectedScan(IScanWSD selectedScan) {

		/*
		 * FireUpdateChange will be called in the validate method.
		 */
		super.setSelectedScan(selectedScan, true);
	}

	@Override
	public void setSelectedScan(IScanWSD selectedScan, boolean update) {

		if(selectedScan != null) {
			setSelectedScan(selectedScan);
			/*
			 * Fire update change if necessary.
			 */
			if(update) {
				UpdateNotifier.update(this);
			}
		}
	}

	@Override
	public void fireUpdateChange(boolean forceReload) {

		UpdateNotifier.update(this);
	}

	@Override
	public void update(boolean forceReload) {

		super.update(forceReload);

		fireUpdateChange(forceReload);
	}

	@Override
	public List<ITrace> getSelectedWavelengths() {

		return selectedWavelengths;
	}

	@Override
	public void setSelectedWavelengths(List<ITrace> selectedWavelengths) {

		this.selectedWavelengths = selectedWavelengths;
	}
}
