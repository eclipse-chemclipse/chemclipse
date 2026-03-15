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
package org.eclipse.chemclipse.wsd.model.core.implementation;

import org.eclipse.chemclipse.wsd.model.core.AbstractPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.IPeakModelWSD;

public class PeakWSD extends AbstractPeakWSD {

	public PeakWSD(IPeakModelWSD peakModel, String modelDescription) throws IllegalArgumentException {
		super(peakModel, modelDescription);
	}

	public PeakWSD(IPeakModelWSD peakModel) throws IllegalArgumentException {
		super(peakModel);
	}
}
