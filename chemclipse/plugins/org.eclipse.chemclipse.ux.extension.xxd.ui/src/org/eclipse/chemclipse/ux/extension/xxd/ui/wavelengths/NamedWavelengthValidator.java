/*******************************************************************************
 * Copyright (c) 2020, 2022 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - adapted for DAD
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.wavelengths;

import org.eclipse.chemclipse.support.util.NamedWavelengthListUtil;
import org.eclipse.chemclipse.support.validators.WavelengthValidator;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class NamedWavelengthValidator implements IValidator {

	private static final String ERROR = "Please enter a correct identifier.";
	//
	private WavelengthValidator wavelengthValidator = new WavelengthValidator();
	//
	private String identifier = "";
	private String wavelengths = "";

	@Override
	public IStatus validate(Object value) {

		String message = null;
		//
		if(value == null) {
			message = ERROR;
		} else {
			if(value instanceof String) {
				String[] values = value.toString().trim().split("\\" + NamedWavelengthListUtil.SEPARATOR_ENTRY);
				String identifier = values.length > 0 ? values[0].trim() : "";
				String wavelengths = values.length > 1 ? values[1].trim() : "";
				//
				if("".equals(identifier)) {
					message = ERROR;
				} else {
					this.identifier = identifier;
					IStatus status = wavelengthValidator.validate(wavelengths);
					if(status.isOK()) {
						this.wavelengths = wavelengths;
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

	public String getIdentifier() {

		return identifier;
	}

	public String getWavelengths() {

		return wavelengths;
	}
}
