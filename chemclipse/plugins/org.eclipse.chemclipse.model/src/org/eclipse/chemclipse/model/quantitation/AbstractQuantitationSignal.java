/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.quantitation;

public abstract class AbstractQuantitationSignal implements IQuantitationSignal {

	private double signal = 0.0d;
	private double relativeResponse = ABSOLUTE_RELATIVE_RESPONSE;
	private double uncertainty = 0.0d;
	private boolean use = true;

	public AbstractQuantitationSignal(double signal, double relativeResponse) {
		this(signal, relativeResponse, 0.0d, true);
	}

	public AbstractQuantitationSignal(double signal, double relativeResponse, double uncertainty) {
		this(signal, relativeResponse, uncertainty, true);
	}

	public AbstractQuantitationSignal(double signal, double relativeResponse, double uncertainty, boolean use) {
		this.signal = signal;
		this.relativeResponse = relativeResponse;
		this.uncertainty = uncertainty;
		this.use = use;
	}

	@Override
	public double getSignal() {

		return signal;
	}

	@Override
	public double getRelativeResponse() {

		return relativeResponse;
	}

	@Override
	public void setRelativeResponse(double relativeResponse) {

		this.relativeResponse = relativeResponse;
	}

	@Override
	public double getUncertainty() {

		return uncertainty;
	}

	@Override
	public void setUncertainty(double uncertainty) {

		this.uncertainty = uncertainty;
	}

	@Override
	public boolean isUse() {

		return use;
	}

	@Override
	public void setUse(boolean use) {

		this.use = use;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(signal);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AbstractQuantitationSignal other = (AbstractQuantitationSignal)obj;
		if(Double.doubleToLongBits(signal) != Double.doubleToLongBits(other.signal))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "AbstractQuantitationSignal [signal=" + signal + ", relativeResponse=" + relativeResponse + ", uncertainty=" + uncertainty + ", use=" + use + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Override
	public int compareTo(IQuantitationSignal quantitationSignal) {

		int result = 0;
		if(quantitationSignal != null) {
			result = Double.compare(getSignal(), quantitationSignal.getSignal());
		}
		return result;
	}
}
