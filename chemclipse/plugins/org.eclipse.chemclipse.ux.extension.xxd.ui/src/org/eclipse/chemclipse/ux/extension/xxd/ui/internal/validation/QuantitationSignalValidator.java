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

import org.eclipse.chemclipse.model.quantitation.IQuantitationSignal;
import org.eclipse.chemclipse.model.quantitation.QuantitationSignal;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class QuantitationSignalValidator extends ValueParserSupport implements IValidator<Object> {

	public static final String DEMO = "TIC | 100.0 | 0.0 | true";

	private static final String DELIMITER = "|";
	private static final String ERROR_TARGET = ExtensionMessages.enterSignalExample + ": " + DEMO;

	private double signal;
	private float relativeResponse;
	private double uncertainty;
	private boolean use;

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

					relativeResponse = parseFloat(values, 1);
					uncertainty = parseDouble(values, 2);
					use = parseBoolean(values, 3);
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

	public IQuantitationSignal getQuantitationSignal() {

		return new QuantitationSignal(signal, relativeResponse, uncertainty, use);
	}
}
