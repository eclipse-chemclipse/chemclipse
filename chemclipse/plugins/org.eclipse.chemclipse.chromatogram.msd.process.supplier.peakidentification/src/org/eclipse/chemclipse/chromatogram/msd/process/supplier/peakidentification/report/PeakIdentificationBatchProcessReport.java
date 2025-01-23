/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.process.supplier.peakidentification.report;

import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.msd.model.core.PeaksMSD;

public class PeakIdentificationBatchProcessReport implements IPeakIdentificationBatchProcessReport {

	private IPeaksMSD peaks;

	public PeakIdentificationBatchProcessReport() {

		peaks = new PeaksMSD();
	}

	@Override
	public IPeaksMSD getPeaks() {

		return peaks;
	}
}
