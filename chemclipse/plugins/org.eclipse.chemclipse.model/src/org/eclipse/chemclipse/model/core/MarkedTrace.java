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
package org.eclipse.chemclipse.model.core;

public class MarkedTrace extends AbstractMarkedTrace {

	public MarkedTrace(double trace) {

		super(trace, 1);
	}

	public MarkedTrace(double trace, int magnification) {

		super(trace, magnification);
	}

	@Override
	public int castTrace() {

		return (int)Math.round(getTrace());
	}
}