/*******************************************************************************
 * Copyright (c) 2015, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.xxd.filter.ui;

import org.eclipse.chemclipse.numeric.services.IMaximaDetectorService;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivatorUI {

	private static Activator plugin;
	private ServiceTracker<IMaximaDetectorService, IMaximaDetectorService> maximaDetectorServiceTracker = null;

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		maximaDetectorServiceTracker = new ServiceTracker<>(context, IMaximaDetectorService.class, null);
		maximaDetectorServiceTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		maximaDetectorServiceTracker.close();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {

		return plugin;
	}

	public Object[] getMaximaDetectorServices() {

		return maximaDetectorServiceTracker.getServices();
	}
}
