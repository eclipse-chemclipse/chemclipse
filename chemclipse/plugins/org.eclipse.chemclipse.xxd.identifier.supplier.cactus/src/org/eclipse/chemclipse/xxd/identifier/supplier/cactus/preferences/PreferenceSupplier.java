/*******************************************************************************
 * Copyright (c) 2025, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.identifier.supplier.cactus.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	public static final String P_LINEWIDTH = "cactusLinewidth";
	public static final int DEF_LINEWIDTH = 2;

	public static final String P_SYMBOL_FONTSIZE = "cactusSymbolFontSize";
	public static final int DEF_SYMBOL_FONTSIZE = 16;

	private static IPreferenceSupplier preferenceSupplier = null;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_LINEWIDTH, DEF_LINEWIDTH);
		putDefault(P_SYMBOL_FONTSIZE, DEF_SYMBOL_FONTSIZE);
	}

	public static int getLineWidth() {

		return INSTANCE().getInteger(P_LINEWIDTH, DEF_LINEWIDTH);
	}

	public static int getSymbolFontSize() {

		return INSTANCE().getInteger(P_SYMBOL_FONTSIZE, DEF_SYMBOL_FONTSIZE);
	}
}