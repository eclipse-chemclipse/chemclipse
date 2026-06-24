/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.workbench;

import java.util.List;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import jakarta.inject.Inject;

/**
 * A helper class that can be injected into E4 parts to perform common tasks related to perspectives
 *
 * @author Christoph Läubrich
 *
 */
@Creatable
public class PerspectiveSupport {

	@Inject
	private EModelService eModelService;
	@Inject
	private EPartService ePartService;
	@Inject
	private MApplication mApplication;
	@Inject
	private IEventBroker eventBroker;

	public String getActivePerspectiveId() {

		MPerspective perspective = getActiveMPerspective();
		if(perspective != null) {
			return perspective.getElementId();
		} else {
			return "";
		}
	}

	private MPerspective getActiveMPerspective() {

		List<MPerspectiveStack> perspectiveStacks = eModelService.findElements(mApplication, null, MPerspectiveStack.class);
		if(!perspectiveStacks.isEmpty()) {
			MPerspectiveStack perspectiveStack = perspectiveStacks.get(0);
			return perspectiveStack.getSelectedElement();
		}
		return null;
	}

	/**
	 * Try to load the perspective.
	 *
	 * @param perspectiveId
	 */
	public void changePerspective(String perspectiveId) {

		MPerspective perspectiveModel = getPerspectiveModel(perspectiveId);
		if(perspectiveModel != null) {
			MPerspective activePerspective = getActiveMPerspective();
			if(perspectiveModel.equals(activePerspective)) {
				return;
			}
			ePartService.switchPerspective(perspectiveModel);
			eventBroker.post(IChemClipseEvents.TOPIC_APPLICATION_SELECT_PERSPECTIVE, perspectiveModel.getElementId());
		}
	}

	public MPerspective getPerspectiveModel(String perspectiveId) {

		if(perspectiveId != null) {
			List<MPerspective> elements = eModelService.findElements(mApplication, perspectiveId, MPerspective.class);
			if(elements != null && !elements.isEmpty()) {
				for(MPerspective perspective : elements) {
					String elementId = perspective.getElementId();
					String elementLabel = perspective.getLabel();
					if(perspectiveId.equals(elementId) || elementId.equals(perspectiveId + "." + elementLabel)) {
						return perspective;
					}
				}
			}
		}
		return null;
	}
}
