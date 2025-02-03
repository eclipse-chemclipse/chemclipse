/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.pcr.ui;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.eclipse.chemclipse.ux.extension.pcr.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractActivatorUI {

	private static Activator plugin;
	private DataUpdateSupport dataUpdateSupport;

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		initializePreferenceStore(PreferenceSupplier.INSTANCE());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		dataUpdateSupport = null;
		super.stop(context);
	}

	public static Activator getDefault() {

		return plugin;
	}

	public DataUpdateSupport getDataUpdateSupport() {

		if(dataUpdateSupport == null) {
			dataUpdateSupport = new DataUpdateSupport(getEventBroker());
			initialize(dataUpdateSupport);
		}
		return dataUpdateSupport;
	}

	private void initialize(DataUpdateSupport dataUpdateSupport) {

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_WELL_PCR_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
	}

}