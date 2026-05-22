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

import org.eclipse.chemclipse.support.traces.ITrace;

/**
 * Defines the values for a baseline segment, e.g. start and stop retention
 * time, start and stop background abundance.
 */
public interface IBaselineSegment {

	int getStartRetentionTime();

	/**
	 * The start retention time must be &gt;= 0 and must be =&lt; stopRetentionTime.
	 */
	void setStartRetentionTime(int startRetentionTime);

	float getStartBackgroundAbundance();

	/**
	 * The start background abundance time must be &gt;= 0.
	 */
	void setStartBackgroundAbundance(float startBackgroundAbundance);

	int getStopRetentionTime();

	/**
	 * The stop retention time must be &gt;= 0 and must be &gt;= startRetentionTime.
	 */
	void setStopRetentionTime(int stopRetentionTime);

	float getStopBackgroundAbundance();

	/**
	 * The stop background abundance time must be &gt;= 0.
	 */
	void setStopBackgroundAbundance(float stopBackgroundAbundance);

	float getBackgroundAbundance(int retentionTime);

	ITrace getTrace();
}