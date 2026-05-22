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
	private static final long serialVersionUID = -2481473608026036078L;
	private static final int MAX_PRECISION = 6;

	private double ion = 0.0d;
	private float abundance = 0.0f;
	private IIonTransition ionTransition;

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

	protected AbstractIon(double ion, float abundance, IIonTransition ionTransition) throws NullPointerException {

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
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new NullPointerException("The given ion transition instance should be not null.");
		}
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

	protected AbstractIon(IIon ion, IIonTransition ionTransition) throws IllegalArgumentException {

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
		this(ion);
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new IllegalArgumentException("The given ion transition instance should be not null.");
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
	 * <li>precision 1 =&gt; 28.8</li>
	 * <li>precision 2 =&gt; 28.79</li>
	 * <li>precision 3 =&gt; 28.787</li>
	 * <li>precision 4 =&gt; 28.7875</li>
	 * <li>precision 5 =&gt; 28.78749</li>
	 * <li>precision 6 =&gt; 28.787492</li>
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

	@Override
	public IIonTransition getIonTransition() {

		return ionTransition;
	}

	/**
	 * Compares the mass/charge ration of two ions. Returns the
	 * following values: a.compareTo(b) 0 a == b : 28 == 28 -1 a &lt; b : 18 &lt; 28
	 * +1 a &gt; b : 28 &gt; 18
	 */
	@Override
	public int compareTo(IIon other) {

		return (int)(this.ion - other.getIon());
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
		return ion == other.getIon() && abundance == other.getAbundance() && ionTransition == other.getIonTransition();
	}

	@Override
	public int hashCode() {

		int ionTransitionHashCode = 0;
		if(ionTransition != null) {
			ionTransitionHashCode = ionTransition.hashCode();
		}
		return 7 * Double.valueOf(ion).hashCode() + 11 * Float.valueOf(abundance).hashCode() + ionTransitionHashCode;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("ion=" + ion);
		builder.append(",");
		builder.append("abundance=" + abundance);
		builder.append(",");
		builder.append("ionTransition=" + ionTransition);
		builder.append("]");
		return builder.toString();
	}
}
