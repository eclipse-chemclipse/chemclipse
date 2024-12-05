/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring Observer
 *******************************************************************************/
package org.eclipse.chemclipse.model.results;

import java.util.List;

import org.eclipse.chemclipse.model.support.INoiseSegment;

public class NoiseSegmentMeasurementResult extends AnalysisSegmentMeasurementResult<INoiseSegment> {

	private static final long serialVersionUID = -5989247294723381518L;
	//
	private final ChromatogramSegmentation chromatogramSegmentation;
	private final String noiseCalculatorId;
	private final List<INoiseSegment> noiseSegments;

	public NoiseSegmentMeasurementResult(List<INoiseSegment> noiseSegments, ChromatogramSegmentation chromatogramSegmentation, String noiseCalculatorId) {

		this.noiseSegments = noiseSegments;
		this.chromatogramSegmentation = chromatogramSegmentation;
		this.noiseCalculatorId = noiseCalculatorId;
	}

	@Override
	public String getName() {

		return "Noise Segments";
	}

	@Override
	public List<INoiseSegment> getResult() {

		return noiseSegments;
	}

	public ChromatogramSegmentation getChromatogramSegmentation() {

		return chromatogramSegmentation;
	}

	public String getNoiseCalculatorId() {

		return noiseCalculatorId;
	}

	@Override
	public Class<INoiseSegment> getType() {

		return INoiseSegment.class;
	}
}