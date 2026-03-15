/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.implementation;

import org.eclipse.chemclipse.model.core.AbstractPeakIntensityValues;

public class PeakIntensityValues extends AbstractPeakIntensityValues {

	private static final long serialVersionUID = 8596503209176077779L;

	public PeakIntensityValues() {

		super();
	}

	public PeakIntensityValues(float maxIntensity) {

		super(maxIntensity);
	}
}