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

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Creatable
public class ContextAddon {

	/**
	 * This add-on is created by the application, see Application.e4xmi.
	 * org.eclipse.chemclipse.rcp.app.ui/Application.e4xmi
	 *
	 * We plan to switch to the e4.legacy model instead of having an own application model.
	 * As soon as the migration was successful, the ContextAddon can be included via a fragment:
	 * --
	 * fragment.e4xmi
	 * org.eclipse.e4.legacy.ide.application
	 * addons
	 */
	private static ContextAddon contextAddon = null;

	@Inject
	private MApplication application;
	@Inject
	private EModelService modelService;
	@Inject
	private EPartService partService;

	@PostConstruct
	public void postConstruct() {

		contextAddon = this;
	}

	public static MApplication getApplication() {

		return (contextAddon != null) ? contextAddon.application : null;
	}

	public static EModelService getModelService() {

		return (contextAddon != null) ? contextAddon.modelService : null;
	}

	/**
	 * Be aware, this is the 'ApplicationPartServiceImpl' instance:
	 * org.eclipse.e4.ui.internal.workbench.ApplicationPartServiceImpl
	 * ---
	 * If 'PartServiceImpl' is needed use {@link #getWindowPartService()}
	 * ---
	 * Inject it, when creating a part. Have a look also at:
	 * AbstractActivatorUI.updateEPartService(EPartService partService);
	 */
	public static EPartService getPartService() {

		return (contextAddon != null) ? contextAddon.partService : null;
	}

	/**
	 * Returns the window-scoped 'PartServiceImpl'.
	 * This is the service to use when creating or showing parts, as it has a valid window context.
	 * Falls back to the application-scoped {@link #getPartService()} if no window context is available.
	 */
	public static EPartService getWindowPartService() {

		MApplication application = getApplication();
		if(application != null && !application.getChildren().isEmpty()) {
			MWindow window = application.getChildren().get(0);
			IEclipseContext context = window.getContext();
			if(context != null) {
				EPartService windowPartService = context.get(EPartService.class);
				if(windowPartService != null) {
					return windowPartService;
				}
			}
		}
		return getPartService();
	}
}
