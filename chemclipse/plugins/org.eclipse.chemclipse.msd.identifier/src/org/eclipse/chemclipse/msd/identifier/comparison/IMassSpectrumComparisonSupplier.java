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
package org.eclipse.chemclipse.msd.identifier.comparison;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;

public interface IMassSpectrumComparisonSupplier {

	/**
	 * The id of the extension point: e.g.
	 * (org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.xyz)
	 * 
	 * @return String
	 */
	String getId();

	/**
	 * A short description of the functionality of the extension point.
	 * 
	 * @return String
	 */
	String getDescription();

	/**
	 * The comparator name that can be shown in a list box dialogue.
	 * 
	 * @return String
	 */
	String getComparatorName();

	boolean supportsNominalMS();

	boolean supportsTandemMS();

	boolean supportsHighResolutionMS();

	default boolean supports(IScanMSD massSpectrum) {

		// we don't know yet
		if(massSpectrum == null) {
			return true;
		}
		if(massSpectrum.isTandemMS() && !supportsTandemMS()) {
			return false;
		}
		return massSpectrum.isHighResolutionMS() ? supportsHighResolutionMS() : supportsNominalMS();
	}
}
