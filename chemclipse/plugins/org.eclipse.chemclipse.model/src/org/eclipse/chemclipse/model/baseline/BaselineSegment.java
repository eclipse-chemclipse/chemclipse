/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.baseline;

import java.util.Objects;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.numeric.core.Point;
import org.eclipse.chemclipse.numeric.equations.Equations;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceEmpty;

/**
 * This class implements a baseline segment used in org.eclipse.chemclipse.msd.model.core.IChromatogramMSD and {@link BaselineModel}.
 */
public class BaselineSegment implements IBaselineSegment {

	private static final Logger logger = Logger.getLogger(BaselineSegment.class);
	private int startRetentionTime = 0;
	private float startBackgroundAbundance = 0.0f;
	private int stopRetentionTime = 0;
	private float stopBackgroundAbundance = 0.0f;
	private ITrace trace = new TraceEmpty();

	/**
	 * The start retention time must be &lt;= than the stop retention time.
	 */
	public BaselineSegment(int startRetentionTime, int stopRetentionTime, ITrace trace) {

		if(startRetentionTime > stopRetentionTime) {
			int tmp = startRetentionTime;
			startRetentionTime = stopRetentionTime;
			stopRetentionTime = tmp;
		}
		setStopRetentionTime(stopRetentionTime);
		setStartRetentionTime(startRetentionTime);
		if(trace != null) {
			this.trace = trace;
		}
	}

	@Override
	public float getStartBackgroundAbundance() {

		return startBackgroundAbundance;
	}

	@Override
	public int getStartRetentionTime() {

		return startRetentionTime;
	}

	@Override
	public float getStopBackgroundAbundance() {

		return stopBackgroundAbundance;
	}

	@Override
	public int getStopRetentionTime() {

		return stopRetentionTime;
	}

	@Override
	public void setStartBackgroundAbundance(float startBackgroundAbundance) {

		this.startBackgroundAbundance = startBackgroundAbundance;
	}

	@Override
	public void setStartRetentionTime(int startRetentionTime) {

		if(startRetentionTime >= 0 && startRetentionTime <= this.stopRetentionTime) {
			this.startRetentionTime = startRetentionTime;
		} else {
			logger.warn("The start retention time must be lower or equal than the stop retention time. " + "start: " + startRetentionTime + " - " + "stop: " + stopRetentionTime);
		}
	}

	@Override
	public void setStopBackgroundAbundance(float stopBackgroundAbundance) {

		this.stopBackgroundAbundance = stopBackgroundAbundance;
	}

	@Override
	public void setStopRetentionTime(int stopRetentionTime) {

		if(stopRetentionTime >= 0 && stopRetentionTime >= this.startRetentionTime) {
			this.stopRetentionTime = stopRetentionTime;
		} else {
			logger.warn("The stop retention time must be higher or equal than the start retention time. " + "start: " + startRetentionTime + " - " + "stop: " + stopRetentionTime);
		}
	}

	@Override
	public ITrace getTrace() {

		return trace;
	}

	@Override
	public float getBackgroundAbundance(int retentionTime) {

		Point p1 = new Point(startRetentionTime, startBackgroundAbundance);
		Point p2 = new Point(stopRetentionTime, stopBackgroundAbundance);
		return (float)Equations.createLinearEquation(p1, p2).calculateY(retentionTime);
	}

	@Override
	public int hashCode() {

		return Objects.hash(startBackgroundAbundance, startRetentionTime, stopBackgroundAbundance, stopRetentionTime, trace);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		BaselineSegment other = (BaselineSegment)obj;
		return Float.floatToIntBits(startBackgroundAbundance) == Float.floatToIntBits(other.startBackgroundAbundance) && startRetentionTime == other.startRetentionTime && Float.floatToIntBits(stopBackgroundAbundance) == Float.floatToIntBits(other.stopBackgroundAbundance) && stopRetentionTime == other.stopRetentionTime && Objects.equals(trace, other.trace);
	}

	@Override
	public String toString() {

		return "BaselineSegment [startRetentionTime=" + startRetentionTime + ", startBackgroundAbundance=" + startBackgroundAbundance + ", stopRetentionTime=" + stopRetentionTime + ", stopBackgroundAbundance=" + stopBackgroundAbundance + ", trace=" + trace + "]";
	}
}