/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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

import java.util.Set;

public interface IChromatogramBaseline {

	/*
	 * Do not translate this ID.
	 */
	String DEFAULT_BASELINE_ID = "Default"; //$NON-NLS-1$

	/**
	 * Returns the active baseline model.<br/>
	 * You can modify the baseline through this baseline model.
	 */
	IBaselineModel getBaselineModel();

	/**
	 * Return the set of available baseline ids.
	 */
	Set<String> getBaselineIds();

	/**
	 * Returns the active baseline id.
	 */
	String getActiveBaseline();

	/**
	 * Activates the default baseline.
	 */
	void setActiveBaselineDefault();

	/**
	 * Empty or null is not allowed.
	 */
	void setActiveBaseline(String id);

	/**
	 * Removes the given baseline id.
	 * The default id can't be removed.
	 */
	void removeBaseline(String id);
}