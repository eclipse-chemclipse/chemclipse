/*******************************************************************************
 * Copyright (c) 2011, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.settings;

import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;

public interface ISupplierFilterSettings extends IChromatogramFilterSettings {

	/**
	 * Returns the coda threshold value.
	 * 
	 * @return float
	 */
	float getCodaThreshold();

	/**
	 * Sets the coda threshold value.<br/>
	 * The value must be in between the range of MIN_CODA_THRESHOLD 0.0f (low
	 * quality) and MAX_CODA_THRESHOLD 1.0f (high quality).
	 * 
	 * @param codaThreshold
	 */
	void setCodaThreshold(float codaThreshold);
}
