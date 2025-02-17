/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Matthias Mailänder - set/populate wavelengths
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.model.core.selection;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.chemclipse.wsd.model.core.support.IMarkedWavelengths;

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
	IMarkedWavelengths getSelectedWavelengths();

	void setSelectedWavelengths(IMarkedWavelengths selectedWavelengths);

	/**
	 * Populate the list with wavelengths from the first scan of the given chromatogram.
	 *
	 * @param chromatogram
	 */
	void populateWavelengths(IChromatogramWSD chromatogram);
}
