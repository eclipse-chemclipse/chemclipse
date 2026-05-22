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
package org.eclipse.chemclipse.msd.model.xic;

/**
 * This class can be used to represent a valid ion range.<br/>
 * It evaluates the constructor parameters and corrects them to a valid state.
 */
public class IonRange implements IIonRange {

	private int startIon;
	private int stopIon;

	/**
	 * Creates a new IonRange object. There are some limitations:<br/>
	 * The startIon may not be &lt;= 0 and &gt; MAX_Ion. In such a case it
	 * will be set to MIN_Ion. The stopIon should be &lt;= MAX_Ion and &gt; 0
	 * otherwise it will be set to MAX_Ion.
	 */
	public IonRange(int startIon, int stopIon) {

		if(startIon > stopIon) {
			int tmp = startIon;
			startIon = stopIon;
			stopIon = tmp;
		}
		if(startIon < MIN_ION || startIon > MAX_ION) {
			startIon = MIN_ION;
		}
		if(stopIon > MAX_ION || stopIon < MIN_ION) {
			stopIon = MAX_ION;
		}
		this.startIon = startIon;
		this.stopIon = stopIon;
	}

	@Override
	public int getStartIon() {

		return startIon;
	}

	@Override
	public int getStopIon() {

		return stopIon;
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
		IonRange otherRange = (IonRange)other;
		return getStartIon() == otherRange.getStartIon() && getStopIon() == otherRange.getStopIon();
	}

	@Override
	public int hashCode() {

		return 7 * Integer.valueOf(getStartIon()).hashCode() + 11 * Integer.valueOf(getStopIon()).hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("startIon=" + getStartIon());
		builder.append(",");
		builder.append("stopIon=" + getStopIon());
		builder.append("]");
		return builder.toString();
	}
	// -----------------------------equals, hashCode, toString
}
