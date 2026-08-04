/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.model.xwc;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.signals.ITotalScanSignalExtractor;
import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;

public interface ITotalWavelengthSignalExtractor extends ITotalScanSignalExtractor {

	ITotalScanSignals getTotalWavelengthSignals(IChromatogramSelectionWSD chromatogramSelection);

	ITotalScanSignals getTotalWavelengthSignals(IChromatogramSelectionWSD chromatogramSelection, IMarkedTraces<ITrace> excludedWavelengths);

	ITotalScanSignals getTotalScanSignals(int startScan, int stopScan, IMarkedTraces<ITrace> excludedWavelengths);
}