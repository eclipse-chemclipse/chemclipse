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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.chemclipse.tsd.model.core.TraceRanges;
import org.eclipse.chemclipse.tsd.model.validators.TraceRangeValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;

public class TraceRangeInputValidator implements IInputValidator {

	private TraceRangeValidator validator = new TraceRangeValidator();
	private TraceRanges traceRanges = new TraceRanges();

	public TraceRangeInputValidator(TraceRanges traceRanges) {

		if(traceRanges != null) {
			this.traceRanges = traceRanges;
		}
	}

	@Override
	public String isValid(String target) {

		IStatus status = validator.validate(target);
		if(status.isOK()) {
			TraceRange stackRange = validator.getSetting();
			if(traceRanges.contains(stackRange)) {
				return "The stack range exists already.";
			}
		} else {
			return status.getMessage();
		}
		return null;
	}
}