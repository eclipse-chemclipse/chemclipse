/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.vsd.model.core;

import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.vsd.model.core.selection.ChromatogramSelectionVSD;

public abstract class AbstractChromatogramVSD extends AbstractChromatogram implements IChromatogramVSD {

	private static final long serialVersionUID = -2463054178850833466L;

	@Override
	public void updateNoiseFactor() {

		// TODO - Noise Calculation
	}

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

		/*
		 * Fire an update to inform all listeners.
		 */
		if(chromatogramSelection instanceof ChromatogramSelectionVSD chromatogramSelectionISD) {
			chromatogramSelectionISD.update(true);
		}
	}
}
