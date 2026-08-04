/*******************************************************************************
 * Copyright (c) 2011, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.coda.calculator;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.support.traces.ITrace;

public interface IMassChromatographicQualityResult {

	/**
	 * Returns an {@link IMarkedTraces<ITrace>} object.<br/>
	 * The object stores, which ion values should not be used.
	 */
	IMarkedTraces<ITrace> getExcludedIons();

	/**
	 * Returns the data reduction value.
	 *
	 * @return float
	 */
	float getDataReductionValue();
}