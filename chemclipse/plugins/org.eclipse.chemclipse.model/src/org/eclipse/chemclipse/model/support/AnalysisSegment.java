/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

/**
 * A analysis segment represents a segment of a chromatogram which should be analyzed.
 * The segment gives information about the start and stop scan number and the segment area.
 */
public abstract class AnalysisSegment implements IAnalysisSegment {

	private int startScan = 0;
	private int stopScan = 0;

	public AnalysisSegment(int startScan, int segmentWidth) {

		if(startScan > 0 && segmentWidth > 0) {
			this.startScan = startScan;
			this.stopScan = startScan + segmentWidth - 1;
		}
	}

	@Override
	public int getStartScan() {

		return startScan;
	}

	@Override
	public int getStopScan() {

		return stopScan;
	}

	public void setStartScan(int startScan) {

		if(startScan > 0) {
			this.startScan = startScan;
		} else {
			throw new IllegalArgumentException("start scan must be > 0");
		}
	}

	public void setStopScan(int stopScan) {

		if(stopScan >= startScan) {
			this.stopScan = stopScan;
		} else {
			throw new IllegalArgumentException("stop scan must be >= start scan");
		}
	}
}