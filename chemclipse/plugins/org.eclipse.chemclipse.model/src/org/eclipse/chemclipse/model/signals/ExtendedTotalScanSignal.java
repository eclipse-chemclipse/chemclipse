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
 *******************************************************************************/
package org.eclipse.chemclipse.model.signals;

/**
 * What's the difference between {@link TotalScanSignal} and {@link ExtendedTotalScanSignal}?<br/>
 * The extended total ion signal allows also negative total signals.
 */
public class ExtendedTotalScanSignal extends AbstractTotalScanSignal {

	/*
	 * No limits like in TotalIonSignal.
	 */
	public ExtendedTotalScanSignal(int retentionTime, float retentionIndex, float totalSignal) {

		setRetentionTime(retentionTime);
		setRetentionIndex(retentionIndex);
		setTotalSignal(totalSignal);
	}

	// ------------------------------ITotalIonSignal
	@Override
	public ITotalScanSignal makeDeepCopy() {

		return new ExtendedTotalScanSignal(getRetentionTime(), getRetentionIndex(), getTotalSignal());
	}
	// ------------------------------ITotalIonSignal
}
