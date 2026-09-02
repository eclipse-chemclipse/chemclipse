/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.support.traces.ITrace;

public class ResponseSignal implements IResponseSignal {

	private static final long serialVersionUID = -4768367983343261439L;

	private double signal = ITrace.TOTAL_INTENSITY;
	private double concentration = 0.0d;
	private double response = 0.0d;

	public ResponseSignal(double signal, double concentration, double response) {
		this.signal = signal;
		this.concentration = concentration;
		this.response = response;
	}

	@Override
	public double getSignal() {

		return signal;
	}

	@Override
	public double getConcentration() {

		return concentration;
	}

	@Override
	public double getResponse() {

		return response;
	}

	@Override
	public void setResponse(double response) {

		this.response = response;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(concentration);
		result = prime * result + (int)(temp ^ (temp >>> 32));
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
		ResponseSignal other = (ResponseSignal)obj;
		if(Double.doubleToLongBits(concentration) != Double.doubleToLongBits(other.concentration))
			return false;
		if(Double.doubleToLongBits(signal) != Double.doubleToLongBits(other.signal))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "AbstractConcentrationResponseEntry [signal=" + signal + ", concentration=" + concentration + ", response=" + response + "]";
	}
}
