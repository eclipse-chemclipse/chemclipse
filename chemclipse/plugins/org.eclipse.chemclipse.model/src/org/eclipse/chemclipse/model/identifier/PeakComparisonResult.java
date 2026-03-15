/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

public class PeakComparisonResult extends AbstractPeakComparisonResult {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = -1197615645280532178L;
	private boolean unique;

	public PeakComparisonResult(float matchQuality, float reverseMatchQuality, float matchFactorDirect, float reverseMatchFactorDirect) {
		super(matchQuality, reverseMatchQuality, matchFactorDirect, reverseMatchFactorDirect);
	}

	public PeakComparisonResult(float matchFactor, float reverseMatchFactor, float matchFactorDirect, float reverseMatchFactorDirect, float probability) {
		super(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect, probability);
	}

	@Override
	public boolean isMarkerPeak() {

		return unique;
	}

	@Override
	public void setMarkerPeak(boolean unique) {

		this.unique = unique;
	}
}
