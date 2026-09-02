/*******************************************************************************
 * Copyright (c) 2019, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.validation;

import org.eclipse.chemclipse.model.quantitation.IResponseSignal;
import org.eclipse.chemclipse.model.quantitation.ResponseSignal;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class ResponseSignalValidator extends ValueParserSupport implements IValidator<Object> {

	public static final String DEMO = "TIC | 1.5 | 289893.38";

	private static final String DELIMITER = "|";
	private static final String ERROR_TARGET = ExtensionMessages.enterResponseSignalExample + DEMO;

	private double signal;
	private double concentration;
	private double response;

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = ERROR_TARGET;
		} else {
			if(value instanceof String text) {
				text = text.trim();
				if("".equals(text.trim())) {
					message = ERROR_TARGET;
				} else {
					String[] values = text.trim().split("\\" + DELIMITER);

					String signalValue = parseString(values, 0);
					if("TIC".equals(signalValue)) {
						signal = ITrace.TOTAL_INTENSITY;
					} else {
						signal = parseDouble(values, 0);
					}

					concentration = parseDouble(values, 1);
					response = parseDouble(values, 2);
				}
			} else {
				message = ERROR_TARGET;
			}
		}

		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	public IResponseSignal getResponseSignal() {

		return new ResponseSignal(signal, concentration, response);
	}
}
