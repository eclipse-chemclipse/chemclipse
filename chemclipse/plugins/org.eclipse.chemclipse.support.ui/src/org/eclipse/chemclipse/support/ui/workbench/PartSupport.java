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

import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import jakarta.inject.Inject;

/**
 * A helper class that can be injected into E4 parts to perform common tasks
 *
 * @author Christoph Läubrich
 *
 */
@Creatable
public class PartSupport {

	@Inject
	private MApplication mApplication;
	@Inject
	private EModelService eModelService;
	@Inject
	private EPartService ePartService;
	@Inject
	private IEventBroker eventBroker;

	/**
	 * Load and show the part.
	 */
	public void focusPart(String partId) {

		MUIElement element = eModelService.find(partId, mApplication);
		if(element instanceof MPart part) {
			if(!ePartService.getParts().contains(part)) {
				ePartService.createPart(part.getElementId());
			}
			ePartService.showPart(part, PartState.ACTIVATE);
			if(eventBroker != null) {
				eventBroker.send(IChemClipseEvents.TOPIC_APPLICATION_SELECT_VIEW, part.getLabel());
			}
		}
	}

	public void closePart(MPart part) {

		if(part == null) {
			return;
		}

		part.setToBeRendered(false);
		part.setVisible(false);

		DisplayUtils.getDisplay().asyncExec(() -> {
			MPartStack partStack = (MPartStack)eModelService.find(IPerspectiveAndViewIds.EDITOR_PART_STACK_ID, mApplication);
			partStack.getChildren().remove(part);
		});
	}
}
