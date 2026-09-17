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
package org.eclipse.chemclipse.ux.extension.dsd.ui;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractActivatorUI {

	private static Activator plugin;
	private static BundleContext context;

	private ScopedPreferenceStore preferenceStoreSubtract;
	private DataUpdateSupport dataUpdateSupport;

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		Activator.context = context;
		plugin = this;
		initializePreferenceStore(PreferenceSupplier.INSTANCE());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		dataUpdateSupport = null;
		Activator.context = null;
		super.stop(context);
	}

	public static Activator getDefault() {

		return plugin;
	}

	public static BundleContext getContext() {

		return context;
	}

	public ScopedPreferenceStore getPreferenceStoreSubtract() {

		return preferenceStoreSubtract;
	}

	public DataUpdateSupport getDataUpdateSupport() {

		if(dataUpdateSupport == null) {
			dataUpdateSupport = new DataUpdateSupport(getEventBroker());
			initialize(dataUpdateSupport);
		}
		return dataUpdateSupport;
	}

	private void initialize(DataUpdateSupport dataUpdateSupport) {

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_APPLICATION_SELECT_PERSPECTIVE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_APPLICATION_RESET_PERSPECTIVE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PART_CLOSED, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGET_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDIT_HISTORY_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_ADJUST, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_TOOLBAR_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
	}
}
