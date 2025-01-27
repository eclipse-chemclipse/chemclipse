/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.validators;

import java.io.File;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class PathValidator implements IValidator<Object> {

	private static final String ERROR = "Please enter valid path.";
	private static final String ERROR_VALUE = "The path is not available.";
	private static final String ERROR_DIRECTORY = "The path is not a directory.";
	//
	private boolean enforceDirectory = false;
	private String path = "";

	public PathValidator(boolean enforceDirectory) {

		this.enforceDirectory = enforceDirectory;
	}

	@Override
	public IStatus validate(Object value) {

		path = "";
		String message = null;
		if(value == null) {
			message = ERROR;
		} else {
			if(value instanceof String text) {
				path = text;
				File file = new File(text);
				if(!file.exists()) {
					message = ERROR_VALUE;
				} else {
					if(enforceDirectory) {
						if(!file.isDirectory()) {
							message = ERROR_DIRECTORY;
						}
					}
				}
			} else {
				message = ERROR;
			}
		}
		//
		if(message != null) {
			path = "";
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public String getPath() {

		return path;
	}
}