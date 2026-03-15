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
package org.eclipse.chemclipse.chromatogram.xxd.integrator.result;

import org.eclipse.chemclipse.msd.model.core.IIon;

public class ChromatogramIntegrationResult extends AbstractChromatogramIntegrationResult {

	public ChromatogramIntegrationResult(double ion, double chromatogramArea, double backgroundArea) {

		super(ion, chromatogramArea, backgroundArea);
	}

	/**
	 * Set the TIC result.
	 *
	 * @param chromatogramArea
	 * @param backgroundArea
	 */
	public ChromatogramIntegrationResult(double chromatogramArea, double backgroundArea) {

		super(IIon.TIC_ION, chromatogramArea, backgroundArea);
	}
}