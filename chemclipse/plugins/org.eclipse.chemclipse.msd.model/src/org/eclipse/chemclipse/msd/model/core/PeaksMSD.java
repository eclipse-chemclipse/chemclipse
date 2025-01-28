/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.util.ArrayList;
import java.util.List;

public class PeaksMSD implements IPeaksMSD {

	List<IPeakMSD> peaks = new ArrayList<>();

	@Override
	public void addPeak(IPeakMSD peak) {

		peaks.add(peak);
	}

	@Override
	public void removePeak(IPeakMSD peak) {

		if(peak != null) {
			peaks.remove(peak);
		}
	}

	@Override
	public List<IPeakMSD> getPeaks() {

		return peaks;
	}
}
