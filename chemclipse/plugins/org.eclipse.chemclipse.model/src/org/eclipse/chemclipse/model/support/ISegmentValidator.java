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

public interface ISegmentValidator {

	default boolean acceptSegment(float[] values, double mean) {

		double[] doubles = new double[values.length];
		for(int i = 0; i < doubles.length; i++) {
			doubles[i] = values[i];
		}
		//
		return acceptSegment(doubles, mean);
	}

	boolean acceptSegment(double[] values, double mean);
}