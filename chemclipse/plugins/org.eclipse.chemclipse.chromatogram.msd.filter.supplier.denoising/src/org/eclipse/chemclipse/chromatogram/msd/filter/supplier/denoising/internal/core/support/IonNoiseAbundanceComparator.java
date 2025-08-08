/*******************************************************************************
 * Copyright (c) 2016, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import java.util.Comparator;

import org.eclipse.chemclipse.support.comparator.SortOrder;

public class IonNoiseAbundanceComparator implements Comparator<IonNoise> {

	private SortOrder sortOrder;

	/**
	 * The sort order is per default value ascending.
	 */
	public IonNoiseAbundanceComparator() {

		sortOrder = SortOrder.ASC;
	}

	/**
	 * You can choose whether to sort the abundance values ascending (asc) or
	 * descending (desc).
	 * 
	 * @param sortOrder
	 */
	public IonNoiseAbundanceComparator(SortOrder sortOrder) {

		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(IonNoise ion1, IonNoise ion2) {

		int returnValue;
		switch(sortOrder) {
			case ASC:
				returnValue = Float.compare(ion1.getAbundance(), ion2.getAbundance());
				break;
			case DESC:
				returnValue = Float.compare(ion2.getAbundance(), ion1.getAbundance());
				break;
			default:
				returnValue = Float.compare(ion1.getAbundance(), ion2.getAbundance());
				break;
		}
		return returnValue;
	}
}
