/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - init DataUpdateSupport on first access
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui;

import java.util.Map;

import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.support.ui.activator.AbstractActivatorUI;
import org.eclipse.chemclipse.swt.ui.services.IMoleculeImageService;
import org.eclipse.chemclipse.swt.ui.services.IScanIdentifierService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.preferences.PreferenceSupplierModelMSD;
import org.eclipse.chemclipse.ux.extension.xxd.ui.methods.IAnnotationWidgetService;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.services.IEditorService;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivatorUI {

	private static Activator plugin;
	private static BundleContext context;
	//
	private ScopedPreferenceStore preferenceStoreSubtract;
	private DataUpdateSupport dataUpdateSupport;
	//
	private ServiceTracker<IMoleculeImageService, IMoleculeImageService> moleculeImageServiceTracker = null;
	private ServiceTracker<IAnnotationWidgetService, IAnnotationWidgetService> annotationWidgetServiceTracker = null;
	private ServiceTracker<IScanIdentifierService, IScanIdentifierService> scanIdentifierServiceTracker = null;
	private ServiceTracker<IEditorService, IEditorService> editorServiceTracker = null;
	private static ServiceTracker<IProcessSupplierContext, IProcessSupplierContext> processSupplierServiceTracker;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		Activator.context = context;
		plugin = this;
		initializePreferenceStore(PreferenceSupplier.INSTANCE());
		initializePreferenceStoreSubtract(PreferenceSupplierModelMSD.INSTANCE());
		startServices(context);
		/*
		 * Don't call here:
		 * ---
		 * getDataUpdateSupport()
		 * getEclipseContext()
		 * ---
		 * The context is initialized, but the application values are not available yet.
		 */
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		dataUpdateSupport = null;
		Activator.context = null;
		stopServices();
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

	public static BundleContext getContext() {

		return context;
	}

	public ScopedPreferenceStore getPreferenceStoreSubtract() {

		return preferenceStoreSubtract;
	}

	public String getSettingsPath() {

		Location location = Platform.getUserLocation();
		return location.getURL().getPath();
	}

	public DataUpdateSupport getDataUpdateSupport() {

		if(dataUpdateSupport == null) {
			dataUpdateSupport = new DataUpdateSupport(getEventBroker());
			initialize(dataUpdateSupport);
		}
		return dataUpdateSupport;
	}

	public Object[] getMoleculeImageServices() {

		return moleculeImageServiceTracker.getServices();
	}

	public Object[] getAnnotationWidgetServices() {

		return annotationWidgetServiceTracker.getServices();
	}

	public Object[] getScanIdentifierServices() {

		return scanIdentifierServiceTracker.getServices();
	}

	public Object[] getEditorServices() {

		return editorServiceTracker.getServices();
	}

	public static IProcessSupplierContext getProcessSupplierContext() {

		if(processSupplierServiceTracker != null) {
			return processSupplierServiceTracker.getService();
		} else {
			return null;
		}
	}

	private void startServices(BundleContext context) {

		moleculeImageServiceTracker = new ServiceTracker<>(context, IMoleculeImageService.class, null);
		moleculeImageServiceTracker.open();
		//
		annotationWidgetServiceTracker = new ServiceTracker<>(context, IAnnotationWidgetService.class, null);
		annotationWidgetServiceTracker.open();
		//
		scanIdentifierServiceTracker = new ServiceTracker<>(context, IScanIdentifierService.class, null);
		scanIdentifierServiceTracker.open();
		//
		editorServiceTracker = new ServiceTracker<>(context, IEditorService.class, null);
		editorServiceTracker.open();
		//
		processSupplierServiceTracker = new ServiceTracker<>(context, IProcessSupplierContext.class, null);
		processSupplierServiceTracker.open();
	}

	private void stopServices() {

		moleculeImageServiceTracker.close();
		annotationWidgetServiceTracker.close();
		scanIdentifierServiceTracker.close();
		editorServiceTracker.close();
		processSupplierServiceTracker.close();
	}

	private void initialize(DataUpdateSupport dataUpdateSupport) {

		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_RAWFILE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_RAWFILE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_RAWFILE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_RAWFILE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_RAWFILE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_NONE, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_LITERATURE_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_MSD_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_CSD_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_WSD_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_OVERVIEW, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_VSD_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_NMR_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_APPLICATION_SELECT_PERSPECTIVE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_APPLICATION_RESET_PERSPECTIVE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PART_CLOSED, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_WELL_PCR_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_PLATE_PCR_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGET_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGETS_UPDATE_SELECTION, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_UPDATE_SESSION_SUBTRACT_MASS_SPECTRUM, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_QUANT_DB_COMPOUND_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_TARGET_UPDATE_COMPARISON, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_SCAN_REFERENCE_UPDATE_COMPARISON, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDIT_HISTORY_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_ADJUST, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_LIBRARY_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_PCR_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_NMR_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_WSD_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_VSD_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_FSD_CLOSE, IChemClipseEvents.EVENT_BROKER_DATA);
		//
		dataUpdateSupport.subscribe(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_TOOLBAR_UPDATE, IChemClipseEvents.EVENT_BROKER_DATA);
	}

	private void initializePreferenceStoreSubtract(IPreferenceSupplier preferenceSupplier) {

		if(preferenceSupplier != null) {
			/*
			 * Set the default values.
			 */
			preferenceStoreSubtract = new ScopedPreferenceStore(preferenceSupplier.getScopeContext(), preferenceSupplier.getPreferenceNode());
			Map<String, String> initializationEntries = preferenceSupplier.getDefaultValues();
			for(Map.Entry<String, String> entry : initializationEntries.entrySet()) {
				preferenceStoreSubtract.setDefault(entry.getKey(), entry.getValue());
			}
		}
	}
}
