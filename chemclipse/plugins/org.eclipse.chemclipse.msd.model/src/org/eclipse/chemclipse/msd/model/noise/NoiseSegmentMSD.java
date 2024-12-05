/*******************************************************************************
 * Copyright (c) 2010, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.noise;

import org.eclipse.chemclipse.model.support.IAnalysisSegment;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;

public class NoiseSegmentMSD implements INoiseSegmentMSD {

	private IAnalysisSegment analysisSegment;
	private ICombinedMassSpectrum noiseMassSpectrum;

	public NoiseSegmentMSD(IAnalysisSegment analysisSegment, ICombinedMassSpectrum noiseMassSpectrum) {

		this.analysisSegment = analysisSegment;
		this.noiseMassSpectrum = noiseMassSpectrum;
	}

	@Override
	public IAnalysisSegment getAnalysisSegment() {

		return analysisSegment;
	}

	@Override
	public ICombinedMassSpectrum getNoiseMassSpectrum() {

		return noiseMassSpectrum;
	}
}
