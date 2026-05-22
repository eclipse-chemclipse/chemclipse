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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Why does IChromatogram extends ({@link IChromatogramOverview})?<br/>
 * See the description in ({@link AbstractChromatogramMSD}).
 */
public interface IChromatogramMSD extends IChromatogram, IChromatogramPeaksMSD {

	/**
	 * Returns a new mass spectrum from the scan with the given scan number from
	 * the chromatogram.<br>
	 * The mass spectrum does not contain the ion specified in
	 * excludedIons.<br>
	 * To retrieve the mass spectrum, it must be
	 * deeply cloned and the given excluded ions must be removed.<br>
	 * You can test the retrieved mass spectrum with "instanceof" if it is a
	 * kind of ISupplierMassSpectrum. If no scan is available, null will be
	 * returned.
	 */
	IScanMSD getScan(int scan, IMarkedIons excludedIons);

	/**
	 * Returns a supplier scan mass spectrum or null, if no supplier
	 * scan mass spectrum is stored.
	 */
	@Override
	IScanMSD getScan(int scan);

	/**
	 * Returns the number of scanned ions.<br/>
	 * If a chromatogram contains for example 100 scans. These 100 scans contain
	 * each 300 ions, then a value of 100 * 300 = 30000 will be
	 * returned.
	 */
	int getNumberOfScanIons();

	/**
	 * If files are getting too big, a scan proxy shall be used
	 * which loads the ions on demand only.
	 * This method enforces to load all scan proxies.
	 */
	void enforceLoadScanProxies(IProgressMonitor monitor);

	/**
	 * Returns the minimal available ion abundance signal.<br/>
	 * This represents approximately the threshold value of the mass
	 * spectrometer.<br/>
	 * If no scans are stored, 0 will be returned.
	 */
	float getMinIonAbundance();

	/**
	 * Returns the maximal available ion abundance signal.<br/>
	 * If no scans are stored, 0 will be returned.
	 */
	float getMaxIonAbundance();

	/**
	 * Returns the lowest start ion (ion).<br/>
	 * Returns 0 if no scan is stored.
	 */
	double getStartIon();

	/**
	 * Returns the highest stop ion (ion).<br/>
	 * Returns 0 if no scan is stored.
	 */
	double getStopIon();

	/**
	 * Returns the ion transition settings.
	 * Used e.g. by triple quadrupol experiments.
	 */
	IIonTransitionSettings getIonTransitionSettings();

	/**
	 * Set the combined mass spectrum of the chromatogram.
	 */
	void setCombinedMassSpectrum(IScanMSD scanMSD);

	/**
	 * The combined mass spectrum is a summary of the scans/peaks of the chromatogram.
	 */
	IScanMSD getCombinedMassSpectrum();
}
