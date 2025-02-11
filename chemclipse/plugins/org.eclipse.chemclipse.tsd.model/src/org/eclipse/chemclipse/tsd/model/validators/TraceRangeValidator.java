/*******************************************************************************
 * Copyright (c) 2024, 2025 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.support.util.ValueParserSupport;
import org.eclipse.chemclipse.tsd.model.core.SecondDimensionHint;
import org.eclipse.chemclipse.tsd.model.core.TraceRange;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class TraceRangeValidator extends ValueParserSupport implements IValidator<Object> {

	private int retentionTimeColumn1Start = 0;
	private int retentionTimeColumn1Stop = 0;
	private int retentionTimeColumn2Start = 0;
	private int retentionTimeColumn2Stop = 0;
	private String scanIndicesColumn2 = "";
	private String name = "";
	private String traces = "";
	private SecondDimensionHint secondDimensionHint = SecondDimensionHint.NONE;

	@Override
	public IStatus validate(Object value) {

		String message = null;
		if(value == null) {
			message = "Value can't be null";
		} else {
			if(value instanceof String content) {
				String text = content.trim();
				if(text.isEmpty()) {
					message = "Entry can't be empty";
				} else {
					/*
					 * Extract the values
					 */
					retentionTimeColumn1Start = 0;
					retentionTimeColumn1Stop = 0;
					retentionTimeColumn2Start = 0;
					retentionTimeColumn2Stop = 0;
					scanIndicesColumn2 = "";
					name = "";
					traces = "";
					secondDimensionHint = SecondDimensionHint.NONE;
					//
					String[] values = text.trim().split("\\" + '|'); // The pipe needs to be escaped.
					if(values.length > 1) {
						/*
						 * Evaluation
						 */
						retentionTimeColumn1Start = (int)(parseDouble(values, 0) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
						if(retentionTimeColumn1Start < 0.0d) {
							message = "The retention time start (Column1) must not be < 0.";
						}
						//
						retentionTimeColumn1Stop = (int)(parseDouble(values, 1) * IChromatogramOverview.MINUTE_CORRELATION_FACTOR);
						if(retentionTimeColumn1Stop < 0.0d) {
							message = "The retention time stop (Column1) must not be < 0.";
						}
						//
						retentionTimeColumn2Start = (int)(parseDouble(values, 2) * IChromatogramOverview.SECOND_CORRELATION_FACTOR);
						retentionTimeColumn2Stop = (int)(parseDouble(values, 3) * IChromatogramOverview.SECOND_CORRELATION_FACTOR);
						scanIndicesColumn2 = parseString(values, 4);
						name = parseString(values, 5);
						traces = parseString(values, 6);
						secondDimensionHint = getSecondDimensionHint(parseString(values, 7));
						//
					} else {
						message = "Please enter a valid trace range.";
					}
				}
			} else {
				message = "Value has to be a string.";
			}
		}
		//
		if(message != null) {
			return ValidationStatus.error(message);
		} else {
			return ValidationStatus.ok();
		}
	}

	private SecondDimensionHint getSecondDimensionHint(String value) {

		try {
			return SecondDimensionHint.valueOf(value.trim());
		} catch(Exception e) {
			return SecondDimensionHint.NONE;
		}
	}

	public TraceRange getSetting() {

		TraceRange traceRange = new TraceRange();
		traceRange.setRetentionTimeColumn1Start(retentionTimeColumn1Start);
		traceRange.setRetentionTimeColumn1Stop(retentionTimeColumn1Stop);
		traceRange.setRetentionTimeColumn2Start(retentionTimeColumn2Start);
		traceRange.setRetentionTimeColumn2Stop(retentionTimeColumn2Stop);
		traceRange.setScanIndicesColumn2(scanIndicesColumn2);
		traceRange.setName(name);
		traceRange.setTraces(traces);
		traceRange.setSecondDimensionHint(secondDimensionHint);
		//
		return traceRange;
	}
}