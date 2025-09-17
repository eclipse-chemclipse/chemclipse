/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.comparison.purity;

public interface IMassSpectrumPurityResult {

	/**
	 * The fit value describes how good the unknown fits into the reference.<br/>
	 * Zero means that there are no mass spectra which could be found in the
	 * other mass spectrum.<br/>
	 * A value of 100 says that all mass spectra of unknown can be found in
	 * reference.
	 * 
	 * @return float
	 */
	float getFitValue();

	/**
	 * The reverse fit describes the fit value just the way around.<br/>
	 * So reference is compared to unknown.
	 * 
	 * @return float
	 */
	float getReverseFitValue();
}
