/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.splitter.model;

import org.eclipse.chemclipse.msd.model.core.AbstractRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.Ion;

public class VendorScan extends AbstractRegularMassSpectrum {

	private static final long serialVersionUID = 4479075020372518951L;

	@Override
	public IRegularMassSpectrum makeDeepCopy() throws CloneNotSupportedException {

		IRegularMassSpectrum massSpectrum = (IRegularMassSpectrum)super.clone();
		IIon clonedIon;

		for(IIon ion : getIons()) {
			clonedIon = new Ion(ion.getIon(), ion.getAbundance());
			massSpectrum.addIon(clonedIon);
		}
		return massSpectrum;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return makeDeepCopy();
	}
}