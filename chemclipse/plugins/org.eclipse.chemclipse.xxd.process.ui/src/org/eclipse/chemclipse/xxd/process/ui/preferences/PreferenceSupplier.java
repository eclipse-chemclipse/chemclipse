/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.process.ui.preferences;

import org.eclipse.chemclipse.support.preferences.AbstractPreferenceSupplier;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.osgi.framework.FrameworkUtil;

public class PreferenceSupplier extends AbstractPreferenceSupplier {

	/*
	 * Processors that shall not appear in the chart context menus and the main menu.
	 * Uses the same persistence format as the processor quick-access toolbar.
	 */
	public static final String P_MENU_BLACKLIST_PROCESSORS = "menuBlacklistProcessors";
	public static final String DEF_MENU_BLACKLIST_PROCESSORS = "";

	public static IPreferenceSupplier INSTANCE() {

		return INSTANCE(PreferenceSupplier.class);
	}

	@Override
	public String getPreferenceNode() {

		return FrameworkUtil.getBundle(PreferenceSupplier.class).getSymbolicName();
	}

	@Override
	public void initializeDefaults() {

		putDefault(P_MENU_BLACKLIST_PROCESSORS, DEF_MENU_BLACKLIST_PROCESSORS);
	}

	public static String getMenuBlacklistProcessors() {

		return INSTANCE().get(P_MENU_BLACKLIST_PROCESSORS);
	}

	public static void setMenuBlacklistProcessors(String processors) {

		INSTANCE().put(P_MENU_BLACKLIST_PROCESSORS, processors);
	}
}
