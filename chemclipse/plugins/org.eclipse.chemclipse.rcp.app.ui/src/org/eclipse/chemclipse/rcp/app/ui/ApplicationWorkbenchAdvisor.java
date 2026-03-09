/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - exclude unrelated import/export wizards
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.app.ui;

import org.eclipse.chemclipse.support.ui.workbench.WorkbenchAdvisorSupport;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.eclipse.chemclipse.ux.extension.xxd.ui.perspective.main";

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {

		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {

		super.initialize(configurer);
		WorkbenchAdvisorSupport.declareProjectExplorerImages(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {

		return PERSPECTIVE_ID;
	}

	@Override
	public IAdaptable getDefaultPageInput() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();
	}
}