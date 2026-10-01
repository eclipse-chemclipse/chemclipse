/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - support File properties
 * Matthias Mailänder - support required combo properties
 *******************************************************************************/
package org.eclipse.chemclipse.support.settings.validation;

import java.io.File;

import org.eclipse.chemclipse.support.settings.FileSettingProperty;
import org.eclipse.chemclipse.support.settings.FileSettingProperty.DialogType;
import org.eclipse.chemclipse.support.settings.parser.InputValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class InputValidator implements IValidator<Object> {

	private static final String ERROR = "Please enter a value.";
	private static final String ERROR_COMBO = "Please select an option from the combo box.";
	private final InputValue inputValue;

	public InputValidator(InputValue inputValue) {

		this.inputValue = inputValue;
	}

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(isRequiredComboEmpty(value)) {
			message = ERROR_COMBO;
		} else if(value == null) {
			message = ERROR;
		} else {
			Class<?> rawType = inputValue.getRawType();
			if(rawType != null) {
				message = parse(rawType, value.toString());
			}
		}

		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	private boolean isRequiredComboEmpty(Object value) {

		if(inputValue.getComboSupplier() == null || !inputValue.isComboRequired()) {
			return false;
		}
		return value == null || value.toString().isBlank();
	}

	private String parse(Class<?> rawType, String value) {

		String message = null;
		try {
			if(rawType == boolean.class || rawType == Boolean.class) {
				Boolean.parseBoolean(value);
			} else if(rawType.isEnum()) {
				if("".equals(value)) {
					message = ERROR_COMBO;
				}
			} else if(rawType == File.class) {
				FileSettingProperty property = inputValue.getFileSettingProperty();
				if(property != null) {
					if(value == null || value.isEmpty() || value.isBlank()) {
						if(!property.allowEmpty()) {
							return "Please choose a location.";
						}
					} else if(property.dialogType() == DialogType.OPEN_DIALOG) {
						if(!new File(value).exists()) {
							return "Location does not exist.";
						}
					}
				}
			}
		} catch(NumberFormatException e) {
			message = ERROR;
		}
		return message;
	}
}
