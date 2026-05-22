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
package org.eclipse.chemclipse.support.ui.workbench;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.PlatformUI;

public class PreferencesSupport {

	public static void cleanPreferencesByPrefix(List<String> preservePreferencePrefixes) {

		List<String> preservePreferenceNodes = new ArrayList<>();
		cleanPreferences(preservePreferencePrefixes, preservePreferenceNodes);
	}

	public static void cleanPreferencesByNodeId(List<String> preservePreferenceNodes) {

		List<String> preservePreferencePrefixes = new ArrayList<>();
		cleanPreferences(preservePreferencePrefixes, preservePreferenceNodes);
	}

	// Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=564022
	public static boolean isDarkTheme() {

		return Platform.getPreferencesService().getString("org.eclipse.e4.ui.css.swt.theme", "themeid", "", null).endsWith("dark");
	}

	/**
	 * Cleans the preference tree, e.g.:
	 * {@snippet :
	 *
	 * List<String> preservePreferencePrefixes = new ArrayList<String>();
	 * preservePreferencePrefixes.add("org.eclipse.chemclipse");
	 *
	 * List<String> preservePreferenceNodes = new ArrayList<String>();
	 * preservePreferenceNodes.add("org.eclipse.equinox.internal.p2.ui.sdk.ProvisioningPreferencePage"); // Install/Update
	 * preservePreferenceNodes.add("org.eclipse.equinox.internal.p2.ui.sdk.SitesPreferencePage"); // Available Software Sites
	 * preservePreferenceNodes.add("org.eclipse.equinox.internal.p2.ui.sdk.scheduler.AutomaticUpdatesPreferencePage"); // Automatic Updates
	 * }
	 */
	public static void cleanPreferences(List<String> preservePreferencePrefixes, List<String> preservePreferenceNodes) {

		PreferenceManager preferenceManager = PlatformUI.getWorkbench().getPreferenceManager();
		for(IPreferenceNode preferenceNode : preferenceManager.getElements(PreferenceManager.POST_ORDER)) {
			String preferenceNodeId = preferenceNode.getId();
			if(!nodeIdStartsWithPrefix(preferenceNodeId, preservePreferencePrefixes) && !preservePreferenceNodes.contains(preferenceNodeId)) {
				preferenceManager.remove(preferenceNode);
			}
		}
	}

	private static boolean nodeIdStartsWithPrefix(String preferenceNodeId, List<String> preservePreferencePrefixes) {

		if(preservePreferencePrefixes.isEmpty()) {
			return false;
		} else {
			for(String preservePreferencePrefix : preservePreferencePrefixes) {
				if(preferenceNodeId.startsWith(preservePreferencePrefix)) {
					return true;
				}
			}
			return false;
		}
	}
}
