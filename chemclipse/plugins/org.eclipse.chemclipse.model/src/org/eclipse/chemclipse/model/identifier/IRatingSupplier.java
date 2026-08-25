/*******************************************************************************
 * Copyright (c) 2023, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - rate by score instead of by status
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.io.Serializable;

/**
 * Rates a comparison result. It is contributed by the identification algorithm
 * that created the result, see the extension point
 * "org.eclipse.chemclipse.model.comparisonMetrics".
 */
public interface IRatingSupplier extends Serializable {

	/**
	 * Returns an advice, e.g. "The spectrum is of low quality".
	 * The text is specific to the identification algorithm and is displayed as
	 * it is. Must not be null, return an empty string if there is no advice.
	 *
	 * @return String
	 */
	String getAdvise();

	/**
	 * Returns the normalized rating of the comparison result.
	 * 0 - bad
	 * 100 - perfect
	 * Float.NaN - no match or no meaningful rating
	 */
	float getScore();

	/**
	 * Must not be null.
	 * Updates the underlying comparison result required to calculate the
	 * rating and advise.
	 */
	void updateComparisonResult(IComparisonResult comparisonResult);
}
