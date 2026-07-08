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

import org.eclipse.chemclipse.model.math.IonRoundMethod;
import org.eclipse.core.runtime.Adapters;

/**
 * All ions implement the interface Serializable to enable an
 * automated storage in disk.<br/>
 * The serialization of ions is controlled by the corresponding mass
 * spectrum.
 *
 * @see AbstractScanMSD
 */
public abstract class AbstractIon implements IIon {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -2481473608026036079L;
	private static final int MAX_PRECISION = 6;

	private double ion = 0.0d;
	private float abundance = 0.0f;

	protected AbstractIon(double ion) {

		setIon(ion);
	}

	protected AbstractIon(double ion, float abundance) {

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
		setIon(ion);
		setAbundance(abundance);
	}

	protected AbstractIon(IIon ion) throws IllegalArgumentException {

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
		if(ion != null) {
			setIon(ion.getIon());
			setAbundance(ion.getAbundance());
		} else {
			throw new IllegalArgumentException("The given ion instance should be not null.");
		}
	}

	/**
	 * Returns the given ion as an integer value.
	 * The rounding is based on the selected rounding method in the system.
	 */
	public static int getIon(double mz) {

		return IonRoundMethod.getActive().round(mz);
	}

	/**
	 * Returns the given ion as an value rounded to the given precision.
	 * E.g.:
	 * <ul>
	 * <li>ion = 28.78749204</li>
	 * <li>precision 1 ⇒ 28.8</li>
	 * <li>precision 2 ⇒ 28.79</li>
	 * <li>precision 3 ⇒ 28.787</li>
	 * <li>precision 4 ⇒ 28.7875</li>
	 * <li>precision 5 ⇒ 28.78749</li>
	 * <li>precision 6 ⇒ 28.787492</li>
	 * </ul>
	 *
	 * The precision of 6 is the maximum. If the precious is outward of
	 * this bounds it will set to 1.
	 */
	public static double getIon(double ion, int precision) {

		if(precision <= 0 || precision > MAX_PRECISION) {
			precision = 1;
		}
		/*
		 * Math.round() - OK
		 */
		double factor = Math.pow(10, precision);
		return Math.round(ion * factor) / factor;
	}

	/**
	 * Returns the given abundance as an integer value.
	 */
	public static int getAbundance(float abundance) {

		/*
		 * Math.round() - OK
		 */
		return Math.round(abundance);
	}

	@Override
	public float getAbundance() {

		return this.abundance;
	}

	@Override
	public double getIon() {

		return this.ion;
	}

	@Override
	public boolean setAbundance(float abundance) {

		if(abundance < 0) {
			return false;
		}
		this.abundance = abundance;
		return true;
	}

	@Override
	public boolean setIon(double ion) {

		if(ion < 0) {
			return false;
		}
		this.ion = ion;
		return true;
	}

	/**
	 * Compares the mass/charge ration of two ions.
	 */
	@Override
	public int compareTo(IIon other) {

		return Double.compare(this.ion, other.getIon());
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {

		return Adapters.adapt(this, adapter);
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
		AbstractIon other = (AbstractIon)otherObject;
		return ion == other.getIon() && abundance == other.getAbundance();
	}

	@Override
	public int hashCode() {

		return 7 * Double.hashCode(ion) + 11 * Float.hashCode(abundance);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("ion=" + ion);
		builder.append(",");
		builder.append("abundance=" + abundance);
		builder.append("]");
		return builder.toString();
	}
}
