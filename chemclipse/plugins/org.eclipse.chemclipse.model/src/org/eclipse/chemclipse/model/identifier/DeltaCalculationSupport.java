/*******************************************************************************
 * Copyright (c) 2022, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.core.IScan;

public class DeltaCalculationSupport {

	public static boolean useTarget(IScan scanUnknown, IScan scanReference, IDeltaCalculationSettings deltaCalculationSettings) {

		int retentionTimeUnknown = scanUnknown.getRetentionTime();
		double retentionIndexUnknown = Math.round(scanUnknown.getRetentionIndex());
		int retentionTimeReference = scanReference.getRetentionTime();
		double[] retentionIndicesReference = new double[]{scanReference.getRetentionIndex()};
		//
		return useTarget(retentionTimeUnknown, retentionIndexUnknown, retentionTimeReference, retentionIndicesReference, deltaCalculationSettings);
	}

	public static boolean useTarget(int retentionTimeUnknown, double retentionIndexUnknown, int retentionTimeReference, double[] retentionIndicesReference, IDeltaCalculationSettings deltaCalculationSettings) {

		boolean useTarget = false;
		switch(deltaCalculationSettings.getDeltaCalculation()) {
			case RETENTION_TIME_MS:
				useTarget = useTarget(retentionTimeUnknown, retentionTimeReference, deltaCalculationSettings.getDeltaWindow());
				break;
			case RETENTION_TIME_MIN:
				useTarget = useTarget(retentionTimeUnknown / IChromatogramOverview.MINUTE_CORRELATION_FACTOR, retentionTimeReference / IChromatogramOverview.MINUTE_CORRELATION_FACTOR, deltaCalculationSettings.getDeltaWindow());
				break;
			case RETENTION_INDEX:
				exitloop:
				for(double retentionIndexReference : retentionIndicesReference) {
					if(useTarget(retentionIndexUnknown, retentionIndexReference, deltaCalculationSettings.getDeltaWindow())) {
						useTarget = true;
						break exitloop;
					}
				}
				break;
			default:
				useTarget = true;
				break;
		}
		return useTarget;
	}

	/**
	 * Validates if the current target shall be used.
	 * 
	 * @param valueUnknown
	 * @param valueReference
	 * @param valueDelta
	 * @return boolean
	 */
	public static boolean useTarget(double valueUnknown, double valueReference, double valueDelta) {

		return valueReference >= (valueUnknown - valueDelta) && valueReference <= (valueUnknown + valueDelta);
	}
}