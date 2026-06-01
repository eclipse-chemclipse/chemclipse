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
package org.eclipse.chemclipse.ux.extension.xxd.ui;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IStartup;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class LogViewSettings implements IStartup {

	private static final Logger logger = Logger.getLogger(LogViewSettings.class);

	@Override
	public void earlyStartup() {

		Preferences preferences = InstanceScope.INSTANCE.getNode("org.eclipse.ui.views.log");
		preferences.putBoolean("activate", false);
		preferences.putBoolean("activateWarn", false);
		preferences.putBoolean("activateError", false);
		try {
			preferences.flush();
		} catch(BackingStoreException e) {
			logger.error(e);
		}
	}
}