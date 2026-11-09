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
 * Jan Holy - initial API and implementation
 * Philip Wenig - improvements
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

public interface IRetentionTime extends IVariable {

	/*
	 * Retention time in minutes
	 */
	String TYPE = "Retention Time [min]"; //$NON-NLS-1$

	int getRetentionTime();

	double getRetentionTimeMinutes();

	void setRetentionTime(int retentionTime);
}
