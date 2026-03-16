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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.msd.model.core.AbstractStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;

public class StandaloneMassSpectrum extends AbstractStandaloneMassSpectrum {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 7540947309609765368L;

	/**
	 * Keep in mind, it is a covariant return.<br/>
	 * IMassSpectrum is needed. ISupplierMassSpectrum is a subtype of
	 * IMassSpectrum.
	 */
	@Override
	public IRegularMassSpectrum makeDeepCopy() throws CloneNotSupportedException {

		/*
		 * The method super.clone() is not used here to avoid removing the mass
		 * fragments from the mass spectrum and to add freshly created ones
		 * again.
		 */
		IRegularMassSpectrum massSpectrum = (IRegularMassSpectrum)super.clone();
		IIon defaultIon;
		/*
		 * The instance variables have been copied by super.clone();.<br/> The
		 * ions in the ion list need not to be removed via
		 * removeAllIons as the method super.clone() has created a new
		 * list.<br/> It is necessary to fill the list again, as the abstract
		 * super class does not know each available type of ion.<br/>
		 * Make a deep copy of all ions.
		 */
		for(IIon ion : getIons()) {
			defaultIon = new Ion(ion.getIon(), ion.getAbundance());
			massSpectrum.addIon(defaultIon);
		}
		return massSpectrum;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return makeDeepCopy();
	}
}
