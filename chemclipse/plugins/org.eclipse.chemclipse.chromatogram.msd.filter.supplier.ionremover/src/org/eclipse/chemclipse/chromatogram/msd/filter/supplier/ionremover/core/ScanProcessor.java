/*******************************************************************************
 * Copyright (c) 2014, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.ionremover.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.ionremover.settings.ITraceRemoverFilterSettings;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceFactory;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;

public class ScanProcessor {

	public void apply(List<IScanMSD> massSpectra, ITraceRemoverFilterSettings traceRemoverFilterSettings) {

		/*
		 * Prepare
		 */
		MarkedTraceModus markedTraceModus = traceRemoverFilterSettings.getMarkedTraceModus();
		IMarkedTraces<ITrace> markedIons = new MarkedTraces(markedTraceModus);
		for(int ion : getIonsList(traceRemoverFilterSettings.getIonsToRemove())) {
			markedIons.add(new TraceNominalMSD(ion));
		}
		/*
		 * Apply
		 */
		for(IScanMSD massSpectrum : massSpectra) {
			massSpectrum.removeIons(markedIons);
		}
	}

	private int[] getIonsList(String ions) {

		List<TraceNominalMSD> traces = TraceFactory.parseTraces(ions, TraceNominalMSD.class);
		int size = traces.size();
		int[] ionsList = new int[size];
		for(int i = 0; i < size; i++) {
			ionsList[i] = traces.get(i).getMZ();
		}

		return ionsList;
	}
}