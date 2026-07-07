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
 * Christoph Läubrich - copy constrcutor
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

public class RetentionTimeRange implements IRetentionTimeRange {

	private int startRetentionTime;
	private int stopRetentionTime;

	public RetentionTimeRange(IRetentionTimeRange range) {

		this(range.getStartRetentionTime(), range.getStopRetentionTime());
	}

	/**
	 * Creates a new retention time range.
	 * 
	 * @param startRetentionTime
	 * @param stopRetentionTime
	 */
	public RetentionTimeRange(int startRetentionTime, int stopRetentionTime) {

		if(startRetentionTime < 0) {
			startRetentionTime = 0;
		}
		if(stopRetentionTime < 0) {
			stopRetentionTime = 0;
		}
		if(startRetentionTime > stopRetentionTime) {
			this.startRetentionTime = stopRetentionTime;
			this.stopRetentionTime = startRetentionTime;
		} else {
			this.startRetentionTime = startRetentionTime;
			this.stopRetentionTime = stopRetentionTime;
		}
	}

	@Override
	public int getStartRetentionTime() {

		return startRetentionTime;
	}

	@Override
	public int getStopRetentionTime() {

		return stopRetentionTime;
	}

	// --------------------------------------equals, hashCode, toString
	@Override
	public boolean equals(Object other) {

		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		if(getClass() != other.getClass()) {
			return false;
		}
		RetentionTimeRange otherRange = (RetentionTimeRange)other;
		return getStartRetentionTime() == otherRange.getStartRetentionTime() && getStopRetentionTime() == otherRange.getStopRetentionTime();
	}

	@Override
	public int hashCode() {

		return 7 * Integer.hashCode(startRetentionTime) + 11 * Integer.hashCode(stopRetentionTime);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("startRetentionTime=" + startRetentionTime);
		builder.append(",");
		builder.append("stopRetentionTime=" + stopRetentionTime);
		builder.append("]");
		return builder.toString();
	}
	// --------------------------------------equals, hashCode, toString
}
