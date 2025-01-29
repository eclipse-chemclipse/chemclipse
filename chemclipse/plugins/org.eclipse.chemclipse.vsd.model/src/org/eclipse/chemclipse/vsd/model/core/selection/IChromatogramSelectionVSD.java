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
package org.eclipse.chemclipse.vsd.model.core.selection;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramPeakVSD;
import org.eclipse.chemclipse.vsd.model.core.IChromatogramVSD;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;

public interface IChromatogramSelectionVSD extends IChromatogramSelection<IChromatogramPeakVSD, IChromatogramVSD> {

	@Override
	IChromatogramVSD getChromatogram();

	@Override
	IScanVSD getSelectedScan();
}
