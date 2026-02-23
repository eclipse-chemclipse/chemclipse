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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.app.ui.handlers;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Execute;

public class SearchHelpHandler {

	private static final Logger logger = Logger.getLogger();

	@Execute
	void execute(ECommandService commandService, EHandlerService handlerService) {

		ParameterizedCommand command = commandService.createCommand("org.eclipse.ui.help.helpSearch", null);
		if(handlerService.canExecute(command)) {
			handlerService.executeHandler(command);
		} else {
			logger.warn("Can't handle to open the help search dialog.");
		}
	}
}
