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
 * If you need signals with a negative total signal range, use {@link ExtendedTotalScanSignal}.
 */
public class TotalScanSignal extends AbstractTotalScanSignal {

	/**
	 * Add the values with a positive check.
	 *
	 * @param retentionTime
	 * @param retentionIndex
	 * @param totalSignal
	 */
	public TotalScanSignal(int retentionTime, float retentionIndex, float totalSignal) {

		this(retentionTime, retentionIndex, totalSignal, true);
	}

	/**
	 * Validates that retention time and retention index are positive.
	 * If validatePositive is true, it will be checked that totalSignal is positive too.
	 *
	 * @param retentionTime
	 * @param retentionIndex
	 * @param totalSignal
	 * @param validatePositive
	 */
	public TotalScanSignal(int retentionTime, float retentionIndex, float totalSignal, boolean validatePositive) {

		if(retentionTime >= 0) {
			setRetentionTime(retentionTime);
		}
		if(retentionIndex >= 0) {
			setRetentionIndex(retentionIndex);
		}
		setTotalSignal(totalSignal, validatePositive);
	}

	@Override
	public ITotalScanSignal makeDeepCopy() {

		return new TotalScanSignal(getRetentionTime(), getRetentionIndex(), getTotalSignal(), false);
	}
	// ------------------------------ITotalIonSignal
}
