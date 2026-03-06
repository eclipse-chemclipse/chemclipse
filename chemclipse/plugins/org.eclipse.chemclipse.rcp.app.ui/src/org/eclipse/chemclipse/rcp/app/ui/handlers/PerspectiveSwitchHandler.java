/*******************************************************************************
 * Copyright (c) 2012, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui.handlers;

import org.eclipse.chemclipse.rcp.app.ui.dialogs.PerspectiveSwitcherDialog;
import org.eclipse.e4.core.di.annotations.Execute;

public class PerspectiveSwitchHandler {

	@Execute
	public void execute(PerspectiveSwitcherDialog perspectiveSwitcherDialog) {

		perspectiveSwitcherDialog.open();
	}

}
