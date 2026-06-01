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
package org.eclipse.chemclipse.model.ui;

import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.model.services.IRetentionIndexLibraryService;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator extends AbstractActivatorUI {

	private static Activator plugin;
	private ServiceTracker<IRetentionIndexLibraryService, IRetentionIndexLibraryService> retentionIndexLibraryServiceTracker = null;

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		initializePreferenceStore(PreferenceSupplier.INSTANCE());
		startServices(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		stopServices();
		super.stop(context);
	}

	public static Activator getDefault() {

		return plugin;
	}

	public Object[] getRetentionIndexLibraryServices() {

		return retentionIndexLibraryServiceTracker.getServices();
	}

	private void startServices(BundleContext context) {

		retentionIndexLibraryServiceTracker = new ServiceTracker<>(context, IRetentionIndexLibraryService.class, null);
		retentionIndexLibraryServiceTracker.open();
	}

	private void stopServices() {

		retentionIndexLibraryServiceTracker.close();
	}
}