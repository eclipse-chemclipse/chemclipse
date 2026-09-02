/*******************************************************************************
 * Copyright (c) 2022, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.model.core;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.support.traces.ITrace;

public class SignalSupport {

	public static String asText(double signal, DecimalFormat decimalFormat) {

		if(signal == ITrace.TOTAL_INTENSITY) {
			return ITrace.TOTAL_INTENSITY_DESCRIPTION;
		} else {
			return decimalFormat.format(signal);
		}
	}

	public static String asText(List<Double> signals, DecimalFormat decimalFormat) {

		if(signals.isEmpty()) {
			return ITrace.TOTAL_INTENSITY_DESCRIPTION;
		} else if(signals.size() == 1 && signals.get(0) == ITrace.TOTAL_INTENSITY) {
			return ITrace.TOTAL_INTENSITY_DESCRIPTION;
		} else {
			return signals.stream().sorted().map(value -> decimalFormat.format(value)).collect(Collectors.joining(", "));
		}
	}

	public static int compare(List<Double> signals1, List<Double> signals2) {

		int sortOrder = 0;
		int minSize = Math.min(signals1.size(), signals2.size());

		exitloop:
		for(int i = 0; i < minSize; i++) {
			if(sortOrder == 0) {
				sortOrder = Double.compare(signals1.get(i), signals2.get(i));
			} else {
				break exitloop;
			}
		}

		return sortOrder;
	}
}