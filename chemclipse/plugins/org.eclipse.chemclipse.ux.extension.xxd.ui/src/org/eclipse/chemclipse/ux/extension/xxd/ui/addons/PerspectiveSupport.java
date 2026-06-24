/*******************************************************************************
 * Copyright (c) 2020, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.addons;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.activator.ContextAddon;
import org.eclipse.chemclipse.ux.extension.ui.support.DataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.toolbar.GroupHandler;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Display;

import jakarta.annotation.PostConstruct;

public class PerspectiveSupport {

	private static final Logger logger = Logger.getLogger(PerspectiveSupport.class);
	/*
	 * This flag is used to show a set of parts initially
	 * in the Data Analysis perspective.
	 */
	private static final String TOOLBAR_ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.toolbar.dataanalysis";
	private static boolean activatePartsInitially = true;

	@PostConstruct
	public void postConstruct() {

		logger.info("PerspectiveSupport has been activated.");
		handleToolbarUpdates(Activator.getDefault().getDataUpdateSupport());
	}

	private void handleToolbarUpdates(DataUpdateSupport dataUpdateSupport) {

		dataUpdateSupport.add((topic, objects) -> {

			if(topic.equals(IChemClipseEvents.TOPIC_APPLICATION_SELECT_PERSPECTIVE)) {
				Object object = objects.get(0);
				if(object instanceof String perspectiveId) {
					if(PartSupport.PERSPECTIVE_DATA_ANALYSIS.equals(perspectiveId)) {
						/*
						 * Show parts initially.
						 */
						enableToolBar(true);
						if(activatePartsInitially) {
							GroupHandler.activateReferencedParts();
							activatePartsInitially = false;
						}
					} else {
						enableToolBar(false);
					}
					GroupHandler.updateGroupHandlerMenu();
				}
			} else if(topic.equals(IChemClipseEvents.TOPIC_APPLICATION_RESET_PERSPECTIVE)) {
				Object object = objects.get(0);
				if(object instanceof String perspectiveId) {
					if(PartSupport.PERSPECTIVE_DATA_ANALYSIS.equals(perspectiveId)) {
						enableToolBar(true);
						GroupHandler.activateReferencedParts();
					} else {
						enableToolBar(false);
					}
					GroupHandler.updateGroupHandlerMenu();
				}
			} else if(topic.equals(IChemClipseEvents.TOPIC_PART_CLOSED)) {
				GroupHandler.updateGroupHandlerMenu();
			}
		});
	}

	private static void enableToolBar(boolean show) {

		EModelService modelService = ContextAddon.getModelService();
		MApplication application = ContextAddon.getApplication();

		if(modelService != null && application != null) {
			Display display = Display.getDefault();
			display.asyncExec(() -> {

				MToolBar toolBar = PartSupport.getToolBar(TOOLBAR_ID, modelService, application);
				toolBar.setToBeRendered(show);
				toolBar.setVisible(show);
			});
		}
	}
}
