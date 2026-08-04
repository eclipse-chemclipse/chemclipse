/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - identification target comparator
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.util;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;

public class MarkedTracesSupportMSD {

	public static Set<Integer> getTracesAsInteger(Collection<ITrace> traces) {

		return traces.stream().map(t -> AbstractIon.getIon(t.getValue())).collect(Collectors.toSet());
	}

	public static void add(Collection<ITrace> traces, int ionStart, int ionStop) {

		if(ionStart > ionStop) {
			int tmp = ionStart;
			ionStart = ionStop;
			ionStop = tmp;
		}

		for(int i = ionStart; i <= ionStop; i++) {
			traces.add(new TraceNominalMSD(i));
		}
	}

	public static void add(Collection<ITrace> traces, int... ions) {

		for(int ion : ions) {
			traces.add(new TraceNominalMSD(ion));
		}
	}

	public static void add(Collection<ITrace> traces, Collection<Integer> ions) {

		for(int ion : ions) {
			traces.add(new TraceNominalMSD(ion));
		}
	}
}