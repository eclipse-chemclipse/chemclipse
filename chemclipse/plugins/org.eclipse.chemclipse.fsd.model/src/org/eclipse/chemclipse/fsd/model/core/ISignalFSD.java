/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.fsd.model.core;

import java.io.Serializable;

import org.eclipse.chemclipse.model.core.ISignal;

// Fluorescence Spectroscopy Signal
public interface ISignalFSD extends ISignal, Serializable {

	float getWavelength();

	void setWavelength(float wavelength);

	double getIntensity();

	void setIntensity(double intensity);
}
