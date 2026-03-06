/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.support.ui.activator;

import java.net.URL;
import java.util.Map;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public abstract class AbstractActivatorUI extends AbstractUIPlugin {

	private static final Logger logger = Logger.getLogger(AbstractActivatorUI.class);
	/*
	 * This preference store uses the model store instead of the GUI store
	 * achieve a clean separation of concerns.
	 */
	private ScopedPreferenceStore preferenceStore;
	/*
	 * The event broker is initialized on first use.
	 */
	private IEclipseContext eclipseContext = null;

	@Override
	public void stop(BundleContext context) throws Exception {

		preferenceStore = null;
		super.stop(context);
	}

	@Override
	public IPreferenceStore getPreferenceStore() {

		if(preferenceStore == null) {
			return super.getPreferenceStore();
		}
		return preferenceStore;
	}

	/**
	 * Returns the image given by the key.
	 * Please initialize the image registry before using this function.
	 * Use the method e.g.:
	 *
	 * getImage(ICON_WARN)
	 */
	public Image getImage(String key) {

		return getImageRegistry().get(key);
	}

	public IEventBroker getEventBroker() {

		return getEclipseContext().get(IEventBroker.class);
	}

	public MApplication getApplication() {

		return getEclipseContext().get(MApplication.class);
	}

	public EModelService getModelService() {

		return getEclipseContext().get(EModelService.class);
	}

	public EPartService getPartService() {

		return getEclipseContext().get(EPartService.class);
	}

	/**
	 * If you get null pointer exceptions you may have called this too early in an Activator.
	 */
	public IEclipseContext getEclipseContext() {

		if(eclipseContext == null) {
			/*
			 * Create and initialize the context.
			 */
			eclipseContext = EclipseContextFactory.getServiceContext(getBundle().getBundleContext());
			eclipseContext.set(Logger.class, logger);
			eclipseContext.set(MApplication.class, ContextAddon.getApplication());
			eclipseContext.set(EModelService.class, ContextAddon.getModelService());
			/*
			 * Attention!
			 * This EPartService is injected and retrieved from the ContextAddon:
			 * -> org.eclipse.e4.ui.internal.workbench.ApplicationPartServiceImpl
			 * Sometimes, the OTHER part service implementation seems to be required:
			 * -> org.eclipse.e4.ui.internal.workbench.PartServiceImpl
			 */
			eclipseContext.set(EPartService.class, ContextAddon.getPartService());
			/*
			 * Checks
			 */
			MApplication application = eclipseContext.get(MApplication.class);
			if(application == null) {
				throw new NullPointerException("application");
			}

			EModelService modelService = eclipseContext.get(EModelService.class);
			if(modelService == null) {
				throw new NullPointerException("modelService");
			}

			EPartService partService = eclipseContext.get(EPartService.class);
			if(partService == null) {
				throw new NullPointerException("partService");
			}

			logger.info("The context has been initialized successfully.");
		}

		return eclipseContext;
	}

	/**
	 * Initialize the preference store.
	 */
	protected void initializePreferenceStore(IPreferenceSupplier preferenceSupplier) {

		if(preferenceSupplier != null) {
			/*
			 * Set the default values.
			 */
			preferenceStore = new ScopedPreferenceStore(preferenceSupplier.getScopeContext(), preferenceSupplier.getPreferenceNode());
			Map<String, String> initializationEntries = preferenceSupplier.getDefaultValues();
			for(Map.Entry<String, String> entry : initializationEntries.entrySet()) {
				preferenceStore.setDefault(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Initialize the image registry.
	 * Please supply a HashMap of used images/icons, e.g.:
	 *
	 * <pre><code>
	 * public static final String ICON_WARN = "ICON_WARN";
	 *
	 * Map&lt;String, String&gt; imageHashMap = new HashMap&lt;&gt;();
	 * imageHashMap.put(ICON_WARN, "icons/16x16/warn.gif");
	 * </code></pre>
	 *
	 * The icon path is set relative to the calling plugin.
	 */
	protected void initializeImageRegistry(Map<String, String> imageHashMap) {

		ImageRegistry imageRegistry = getImageRegistry();
		if(imageHashMap != null && imageRegistry != null) {
			/*
			 * Set the image/icon values.
			 */
			for(Map.Entry<String, String> entry : imageHashMap.entrySet()) {
				imageRegistry.put(entry.getKey(), createImageDescriptor(getBundle(), entry.getValue()));
			}
		}
	}

	/**
	 * Helps to create an image descriptor.
	 */
	private ImageDescriptor createImageDescriptor(Bundle bundle, String string) {

		ImageDescriptor imageDescriptor = null;
		IPath path = new Path(string);
		URL url = FileLocator.find(bundle, path, null);
		imageDescriptor = ImageDescriptor.createFromURL(url);
		return imageDescriptor;
	}
}