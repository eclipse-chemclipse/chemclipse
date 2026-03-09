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
package org.eclipse.chemclipse.ux.extension.ui.handlers;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChemClipseEditor;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import jakarta.inject.Named;

public class SaveAsHandler {

	private static final Logger logger = Logger.getLogger(SaveAsHandler.class);

	@CanExecute
	boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart part) {

		if(part != null) {
			if(part.getObject() instanceof IChemClipseEditor || part.getElementId().equals(PartSupport.COMPATIBILITY_EDITOR_ELEMENT_ID)) {
				return true;
			}
		}
		return false;
	}

	@Execute
	void execute(ECommandService commandService, EHandlerService handlerService, @Named(IServiceConstants.ACTIVE_PART) MPart part) {

		if(part != null) {
			Object object = part.getObject();
			if(object != null) {
				/*
				 * Save the data as ...
				 */
				if(object instanceof IChemClipseEditor editor) {
					editor.saveAs();
				} else {
					ParameterizedCommand command = commandService.createCommand("org.eclipse.ui.file.saveAs"); // $NON-NLS-1$
					if(handlerService.canExecute(command)) {
						handlerService.executeHandler(command);
					} else {
						logger.warn("Couldn't run the Save As... command.");
					}
				}
			}
		}
	}
}
