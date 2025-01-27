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
package org.eclipse.chemclipse.ux.extension.ui.locations;

import org.eclipse.chemclipse.support.util.UserLocationListUtil;
import org.eclipse.chemclipse.support.validators.PathValidator;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class UserLocationValidator implements IValidator<Object> {

	private static final String ERROR = "Please enter a correct name.";
	//
	private PathValidator pathValidator = new PathValidator(true);
	//
	private String name = "";
	private String path = "";

	@Override
	public IStatus validate(Object value) {

		String message = null;
		this.name = "";
		this.path = "";
		//
		if(value == null) {
			message = ERROR;
		} else {
			if(value instanceof String) {
				String[] values = value.toString().trim().split("\\" + UserLocationListUtil.SEPARATOR_ENTRY);
				String name = values.length > 0 ? values[0].trim() : "";
				String path = values.length > 1 ? values[1].trim() : "";
				//
				if(name.isBlank()) {
					message = ERROR;
				} else {
					this.name = name;
					IStatus status = pathValidator.validate(path);
					if(status.isOK()) {
						this.path = path;
					} else {
						message = status.getMessage();
					}
				}
			} else {
				message = ERROR;
			}
		}
		//
		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public String getName() {

		return name;
	}

	public String getPath() {

		return path;
	}
}