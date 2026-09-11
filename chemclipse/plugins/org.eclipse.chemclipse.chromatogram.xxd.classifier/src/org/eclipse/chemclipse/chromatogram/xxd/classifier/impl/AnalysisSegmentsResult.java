/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.classifier.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.IChromatogramClassifierResult;
import org.eclipse.chemclipse.chromatogram.xxd.classifier.result.ResultStatus;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.support.IAnalysisSegment;

public class AnalysisSegmentsResult implements IMeasurementResult<List<IAnalysisSegment>>, IChromatogramClassifierResult {

	private static final long serialVersionUID = 6174042111311287631L;
	private List<IAnalysisSegment> analysisSegments = new ArrayList<>();

	public AnalysisSegmentsResult(List<IAnalysisSegment> analysisSegments) {

		this.analysisSegments.addAll(analysisSegments);
	}

	@Override
	public ResultStatus getResultStatus() {

		return ResultStatus.OK;
	}

	@Override
	public String getName() {

		return "Analysis Segments (Fixed Width)";
	}

	@Override
	public String getIdentifier() {

		return AnalysisSegmentsResult.class.getName();
	}

	@Override
	public String getDescription() {

		return "Set analysis segments with a fixed width.";
	}

	@Override
	public List<IAnalysisSegment> getResult() {

		return analysisSegments;
	}
}