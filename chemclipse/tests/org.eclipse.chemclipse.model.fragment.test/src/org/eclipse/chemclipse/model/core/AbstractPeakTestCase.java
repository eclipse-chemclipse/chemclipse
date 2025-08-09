/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.chemclipse.model.implementation.Peak;
import org.eclipse.chemclipse.model.implementation.PeakIntensityValues;
import org.eclipse.chemclipse.model.implementation.PeakModel;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.junit.Ignore;

@Ignore
public class AbstractPeakTestCase {

	protected IPeak createPeak(float totalSignal, TreeMap<Integer, Float> retentionTimeIntensityMap, float startBackgroundAbundance, float stopBackgroundAbundance, boolean strictModel) {

		IScan peakMaximum = new Scan(totalSignal);
		IPeakIntensityValues peakIntensityValues = create(retentionTimeIntensityMap);
		IPeakModel peakModel = new PeakModel(peakMaximum, peakIntensityValues, startBackgroundAbundance, stopBackgroundAbundance);
		peakModel.setStrictModel(strictModel);

		return new Peak(peakModel);
	}

	protected IPeakIntensityValues create(TreeMap<Integer, Float> retentionTimeIntensityMap) {

		float maxIntensity = retentionTimeIntensityMap.values().stream().max(Float::compare).get();
		IPeakIntensityValues peakIntensityValues = new PeakIntensityValues(maxIntensity);
		for(Map.Entry<Integer, Float> entry : retentionTimeIntensityMap.entrySet()) {
			peakIntensityValues.addIntensityValue(entry.getKey(), entry.getValue());
		}
		peakIntensityValues.normalize();

		return peakIntensityValues;
	}
}