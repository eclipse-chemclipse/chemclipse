/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.csd.model.implementation;

import org.eclipse.chemclipse.csd.model.core.AbstractPeakCSD;
import org.eclipse.chemclipse.csd.model.core.IPeakModelCSD;

public class PeakCSD extends AbstractPeakCSD {

	public PeakCSD(IPeakModelCSD peakModel, String modelDescription) throws IllegalArgumentException {

		super(peakModel, modelDescription);
	}

	public PeakCSD(IPeakModelCSD peakModel) throws IllegalArgumentException {

		super(peakModel);
	}
}