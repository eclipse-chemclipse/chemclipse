/*******************************************************************************
 * Copyright (c) 2019, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Matthias Mailänder - remove enums
 * Philip Wenig - refactoring S/N calculation
 *******************************************************************************/
package org.eclipse.chemclipse.model.results;

import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.support.AnalysisSupport;
import org.eclipse.chemclipse.model.support.ChromatogramSegment;

public class ChromatogramSegmentation extends AnalysisSegmentMeasurementResult<ChromatogramSegment> {

	private static final long serialVersionUID = 7162215390880261702L;
	//
	private final List<ChromatogramSegment> chromatogramSegments;
	private final int segmentWidth;

	public ChromatogramSegmentation(IChromatogram chromatogram, int segmentWidth) {

		this.segmentWidth = segmentWidth;
		chromatogramSegments = Collections.unmodifiableList(AnalysisSupport.getChromatogramSegments(chromatogram, segmentWidth));
	}

	@Override
	public String getName() {

		return "Chromatogram Segments";
	}

	@Override
	public List<ChromatogramSegment> getResult() {

		return chromatogramSegments;
	}

	public int getSegmentWidth() {

		return segmentWidth;
	}

	@Override
	public boolean isVisible() {

		return false;
	}

	@Override
	public Class<ChromatogramSegment> getType() {

		return ChromatogramSegment.class;
	}
}
