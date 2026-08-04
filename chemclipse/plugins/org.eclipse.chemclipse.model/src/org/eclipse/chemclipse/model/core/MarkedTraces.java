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

import java.util.ArrayList;

import org.eclipse.chemclipse.support.traces.ITrace;

public class MarkedTraces extends ArrayList<ITrace> implements IMarkedTraces<ITrace> {

	private static final long serialVersionUID = -3071089214262731218L;
	private MarkedTraceModus markedTraceModus;

	public MarkedTraces() {

		this(MarkedTraceModus.INCLUDE);
	}

	public MarkedTraces(MarkedTraceModus markedTraceModus) {

		this.markedTraceModus = markedTraceModus;
	}

	@Override
	public MarkedTraceModus getMarkedTraceModus() {

		return markedTraceModus;
	}
}