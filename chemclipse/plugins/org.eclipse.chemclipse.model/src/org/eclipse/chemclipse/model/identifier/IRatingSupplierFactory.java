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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

/**
 * Creates the {@link IRatingSupplier} of an identification algorithm. It is
 * contributed via the extension point
 * "org.eclipse.chemclipse.model.comparisonMetrics" and is resolved by the
 * algorithm id of an {@link IComparisonResult}.
 */
public interface IRatingSupplierFactory {

	/**
	 * Returns a new rating supplier. The comparison result is assigned by the
	 * caller via {@link IRatingSupplier#updateComparisonResult(IComparisonResult)}.
	 *
	 * @return {@link IRatingSupplier}
	 */
	IRatingSupplier createRatingSupplier();
}
