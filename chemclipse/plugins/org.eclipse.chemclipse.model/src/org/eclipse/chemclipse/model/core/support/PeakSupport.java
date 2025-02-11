/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.core.support;

import java.util.List;

import org.eclipse.chemclipse.model.core.IPeak;

public class PeakSupport {

	public static IPeak selectNearestPeak(List<? extends IPeak> peaks, IPeak peak) {

		return selectNearestPeak(peaks, peak.getPeakModel().getRetentionTimeAtPeakMaximum());
	}

	public static IPeak selectNearestPeak(List<? extends IPeak> peaks, int retentionTime) {

		IPeak nearestPeak = null;
		for(IPeak peak : peaks) {
			if(nearestPeak == null) {
				nearestPeak = peak;
			} else {
				int deltaNearest = Math.abs(retentionTime - nearestPeak.getPeakModel().getRetentionTimeAtPeakMaximum());
				int deltaCurrent = Math.abs(retentionTime - peak.getPeakModel().getRetentionTimeAtPeakMaximum());
				if(deltaCurrent <= deltaNearest) {
					nearestPeak = peak;
				}
			}
		}
		return nearestPeak;
	}
}
