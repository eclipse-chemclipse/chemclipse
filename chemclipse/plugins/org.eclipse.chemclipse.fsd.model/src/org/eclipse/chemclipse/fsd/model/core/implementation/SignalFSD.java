/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core.implementation;

import org.eclipse.chemclipse.fsd.model.core.ISignalFSD;
import org.eclipse.chemclipse.model.core.AbstractSignal;

public class SignalFSD extends AbstractSignal implements ISignalFSD, Comparable<ISignalFSD> {

	private static final long serialVersionUID = -8194790301556998815L;
	//
	private float wavelength = 0; // nm
	private double intensity = 0;

	public SignalFSD(float wavelength, double intensity) {

		this.wavelength = wavelength;
		this.intensity = intensity;
	}

	@Override
	public double getX() {

		return wavelength;
	}

	@Override
	public double getY() {

		return intensity;
	}

	@Override
	public float getWavelength() {

		return wavelength;
	}

	@Override
	public void setWavelength(float wavelength) {

		this.wavelength = wavelength;
	}

	@Override
	public double getIntensity() {

		return intensity;
	}

	@Override
	public void setIntensity(double intensity) {

		this.intensity = intensity;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(wavelength);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		SignalFSD other = (SignalFSD)obj;
		return (Float.floatToIntBits(wavelength) == Float.floatToIntBits(other.wavelength));
	}

	@Override
	public String toString() {

		return "SignalFSD [wavelength=" + wavelength + "]";
	}

	@Override
	public int compareTo(ISignalFSD signal) {

		if(signal != null) {
			return Float.compare(wavelength, signal.getWavelength());
		} else {
			return 0;
		}
	}
}
