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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.columns;

import org.eclipse.chemclipse.support.model.SeparationColumnType;

public class SeparationColumn extends AbstractSeparationColumn {

	private static final long serialVersionUID = -7621907201906074476L;

	public SeparationColumn(String name, SeparationColumnType separationColumnType) {

		this(name, separationColumnType, "", "", "");
	}

	public SeparationColumn(String name, SeparationColumnType separationColumnType, String length, String diameter, String phase) {

		setName(name);
		setSeparationColumnType(separationColumnType);
		setLength(length);
		setDiameter(diameter);
		setPhase(phase);
	}
}