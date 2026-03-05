/*******************************************************************************
 * Copyright (c) 2021, 2026 Lablicate GmbH.
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
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

public class ResetPerspectiveHandler {

	@Execute
	public void execute(MWindow window, IEventBroker eventBroker, IWorkbenchWindow workbenchWindow) {

		IWorkbenchPage page = workbenchWindow.getActivePage();
		page.resetPerspective();
		eventBroker.post(IChemClipseEvents.TOPIC_APPLICATION_RESET_PERSPECTIVE, page.getPerspective().getLabel());
	}
}