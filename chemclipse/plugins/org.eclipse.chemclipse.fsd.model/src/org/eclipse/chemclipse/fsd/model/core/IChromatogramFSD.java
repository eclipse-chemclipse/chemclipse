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
 * Matthias Mailänder - initial API
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core;

import java.util.Set;

import org.eclipse.chemclipse.model.core.IChromatogram;

public interface IChromatogramFSD extends IChromatogram, IChromatogramBaselineFSD, IChromatogramPeaksFSD {

	/**
	 * Returns a supplier scan or null, if no supplier
	 * spectrum is stored.
	 * 
	 * @param scan
	 * @return {@link IScanFSD}
	 */
	@Override
	IScanFSD getScan(int scan);

	/**
	 * 
	 * @return all wavelengths in scan
	 */
	Set<Float> getWavelengths();
}
