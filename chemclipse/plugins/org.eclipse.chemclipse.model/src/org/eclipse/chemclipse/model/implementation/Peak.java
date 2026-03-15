/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.AbstractPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;

public class Peak extends AbstractPeak {

	private IPeakModel peakModel;

	/**
	 * Better use the other constructors otherwise
	 * peak model is null.
	 */
	public Peak() {

	}

	public Peak(IPeakModel peakModel) throws IllegalArgumentException {

		validatePeakModel(peakModel);
		this.peakModel = peakModel;
	}

	public Peak(IPeakModel peakModel, String modelDescription) throws IllegalArgumentException {

		this(peakModel);
		setModelDescription(modelDescription);
	}

	@Override
	public IPeakModel getPeakModel() {

		return peakModel;
	}
}
