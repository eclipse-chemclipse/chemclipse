/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.fsd.model.core.selection;

import java.util.Optional;

import org.eclipse.chemclipse.fsd.model.core.IChromatogramFSD;
import org.eclipse.chemclipse.fsd.model.core.IScanFSD;
import org.eclipse.chemclipse.fsd.model.core.IScanSignalFSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.selection.AbstractChromatogramSelection;
import org.eclipse.chemclipse.model.wavelengths.IMarkedWavelengths;
import org.eclipse.chemclipse.model.wavelengths.MarkedWavelengths;

public class ChromatogramSelectionFSD extends AbstractChromatogramSelection implements IChromatogramSelectionFSD {

	private IScanFSD selectedScan;
	private IMarkedWavelengths selectedWavelengths;

	public ChromatogramSelectionFSD(IChromatogramFSD chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	public ChromatogramSelectionFSD(IChromatogramFSD chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		/*
		 * Set all members to default values.<br/> This includes also to set a
		 * valid scan and if exists a valid peak.
		 */
		super(chromatogram);

		populateWavelengths(chromatogram);
		reset(fireUpdate);
	}

	@Override
	public void populateWavelengths(IChromatogramFSD chromatogram) {

		Optional<IScan> scan = chromatogram.getScans().stream().findFirst();
		if(!scan.isEmpty() && scan.get() instanceof IScanFSD scanFSD) {
			selectedWavelengths = new MarkedWavelengths();
			for(IScanSignalFSD signal : scanFSD.getScanSignals()) {
				selectedWavelengths.add(signal.getWavelength());
			}
		}
	}

	@Override
	public void dispose() {

		selectedScan = null;
		super.dispose();
	}

	@Override
	public IChromatogramFSD getChromatogram() {

		IChromatogram chromatogram = super.getChromatogram();
		if(chromatogram instanceof IChromatogramFSD chromatogramFSD) {
			return chromatogramFSD;
		}
		return null;
	}

	@Override
	public IScanFSD getSelectedScan() {

		return selectedScan;
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
			 * Chromatogram FSD
			 */
			if(chromatogram instanceof IChromatogramFSD chromatogramFSD) {
				selectedScan = chromatogramFSD.getScan(1);
			}
		} else {
			selectedScan = null;
		}
		/*
		 * Fire an update.
		 */
		if(fireUpdate) {
			UpdateNotifier.update(this);
		}
	}

	@Override
	public void setSelectedScan(IScan selectedScan) {

		if(selectedScan instanceof IScanFSD scanFSD) {
			setSelectedScan(scanFSD);
		}
	}

	@Override
	public void setSelectedScan(IScan selectedScan, boolean update) {

		if(selectedScan instanceof IScanFSD scanFSD) {
			setSelectedScan(scanFSD, update);
		}
	}

	@Override
	public void setSelectedScan(IScanFSD selectedScan) {

		/*
		 * FireUpdateChange will be called in the validate method.
		 */
		setSelectedScan(selectedScan, true);
	}

	@Override
	public void setSelectedScan(IScanFSD selectedScan, boolean update) {

		if(selectedScan != null) {
			this.selectedScan = selectedScan;
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
		setSelectedScan(selectedScan, false);
		fireUpdateChange(forceReload);
	}
}
