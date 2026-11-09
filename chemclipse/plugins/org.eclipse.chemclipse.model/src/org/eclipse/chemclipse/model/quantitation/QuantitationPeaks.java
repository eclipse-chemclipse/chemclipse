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
package org.eclipse.chemclipse.model.quantitation;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.chemclipse.logging.core.Logger;

public class QuantitationPeaks extends ArrayList<IQuantitationPeak> implements IQuantitationPeaks {

	private static final long serialVersionUID = -4303659297540195715L;
	private static final Logger logger = Logger.getLogger(QuantitationPeaks.class);

	private String concentrationUnit = ""; //$NON-NLS-1$

	public QuantitationPeaks(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	@Override
	public void add(int index, IQuantitationPeak quantitationPeak) {

		if(isValid(quantitationPeak)) {
			super.add(index, quantitationPeak);
		}
	}

	@Override
	public boolean add(IQuantitationPeak quantitationPeak) {

		if(isValid(quantitationPeak)) {
			return super.add(quantitationPeak);
		} else {
			return false;
		}
	}

	@Override
	public boolean addAll(Collection<? extends IQuantitationPeak> quantitationPeaks) {

		logger.warn("Please use the add() method."); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends IQuantitationPeak> quantitationPeaks) {

		logger.warn("Please use the add() method."); //$NON-NLS-1$
		return false;
	}

	public boolean isValid(IQuantitationPeak quantitationPeak) {

		if(quantitationPeak != null && concentrationUnit.equals(quantitationPeak.getConcentrationUnit())) {
			return true;
		} else {
			logger.warn("Can't add the IQuantitationPeak, cause it has a different concentration unit."); //$NON-NLS-1$
			return false;
		}
	}
}
