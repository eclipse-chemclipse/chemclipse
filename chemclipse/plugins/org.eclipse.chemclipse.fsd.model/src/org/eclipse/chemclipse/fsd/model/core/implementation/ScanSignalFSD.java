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

public class ScanSignalFSD extends AbstractScanSignalFSD {

	private static final long serialVersionUID = -3273447342785680772L;

	public ScanSignalFSD(float wavelength, float fluorescence) {

		super();
		setWavelength(wavelength);
		setFluorescence(fluorescence);
	}
}
