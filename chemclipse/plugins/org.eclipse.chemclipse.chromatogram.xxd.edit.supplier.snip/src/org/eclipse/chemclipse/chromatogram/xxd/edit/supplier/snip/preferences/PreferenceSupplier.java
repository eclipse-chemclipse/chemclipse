/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier implements IPreferenceSupplier {

	public static final int MIN_ITERATIONS = 5;
	public static final int MAX_ITERATIONS = 2000;

	public static final int MIN_WINDOW_SIZE = 0;
	public static final int MAX_WINDOW_SIZE = 45;

	public static final double MIN_MAGNIFICATION_FACTOR = 0.01d;
	public static final double MAX_MAGNIFICATION_FACTOR = 5.0d;

	public static final int MIN_TRANSITIONS = 1;
	public static final int MAX_TRANSITIONS = 100;

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

	}
}