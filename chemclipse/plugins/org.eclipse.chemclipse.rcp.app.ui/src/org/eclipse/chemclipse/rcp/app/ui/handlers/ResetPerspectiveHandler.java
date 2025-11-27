/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui.handlers;

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import jakarta.inject.Inject;

public class ResetPerspectiveHandler {

	@Inject
	private EModelService modelService;

	@Inject
	private IEventBroker eventBroker;
	
    @Inject
    private EPartService partService;

	@Execute
	public void execute(MWindow window) {

		MUIElement activePerspective = modelService.getActivePerspective(window);

		if(activePerspective instanceof MPerspective currentPerspective) {
			eventBroker.post(IChemClipseEvents.TOPIC_APPLICATION_RESET_PERSPECTIVE, currentPerspective.getLabel());
			modelService.resetPerspectiveModel(currentPerspective, window);
			for(MPlaceholder placeholder : modelService.findElements(currentPerspective, null, MPlaceholder.class, null)) {
				if(placeholder.getRef() != null) {
					String partId = placeholder.getRef().getElementId();
					partService.showPart(partId, PartState.CREATE);
				}
			}
			
		}
	}
}