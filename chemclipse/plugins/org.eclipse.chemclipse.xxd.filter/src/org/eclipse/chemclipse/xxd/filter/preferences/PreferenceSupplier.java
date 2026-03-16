/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final double MIN_CONCENTRATION = 0;
	public static final double MAX_CONCENTRATION = Double.MAX_VALUE;
	public static final float MIN_FACTOR = 0.0f;
	public static final float MAX_FACTOR = 100.0f;

	public static final String P_NUMBER_LOWEST = "numberLowest";
	public static final int DEF_NUMBER_LOWEST = 5;
	public static final int MIN_NUMBER_LOWEST = 1;
	public static final int MAX_NUMBER_LOWEST = Integer.MAX_VALUE;

	public static final String P_NUMBER_HIGHEST = "numberHighest";
	public static final int DEF_NUMBER_HIGHEST = 5;
	public static final int MIN_NUMBER_HIGHEST = 1;
	public static final int MAX_NUMBER_HIGHEST = Integer.MAX_VALUE;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_NUMBER_LOWEST, Integer.toString(DEF_NUMBER_LOWEST));
		putDefault(P_NUMBER_HIGHEST, Integer.toString(DEF_NUMBER_HIGHEST));
	}
}