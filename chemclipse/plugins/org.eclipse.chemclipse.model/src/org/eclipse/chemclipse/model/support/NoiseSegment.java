/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

import java.util.Collection;
import java.util.Collections;

public class NoiseSegment implements INoiseSegment {

	private static final long serialVersionUID = 6666299719479935503L;
	//
	private IAnalysisSegment analysisSegment = null;
	private double noiseFactor = 0.0d;
	private String traces = "";
	private boolean use = false;
	private boolean userSelection = false;

	public NoiseSegment(IAnalysisSegment analysisSegment, double noiseFactor) {

		this.analysisSegment = analysisSegment;
		this.noiseFactor = noiseFactor;
	}

	@Override
	public int getStartScan() {

		return analysisSegment.getStartScan();
	}

	@Override
	public int getStopScan() {

		return analysisSegment.getStopScan();
	}

	@Override
	public Collection<IAnalysisSegment> getChildSegments() {

		return Collections.singleton(analysisSegment);
	}

	@Override
	public double getNoiseFactor() {

		return noiseFactor;
	}

	@Override
	public void setNoiseFactor(double noiseFactor) {

		this.noiseFactor = noiseFactor;
	}

	@Override
	public String getTraces() {

		return traces;
	}

	@Override
	public boolean isUse() {

		return use;
	}

	@Override
	public void setUse(boolean use) {

		this.use = use;
	}

	public boolean isUserSelection() {

		return userSelection;
	}

	public void setUserSelection(boolean userSelection) {

		this.userSelection = userSelection;
	}

	@Override
	public int getStartRetentionTime() {

		return analysisSegment.getStartRetentionTime();
	}

	@Override
	public int getStopRetentionTime() {

		return analysisSegment.getStopRetentionTime();
	}
}