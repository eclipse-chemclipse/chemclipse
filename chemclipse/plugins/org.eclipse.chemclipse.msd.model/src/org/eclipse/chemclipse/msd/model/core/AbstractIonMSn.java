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
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.Objects;

public abstract class AbstractIonMSn extends AbstractIon implements IIonMSn {

	private static final long serialVersionUID = 7699261561713484081L;

	private IIonTransition ionTransition;

	protected AbstractIonMSn(double ion, float abundance, IIonTransition ionTransition) throws NullPointerException {

		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		super(ion, abundance);
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new NullPointerException("The given ion transition instance should be not null.");
		}
	}

	protected AbstractIonMSn(IIon ion, IIonTransition ionTransition) throws IllegalArgumentException {

		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		super(ion);
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new IllegalArgumentException("The given ion transition instance should be not null.");
		}
	}

	@Override
	public IIonTransition getIonTransition() {

		return ionTransition;
	}

	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		AbstractIonMSn other = (AbstractIonMSn)otherObject;
		return getIon() == other.getIon() && getAbundance() == other.getAbundance() && Objects.equals(ionTransition, other.getIonTransition());
	}

	@Override
	public int hashCode() {

		int ionTransitionHashCode = 0;
		if(ionTransition != null) {
			ionTransitionHashCode = ionTransition.hashCode();
		}
		return super.hashCode() + ionTransitionHashCode;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("ion=" + getIon());
		builder.append(",");
		builder.append("abundance=" + getAbundance());
		builder.append(",");
		builder.append("ionTransition=" + ionTransition);
		builder.append("]");
		return builder.toString();
	}
}
