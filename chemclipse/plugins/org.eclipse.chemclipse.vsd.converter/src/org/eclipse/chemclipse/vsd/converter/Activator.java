/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.vsd.converter;

import org.eclipse.chemclipse.vsd.converter.service.IConverterServiceVSD;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static Activator plugin;
	private static BundleContext context;
	private ServiceTracker<IConverterServiceVSD, IConverterServiceVSD> converterServiceTracker = null;

	public static BundleContext getContext() {

		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {

		Activator.context = bundleContext;
		plugin = this;
		converterServiceTracker = new ServiceTracker<>(context, IConverterServiceVSD.class, null);
		converterServiceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		converterServiceTracker.close();
		Activator.context = null;
	}

	public static Activator getDefault() {

		return plugin;
	}

	public Object[] getConverterServices() {

		return converterServiceTracker.getServices();
	}
}