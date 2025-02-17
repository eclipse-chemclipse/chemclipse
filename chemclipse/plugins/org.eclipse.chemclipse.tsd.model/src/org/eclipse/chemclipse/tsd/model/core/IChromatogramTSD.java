/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.core;

import org.eclipse.chemclipse.model.core.IChromatogram;

public interface IChromatogramTSD extends IChromatogram {

	/**
	 * This could be null or a GC-MS, GCxGC, HPLC-DAD chromatogram.
	 * 
	 * @return {@link IChromatogram}
	 */
	IChromatogram getChromatogram();

	String getLabelAxisX();

	String getLabelAxisY();

	TypeTSD getTypeTSD();
}
