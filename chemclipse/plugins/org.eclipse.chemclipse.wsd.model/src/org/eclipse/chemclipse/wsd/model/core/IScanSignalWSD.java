/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.core;

import java.io.Serializable;

public interface IScanSignalWSD extends Serializable {

	/**
	 * Returns the wavelength in nanometer (nm) scale.
	 * 
	 * @return float
	 */
	float getWavelength();

	/**
	 * Sets the wavelength in nanometer (nm) scale.
	 * 
	 */
	void setWavelength(float wavelength);

	/**
	 * Returns the actual abundance of the wavelength.
	 * 
	 * @return float
	 */
	float getAbsorbance();

	/**
	 * Sets an optical density value for the wavelength.
	 * 
	 * @param absorbance
	 */
	void setAbsorbance(float absorbance);
}
