/*******************************************************************************
 * Copyright (c) 2018, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - formatting
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.util.Set;

import org.eclipse.chemclipse.support.traces.ITrace;

public interface IMarkedTraces<S extends ITrace> extends Set<S> {

	double TOTAL_SIGNAL = ISignal.TOTAL_INTENSITY;
	int TOTAL_SIGNAL_AS_INT = (int)Math.round(TOTAL_SIGNAL);

	MarkedTraceModus getMarkedTraceModus();
}