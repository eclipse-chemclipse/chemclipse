/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.splash;

import java.util.Comparator;

import org.eclipse.chemclipse.msd.model.core.IIon;

public class IntensityThenMassComparator implements Comparator<IIon> {

	@Override
	public int compare(IIon i1, IIon i2) {

		int result = Double.compare(i2.getAbundance(), i1.getAbundance());
		if(result == 0) {
			result = Double.compare(i1.getIon(), i2.getIon());
		}
		return result;
	}
}
