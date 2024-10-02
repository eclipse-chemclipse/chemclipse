/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.traces;

/**
 * Orbitrap
 * TOF
 * ...
 */
public class TraceHighResMSD extends TraceGenericDelta {

	public double getMZ() {

		return getValue();
	}

	public void setMZ(double mz) {

		setValue(mz);
	}

	public double getStartMZ() {

		return getStartValue();
	}

	public double getStopMZ() {

		return getStopValue();
	}

	public int getPPM() {

		int ppm = 0;
		if(getDelta() > 0 && getMZ() > 0) {
			ppm = (int)Math.round((getDelta() / getMZ()) * ITrace.MILLION);
		}
		//
		return ppm;
	}

	public void setPPM(int ppm) {

		setDelta(getMZ() * ppm / ITrace.MILLION);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getMZ());
		int ppm = getPPM();
		if(ppm >= 1) {
			builder.append(ITrace.INFIX_RANGE_STANDARD);
			builder.append(ppm);
			builder.append(ITrace.POSTFIX_UNIT_PPM);
		}
		builder.append(getScaleFactorAsString());
		//
		return builder.toString();
	}
}