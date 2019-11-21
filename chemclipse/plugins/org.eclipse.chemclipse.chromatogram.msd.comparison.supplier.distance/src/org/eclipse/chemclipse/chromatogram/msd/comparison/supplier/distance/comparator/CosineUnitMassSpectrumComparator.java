/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.IMassSpectrumComparator;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;

public class CosineUnitMassSpectrumComparator extends CosineMassSpectrumComparator implements IMassSpectrumComparator {

	@Override
	protected double getVectorValue(IExtractedIonSignal signal, int i) {

		double signalAbundance = signal.getAbundance(i);
		return signalAbundance > 0 ? 1 : 0;
	}
};