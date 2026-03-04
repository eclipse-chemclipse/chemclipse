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

import org.eclipse.chemclipse.support.events.IPerspectiveAndViewIds;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

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
