/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.identifier.supplier.timeranges.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final float MIN_FACTOR = 0.0f;
	public static final float MAX_FACTOR = 100.0f;

	public static final String P_LIMIT_MATCH_FACTOR_UNKNOWN = "limitMatchFactor";
	public static final float DEF_LIMIT_MATCH_FACTOR_UNKOWN = 80.0f;
	public static final String P_MATCH_QUALITY_UNKNOWN = "matchQuality";
	public static final float DEF_MATCH_QUALITY_UNKNOWN = 80.0f;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_LIMIT_MATCH_FACTOR_UNKNOWN, Float.toString(DEF_LIMIT_MATCH_FACTOR_UNKOWN));
		putDefault(P_MATCH_QUALITY_UNKNOWN, Float.toString(DEF_MATCH_QUALITY_UNKNOWN));
	}
}