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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.wizards;

public class DatabaseEntryClean {

	private final String name;
	private final String casNumber;

	public DatabaseEntryClean(String name, String casNumber) {

		this.name = name;
		this.casNumber = casNumber;
	}

	public String getName() {

		return name;
	}

	public String getCasNumber() {

		return casNumber;
	}
}