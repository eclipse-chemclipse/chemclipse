/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.EditorAreaDropAdapter;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final Logger logger = Logger.getLogger(ApplicationWorkbenchWindowAdvisor.class);

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {

		super(configurer);
		configurer.addEditorAreaTransfer(FileTransfer.getInstance());
		configurer.configureEditorAreaDropListener(new EditorAreaDropAdapter(configurer.getWindow()));
	}

	@Override
	public void preWindowOpen() {

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowProgressIndicator(true);
		disableLogViewFocus();
	}

	private void disableLogViewFocus() {

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
