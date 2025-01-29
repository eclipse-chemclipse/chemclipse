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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core.selection;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIonTransitions;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;

/**
 * Get the selected chromatogram values. The {@link IChromatogramSelectionMSD} represents a selected part of a chromatogram, e.g. to integrate only between
 * a retention time of 10 - 15 minutes.<br/>
 * The selection can also be used to declare a part of a chromatogram, where a
 * filter should be applied.<br/>
 * Start and stop scan are not provided as they can be calculated by the
 * retention time.<br/>
 */
public interface IChromatogramSelectionMSD extends IChromatogramSelection<IChromatogramPeakMSD, IChromatogramMSD> {

	/**
	 * Returns the stored chromatogram.
	 * May return null.
	 *
	 * @return {@link IChromatogramMSD}
	 */
	@Override
	IChromatogramMSD getChromatogram();

	/**
	 * Returns the selected scan of the current chromatogram or null, if none is
	 * stored.
	 *
	 * @return {@link IRegularMassSpectrum}
	 */
	@Override
	IRegularMassSpectrum getSelectedScan();

	/**
	 * Sets the selected scan of the current chromatogram.<br/>
	 * The scan must not be null.
	 */
	void setSelectedScan(IRegularMassSpectrum selectedScan);

	/**
	 * Use this convenient method, if you don't want to fire and update.
	 *
	 * @param selectedScan
	 * @param update
	 */
	void setSelectedScan(IRegularMassSpectrum selectedScan, boolean update);

	/**
	 * Returns a list of selected ions.
	 *
	 * @return IMarkedIons
	 */
	IMarkedIons getSelectedIons();

	/**
	 * Returns a list of excluded ions.
	 *
	 * @return IMarkedIons
	 */
	IMarkedIons getExcludedIons();

	/**
	 * Returns the instance of selected ion transitions.
	 *
	 * @return {@link IMarkedIonTransitions}
	 */
	IMarkedIonTransitions getMarkedIonTransitions();
}
