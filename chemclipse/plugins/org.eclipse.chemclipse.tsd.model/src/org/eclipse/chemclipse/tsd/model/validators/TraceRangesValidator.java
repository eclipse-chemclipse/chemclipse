/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.model.validators;

import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.tsd.model.core.TraceRanges;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class TraceRangesValidator extends ValueParserSupport implements IValidator<Object> {

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = "The trace ranges are empty.";
		} else {
			if(value instanceof TraceRanges traceRanges) {
				for(TraceRange traceRange : traceRanges) {
					if(traceRange.getRetentionTimeColumn1Start() < 0) {
						message = "The retention time start is < 0.";
					} else if(traceRange.getRetentionTimeColumn1Stop() < 0) {
						message = "The retention time start is < 0.";
					}
				}
			} else {
				message = "The settings class is not of type: " + TraceRanges.class.getName();
			}
		}
		//
		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}
}