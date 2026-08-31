/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.model;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final Logger logger = Logger.getLogger(Activator.class);

	private static Activator plugin;
	private IEclipseContext eclipseContext = null;
	private Bundle bundle;

	public Activator() {

	}

	@Override
	public void start(BundleContext context) throws Exception {

		plugin = this;
		this.bundle = context.getBundle();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
	}

	public final Bundle getBundle() {

		return bundle;
	}

	public static Activator getDefault() {

		return plugin;
	}

	public IEventBroker getEventBroker() {

		IEclipseContext eclipseContext = getEclipseContext();
		return eclipseContext.get(IEventBroker.class);
	}

	public IEclipseContext getEclipseContext() {

		if(eclipseContext == null) {
			eclipseContext = EclipseContextFactory.getServiceContext(bundle.getBundleContext());
			eclipseContext.set(Logger.class, logger);
		}

		return eclipseContext;
	}
}