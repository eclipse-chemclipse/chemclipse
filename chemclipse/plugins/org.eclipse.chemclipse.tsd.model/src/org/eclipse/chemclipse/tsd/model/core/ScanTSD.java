/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.tsd.model.core;

import org.eclipse.chemclipse.numeric.statistics.Calculations;

public class ScanTSD extends AbstractScanTSD {

	private static final long serialVersionUID = 2994914620573856493L;

	private int driftInterval = 100; // µs
	private float[] signals = new float[0];

	public ScanTSD() {

	}

	public ScanTSD(int retentionTime, float[] signals) {

		super.setRetentionTime(retentionTime);
		this.signals = signals;
	}

	@Override
	public float getTotalSignal() {

		return Calculations.getSum(signals);
	}

	@Override
	public void adjustTotalSignal(float totalSignal) {

		float totalSignalActive = getTotalSignal();
		if(totalSignalActive != 0) {
			float factor = totalSignal / totalSignalActive;
			for(int i = 0; i < signals.length; i++) {
				signals[i] = factor * signals[i];
			}
		}
	}

	@Override
	public int getDriftInterval() {

		return driftInterval;
	}

	@Override
	public void setDriftInterval(int driftInterval) {

		this.driftInterval = driftInterval;
	}

	@Override
	public float[] getSignals() {

		return signals;
	}

	@Override
	public void setSignals(float[] signals) {

		this.signals = signals;
	}

	@Override
	public boolean isEmpty() {

		return signals.length == 0;
	}
}