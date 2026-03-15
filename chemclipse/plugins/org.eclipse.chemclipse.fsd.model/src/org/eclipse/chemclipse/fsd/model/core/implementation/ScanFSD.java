/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.fsd.model.core.implementation;

import org.eclipse.chemclipse.fsd.model.core.IScanFSD;

public class ScanFSD extends AbstractScanFSD {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = 4152474019760916581L;

	public ScanFSD(IScanFSD scanFSD, float actualPercentageIntensity) throws IllegalArgumentException {

		super(scanFSD, actualPercentageIntensity);
	}
}
