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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.model.quantitation.AbstractQuantitationPeak;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;

public class QuantitationPeakMSD extends AbstractQuantitationPeak {

	/**
	 * Renew the UUID on change.
	 */
	private static final long serialVersionUID = -4605765688648534473L;

	public QuantitationPeakMSD(IPeakMSD referencePeakMSD, double concentration, String concentrationUnit) {
		super(referencePeakMSD, concentration, concentrationUnit);
	}
}
