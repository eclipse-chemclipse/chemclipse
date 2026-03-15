/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.core.implementation;

import org.eclipse.chemclipse.wsd.model.core.AbstractScanWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;

public class ScanWSD extends AbstractScanWSD {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -4018377154187209353L;

	public ScanWSD() {

		super();
	}

	public ScanWSD(IScanWSD scanWSD, float actualPercentageIntensity) throws IllegalArgumentException {

		super(scanWSD, actualPercentageIntensity);
	}
}
