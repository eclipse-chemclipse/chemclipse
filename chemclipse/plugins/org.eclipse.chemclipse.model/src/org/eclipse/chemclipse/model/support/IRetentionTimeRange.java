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
 * Christoph Läubrich - add contentEquals / containsRetentionTime / javadoc
 *******************************************************************************/
package org.eclipse.chemclipse.model.support;

public interface IRetentionTimeRange {

	/**
	 * @return the start retention time in milliseconds or -1 if the start is unknown
	 */
	int getStartRetentionTime();

	/**
	 * @return the stop retention time in milliseconds or -1 if the stop is unknown
	 */
	int getStopRetentionTime();

	/**
	 * Compares this objects content to the other objects content, the default implementation compares {@link #getStartRetentionTime()}, {@link #getStopRetentionTime()}
	 * this method is different to {@link java.lang.Object#equals(Object)} that it does compares for user visible properties to be equal in contrast to objects identity and it allows to compare different instance type, this also means that it is not required that
	 * Object1.contentEquals(Object2} == Object2.contentEquals(Object1}
	 */
	default boolean contentEquals(IRetentionTimeRange other) {

		if(other == null) {
			return false;
		}
		if(this == other) {
			return true;
		}
		return getStartRetentionTime() == other.getStartRetentionTime() && getStopRetentionTime() == other.getStopRetentionTime();
	}

	/**
	 * Check if the given retention time is contained within this range
	 *
	 * @param retentionTime
	 *            the retention time (in milliseconds) to check
	 * @return <code>true</code> if retention time is within bounds, <code>false</code> otherwise
	 *
	 */
	default boolean containsRetentionTime(int retentionTime) {

		return retentionTime >= getStartRetentionTime() && retentionTime <= getStopRetentionTime();
	}
}
