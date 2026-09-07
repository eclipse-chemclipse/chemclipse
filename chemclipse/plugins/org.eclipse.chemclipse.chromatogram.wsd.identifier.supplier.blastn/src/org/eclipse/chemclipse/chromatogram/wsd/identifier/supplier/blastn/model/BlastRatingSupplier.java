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
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.model;

import java.util.OptionalDouble;

import org.eclipse.chemclipse.model.identifier.AbstractComparisonRatingSupplier;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;

public class BlastRatingSupplier extends AbstractComparisonRatingSupplier {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = 6114130570311935062L;

	@Override
	public float getScore() {

		IComparisonResult comparisonResult = getComparisonResult();
		OptionalDouble identity = comparisonResult.getMetric(BlastMetrics.IDENTITY);
		if(identity.isEmpty()) {
			return Float.NaN;
		}

		return (float)identity.getAsDouble();
	}
}
