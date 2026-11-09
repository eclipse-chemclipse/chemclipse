/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.validators;

import org.eclipse.chemclipse.model.l10n.ModelMessages;
import org.eclipse.chemclipse.model.ranges.TimeRange;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class TimeRangeValidator extends ValueParserSupport implements IValidator<Object> {

	private String identifier = ""; //$NON-NLS-1$
	private int start = 0;
	private int center = 0;
	private int stop = 0;

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = ModelMessages.valueMustNotBeNull;
		} else {
			if(value instanceof String) {
				String text = ((String)value).trim();
				if(text.isEmpty()) {
					message = ModelMessages.entryMustNotBeEmpty;
				} else {
					/*
					 * Extract the name
					 */
					identifier = ""; //$NON-NLS-1$
					start = 0;
					center = 0;
					stop = 0;

					String[] values = text.trim().split("\\" + '|'); // The pipe needs to be escaped. //$NON-NLS-1$
					if(values.length > 1) {
						/*
						 * Evaluation
						 */
						identifier = parseString(values, 0);
						if(identifier.isBlank()) {
							message = ModelMessages.identifierMustNotBeBlank;
						}

						start = parseInteger(values, 1);
						if(start < 0) {
							message = ModelMessages.startMustNotBeNegative;
						}

						center = parseInteger(values, 2);
						if(start < 0) {
							message = ModelMessages.centerMustNotBeNegative;
						}

						stop = parseInteger(values, 1);
						if(stop < 0) {
							message = ModelMessages.stopMustNotBeNegative;
						}

						if(start > stop) {
							message = ModelMessages.startMustNotBeGreaterThanStop;
						}

					} else {
						message = ModelMessages.enterValidIdentifier;
					}
				}
			} else {
				message = ModelMessages.valueHasToBeString;
			}
		}

		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public TimeRange getSetting() {

		return new TimeRange(identifier, start, center, stop);
	}
}