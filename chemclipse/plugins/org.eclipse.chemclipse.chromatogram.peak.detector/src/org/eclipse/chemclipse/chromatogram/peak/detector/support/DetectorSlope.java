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
package org.eclipse.chemclipse.chromatogram.peak.detector.support;

import org.eclipse.chemclipse.numeric.core.IPoint;
import org.eclipse.chemclipse.numeric.geometry.Slope;

public class DetectorSlope extends Slope implements IDetectorSlope {

	private int retentionTime;

	public DetectorSlope(IPoint p1, IPoint p2, int retentionTime) {

		super(p1, p2);
		this.retentionTime = retentionTime;
	}

	@Override
	public int getRetentionTime() {

		return retentionTime;
	}

	// --------------equals, hashCode, toString
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
		DetectorSlope otherSlope = (DetectorSlope)other;
		return getRetentionTime() == otherSlope.getRetentionTime();
	}

	@Override
	public int hashCode() {

		return 7 * Integer.hashCode(retentionTime);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("retentionTime=" + retentionTime);
		builder.append("]");
		return builder.toString();
	}
	// --------------equals, hashCode, toString
}
