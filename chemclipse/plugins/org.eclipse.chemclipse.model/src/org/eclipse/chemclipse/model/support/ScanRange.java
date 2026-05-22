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

public class ScanRange implements IScanRange {

	private int startScan;
	private int stopScan;

	/**
	 * Creates a new ScanRange object. There are some limitations:<br/>
	 * The startScan may not be &lt;= 1 and &gt; MAX_SCAN. In such a case it will be
	 * set to MIN_SCAN. The stopScan should be &lt;= MAX_SCAN and &gt; 0 otherwise it
	 * will be set to MAX_SCAN.
	 */
	public ScanRange(int startScan, int stopScan) {

		if(startScan > stopScan) {
			int tmp = startScan;
			startScan = stopScan;
			stopScan = tmp;
		}
		if(startScan < MIN_SCAN || startScan > MAX_SCAN) {
			startScan = MIN_SCAN;
		}
		if(stopScan > MAX_SCAN || stopScan < MIN_SCAN) {
			stopScan = MAX_SCAN;
		}
		this.startScan = startScan;
		this.stopScan = stopScan;
	}

	@Override
	public int getStartScan() {

		return startScan;
	}

	@Override
	public int getStopScan() {

		return stopScan;
	}

	@Override
	public int getWidth() {

		return stopScan - startScan + 1;
	}

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
		ScanRange otherRange = (ScanRange)other;
		return getStartScan() == otherRange.getStartScan() && getStopScan() == otherRange.getStopScan();
	}

	@Override
	public int hashCode() {

		return 7 * Integer.valueOf(getStartScan()).hashCode() + 11 * Integer.valueOf(getStopScan()).hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("startScan=" + getStartScan());
		builder.append(",");
		builder.append("stopScan=" + getStopScan());
		builder.append("]");
		return builder.toString();
	}
}