/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparator;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;

public class CosineBinaryComparator extends CosineComparator implements IMassSpectrumComparator {

	@Override
	protected double getVectorValue(IExtractedIonSignal signal, int i) {

		return signal.getAbundance(i) > 0 ? 1 : 0;
	}
};