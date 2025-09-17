/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.core;

import java.util.Set;

import org.eclipse.chemclipse.model.core.IChromatogram;

public interface IChromatogramWSD extends IChromatogram, IChromatogramWSDBaseline, IChromatogramPeaksWSD {

	/**
	 * Returns a scan or null, if no wave
	 * spectrum is stored.
	 * 
	 * @param scan
	 * @return {@link IScanWSD}
	 */
	@Override
	IScanWSD getScan(int scan);

	/**
	 * 
	 * @return all wavelengths in scan
	 */
	Set<Float> getWavelengths();
}
