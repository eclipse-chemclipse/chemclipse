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
package org.eclipse.chemclipse.model.support;

/**
 * This class can be used to represent a valid background abundance range.<br/>
 * It evaluates the constructor parameters and corrects them to a valid state.
 */
public class BackgroundAbundanceRange implements IBackgroundAbundanceRange {

	private float startBackgroundAbundance;
	private float stopBackgroundAbundance;

	/**
	 * Creates a new BackgroundAbundanceRange object. There are some
	 * limitations:<br/>
	 * The startBackgroundAbundance may not be &lt; 0 and &gt; MAX_BACKGROUND_ABUNDANCE. In such
	 * a case it will be set to MIN_BACKGROUND_ABUNDANCE. The stopBackgroundAbundance
	 * should be &lt;= MAX_BACKGROUND_ABUNDANCE and &gt;= 0 otherwise it will be set to
	 * MAX_BACKGROUND_ABUNDANCE.
	 * 
	 * @param startBackgroundAbundance
	 * @param stopBackgroundAbundance
	 */
	public BackgroundAbundanceRange(float startBackgroundAbundance, float stopBackgroundAbundance) {

		if(startBackgroundAbundance < MIN_BACKGROUND_ABUNDANCE || startBackgroundAbundance > MAX_BACKGROUND_ABUNDANCE) {
			startBackgroundAbundance = MIN_BACKGROUND_ABUNDANCE;
		}
		if(stopBackgroundAbundance > MAX_BACKGROUND_ABUNDANCE || stopBackgroundAbundance < MIN_BACKGROUND_ABUNDANCE) {
			stopBackgroundAbundance = MAX_BACKGROUND_ABUNDANCE;
		}
		this.startBackgroundAbundance = startBackgroundAbundance;
		this.stopBackgroundAbundance = stopBackgroundAbundance;
	}

	@Override
	public float getStartBackgroundAbundance() {

		return startBackgroundAbundance;
	}

	@Override
	public float getStopBackgroundAbundance() {

		return stopBackgroundAbundance;
	}

	// -----------------------------equals, hashCode, toString
	@Override
	public boolean equals(Object other) {

		if(this == other) {
			return true;
		}
		if(other == null) {
			return false;
		}
		if(this.getClass() != other.getClass()) {
			return false;
		}
		BackgroundAbundanceRange otherRange = (BackgroundAbundanceRange)other;
		return getStartBackgroundAbundance() == otherRange.getStartBackgroundAbundance() && getStopBackgroundAbundance() == otherRange.getStopBackgroundAbundance();
	}

	@Override
	public int hashCode() {

		return 7 * Float.hashCode(getStartBackgroundAbundance()) + 11 * Float.hashCode(getStopBackgroundAbundance());
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("startBackgroundAbundance=" + getStartBackgroundAbundance());
		builder.append(",");
		builder.append("stopBackgroundAbundance=" + getStopBackgroundAbundance());
		builder.append("]");
		return builder.toString();
	}
	// -----------------------------equals, hashCode, toString
}
