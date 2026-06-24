/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui.addons;

import java.util.Properties;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import jakarta.annotation.PostConstruct;

public class PerspectiveApplicationAddon {

	@PostConstruct
	public void postConstruct(MApplication application, EModelService modelService, IEventBroker eventBroker) {

		/*
		 * The default perspective can be defined in the product definition, e.g.:
		 * -Dapplication.perspective=org.eclipse.chemclipse.chromatogram.xxd.integrator.ui.perspective.main
		 * If no value has been set, the default perspective will.
		 */
		String perspectiveId;
		Properties properties = System.getProperties();
		Object value = properties.get("application.perspective");
		if(value != null && value instanceof String text) {
			perspectiveId = text;
		} else {
			perspectiveId = IPerspectiveAndViewIds.PERSPECTIVE_WELCOME;
		}
		/*
		 * The Bug #408678 has been fixed since Eclipse 4.3.2
		 */
		MPerspective perspective = (MPerspective)modelService.find(perspectiveId, application);
		if(perspective == null) {
			perspectiveId = IPerspectiveAndViewIds.PERSPECTIVE_WELCOME;
			perspective = (MPerspective)modelService.find(perspectiveId, application);
		}

		MPerspectiveStack perspectiveStack = (MPerspectiveStack)modelService.find(IPerspectiveAndViewIds.STACK_PERSPECTIVES, application);
		perspectiveStack.setSelectedElement(perspective);
		if(eventBroker != null) {
			eventBroker.send(IChemClipseEvents.TOPIC_APPLICATION_SELECT_PERSPECTIVE, perspective.getElementId());
			scheduleSnapshot(application, modelService, perspectiveStack, eventBroker);
		}
	}

	/*
	 * The perspectives are snapshotted as snippets so that the reset perspective command can clone
	 * them later. Snapshotting is deferred until the UI has been rendered, because adding snippets
	 * while the model is still being processed disturbs the initial trim layout.
	 */
	private void scheduleSnapshot(MApplication application, EModelService modelService, MPerspectiveStack perspectiveStack, IEventBroker eventBroker) {

		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, new EventHandler() {

			@Override
			public void handleEvent(Event event) {

				eventBroker.unsubscribe(this);
				snapshotPerspectives(application, modelService, perspectiveStack);
			}
		});
	}

	private void snapshotPerspectives(MApplication application, EModelService modelService, MPerspectiveStack perspectiveStack) {

		for(MPerspective perspective : perspectiveStack.getChildren()) {
			String perspectiveId = perspective.getElementId();
			if(perspectiveId == null || perspectiveId.isEmpty()) {
				continue;
			}
			/*
			 * The model is rebuilt from the fragments each start and the snapshot is always pristine.
			 */
			if(modelService.findSnippet(application, perspectiveId) == null) {
				modelService.cloneElement(perspective, application);
			}
		}
	}
}
