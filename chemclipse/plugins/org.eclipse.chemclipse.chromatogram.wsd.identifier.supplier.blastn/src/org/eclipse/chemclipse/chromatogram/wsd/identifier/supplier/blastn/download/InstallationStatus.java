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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.supplier.blastn.download;

import org.eclipse.chemclipse.support.text.ILabel;

public enum InstallationStatus implements ILabel {

	NOT_INSTALLED("Not installed"), //
	UPDATE_AVAILABLE("Update available"), //
	UP_TO_DATE("Up to date");

	private String label = "";

	private InstallationStatus(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}
}
