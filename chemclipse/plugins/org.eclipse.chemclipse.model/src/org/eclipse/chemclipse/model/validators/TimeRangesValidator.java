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
import org.eclipse.chemclipse.model.ranges.TimeRanges;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;

public class TimeRangesValidator extends ValueParserSupport implements IValidator<Object> {

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = ModelMessages.timeRangesAreEmpty;
		} else {
			if(value instanceof TimeRanges timeRanges) {
				for(TimeRange timeRange : timeRanges.values()) {
					if(timeRange.getIdentifier().isEmpty()) {
						message = ModelMessages.identifierIsEmpty;
					} else {
						if(timeRange.getStart() > timeRange.getStop()) {
							message = ModelMessages.startMustNotBeGreaterThanStopValue;
						}
					}
				}
			} else {
				message = NLS.bind(ModelMessages.settingsClassNotOfType, TimeRanges.class.getName());
			}
		}

		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}
}