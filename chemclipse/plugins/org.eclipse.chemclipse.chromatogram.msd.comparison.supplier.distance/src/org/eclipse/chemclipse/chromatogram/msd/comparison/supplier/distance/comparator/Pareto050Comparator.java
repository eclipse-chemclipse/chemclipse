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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.internal.ParetoDistance;
import org.eclipse.chemclipse.msd.identifier.comparison.IMassSpectrumComparator;

public class Pareto050Comparator extends AbstractDistanceComparator implements IMassSpectrumComparator {

	@Override
	DistanceMeasure getDistanceMeasure() {

		return new ParetoDistance(0.75);
	}
}