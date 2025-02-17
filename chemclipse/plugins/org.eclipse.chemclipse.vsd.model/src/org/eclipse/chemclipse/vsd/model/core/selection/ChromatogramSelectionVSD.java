/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.model.core.selection;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.notifier.UpdateNotifier;
import org.eclipse.chemclipse.model.selection.AbstractChromatogramSelection;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;

public class ChromatogramSelectionVSD extends AbstractChromatogramSelection implements IChromatogramSelectionVSD {

	private IScanVSD selectedScan;

	public ChromatogramSelectionVSD(IChromatogramVSD chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	public ChromatogramSelectionVSD(IChromatogramVSD chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		super(chromatogram);
		reset(fireUpdate);
	}

	@Override
	public void dispose() {

		selectedScan = null;
		super.dispose();
	}

	@Override
	public IScanVSD getSelectedScan() {

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
			if(chromatogram instanceof IChromatogramVSD chromatogramISD) {
				selectedScan = (IScanVSD)chromatogramISD.getScan(1);
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

		if(selectedScan instanceof IScanVSD scanISD) {
			setSelectedScan(scanISD, true);
		}
	}

	@Override
	public void setSelectedScan(IScan selectedScan, boolean update) {

		if(selectedScan instanceof IScanVSD scanISD) {
			/*
			 * Fire update change if neccessary.
			 */
			this.selectedScan = scanISD;
			if(update) {
				fireUpdateChange(false);
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

	@Override
	public IChromatogramVSD getChromatogram() {

		IChromatogram chromatogram = super.getChromatogram();
		if(chromatogram instanceof IChromatogramVSD chromatogramVSD) {
			return chromatogramVSD;
		}
		return null;
	}
}
