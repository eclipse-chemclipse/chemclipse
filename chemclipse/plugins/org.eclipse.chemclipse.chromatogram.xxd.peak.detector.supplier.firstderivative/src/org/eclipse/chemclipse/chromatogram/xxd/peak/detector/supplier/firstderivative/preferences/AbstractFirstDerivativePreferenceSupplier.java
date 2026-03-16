/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;

public abstract class AbstractFirstDerivativePreferenceSupplier extends AbstractPreferenceSupplier {

	public static final float MIN_SN_RATIO_MIN = 0.0f; // 0 = all peaks will be added
	public static final float MIN_SN_RATIO_MAX = Float.MAX_VALUE; // 0 = all peaks will be added
	public static final int MIN_WINDOW_SIZE = 0;
	public static final int MAX_WINDOW_SIZE = 45;
}