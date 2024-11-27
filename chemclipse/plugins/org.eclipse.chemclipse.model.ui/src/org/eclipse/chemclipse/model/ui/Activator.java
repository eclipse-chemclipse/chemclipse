/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		initializePreferenceStore(PreferenceSupplier.INSTANCE());
		startServices(context);
	}

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