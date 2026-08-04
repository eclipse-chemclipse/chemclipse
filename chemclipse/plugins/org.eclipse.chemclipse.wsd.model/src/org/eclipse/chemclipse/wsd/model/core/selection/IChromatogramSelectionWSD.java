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
 * Alexander Kerner - Generics
 * Matthias Mailänder - set/populate wavelengths
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core.selection;

import java.util.List;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;

public interface IChromatogramSelectionWSD extends IChromatogramSelection {

	/**
	 * Returns the stored chromatogram.
	 * May return null.
	 *
	 * @return {@link IChromatogramWSD}
	 */
	@Override
	IChromatogramWSD getChromatogram();

	/**
	 * Returns the selected scan of the current chromatogram or null, if none is
	 * stored.
	 *
	 * @return {@link IScanWSD}
	 */
	@Override
	IScanWSD getSelectedScan();

	/**
	 * Sets the selected scan of the current chromatogram.<br/>
	 * The scan must not be null.
	 */
	void setSelectedScan(IScanWSD selectedScan);

	/**
	 * Use this convenient method, if you don't want to fire and update.
	 *
	 * @param selectedScan
	 * @param update
	 */
	void setSelectedScan(IScanWSD selectedScan, boolean update);

	/**
	 * Returns a list of selected wavelength.
	 *
	 * @return Wavelengths
	 */
	List<ITrace> getSelectedWavelengths();

	void setSelectedWavelengths(List<ITrace> selectedWavelengths);

	/**
	 * Populate the list with wavelengths from the first scan of the given chromatogram.
	 *
	 * @param chromatogram
	 */
	void populateWavelengths(IChromatogramWSD chromatogram);
}