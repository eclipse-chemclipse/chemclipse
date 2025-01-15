/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractActivatorUI {

	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {

		return plugin;
	}

	/**
	 * Returns the absolute path of the specified Folder.
	 * 
	 * @return String
	 */
	public String getSettingsPath() {

		Location location = Platform.getUserLocation();
		return location.getURL().getPath().toString();
	}

	@Override
	public IEclipseContext getEclipseContext() {

		BundleContext bundleContext = getBundle().getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		eclipseContext.set(Logger.class, null);
		return eclipseContext;
	}

	@Override
	public IEventBroker getEventBroker() {

		IEclipseContext eclipseContext = getEclipseContext();
		return eclipseContext.get(IEventBroker.class);
	}
}