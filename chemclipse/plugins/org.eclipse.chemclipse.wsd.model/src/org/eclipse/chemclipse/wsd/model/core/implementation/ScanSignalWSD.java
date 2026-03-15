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

import org.eclipse.chemclipse.wsd.model.core.AbstractScanSignalWSD;

public class ScanSignalWSD extends AbstractScanSignalWSD {

	private static final long serialVersionUID = 7188703805929591517L;

	public ScanSignalWSD() {

		super();
	}

	public ScanSignalWSD(float wavelength, float abundance) {

		super();
		setWavelength(wavelength);
		setAbsorbance(abundance);
	}
}
