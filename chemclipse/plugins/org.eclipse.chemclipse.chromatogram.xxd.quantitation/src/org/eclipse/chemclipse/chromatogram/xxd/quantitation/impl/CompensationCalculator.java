/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.impl;

public class CompensationCalculator {

	private double factor = 0;

	public CompensationCalculator(double setpoint, double actualValue) {

		if(setpoint > 0 && actualValue > 0) {
			factor = setpoint / actualValue;
		}
	}

	public boolean isEnabled() {

		return factor != 0;
	}

	public double getCompensationFactorCalculated(double concentrationSetpoint, double compensationFactorSetpoint) {

		if(factor > 0) {
			if(concentrationSetpoint > 0) {
				double concentrationActualValue = factor * concentrationSetpoint;
				if(compensationFactorSetpoint > 0) {
					double compensationFactorActualValue = (concentrationActualValue / concentrationSetpoint) * compensationFactorSetpoint;
					return compensationFactorActualValue;
				}
			}
		}

		return 0;
	}
}