/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.converter.model.reports;

public interface IReportPeak {

	int getRetentionTime();

	void setRetentionTime(int retentionTime);

	int getStartRetentionTime();

	void setStartRetentionTime(int startRetentionTime);

	int getStopRetentionTime();

	void setStopRetentionTime(int stopRetentionTime);

	double getArea();

	void setArea(double area);

	String getSubstance();

	void setSubstance(String substance);

	String getUnits();

	void setUnits(String units);

	@Override
	String toString();
}