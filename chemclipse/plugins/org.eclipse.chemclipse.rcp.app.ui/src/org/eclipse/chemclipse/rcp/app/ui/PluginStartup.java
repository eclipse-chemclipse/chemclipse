/*******************************************************************************
 * Copyright (c) 2023, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.rcp.app.ui;

import org.eclipse.chemclipse.rcp.app.ui.console.MessageConsoleSupport;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

public class PluginStartup implements IStartup {

	@Override
	public void earlyStartup() {

		Display.getDefault().asyncExec(() -> {

			MessageConsoleSupport consoleSupport = new MessageConsoleSupport();
			consoleSupport.printConfiguration();
		});
	}
}