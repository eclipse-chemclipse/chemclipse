/*******************************************************************************
 * Copyright (c) 2013, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.implementation;

import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

public class Chromatogram extends AbstractChromatogram implements IChromatogram {

	private static final long serialVersionUID = -8477205385713705933L;

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

	}

	@Override
	public void updateNoiseFactor() {

	}

}
