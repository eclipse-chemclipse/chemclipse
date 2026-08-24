/*******************************************************************************
 * Copyright (c) 2024, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.wsd.converter.supplier.scf.model;

import org.eclipse.chemclipse.dsd.model.core.AbstractChromatogramDSD;

public class VendorChromatogram extends AbstractChromatogramDSD implements IVendorChromatogram {

	private static final long serialVersionUID = 2457872678347226000L;

	private Version version;

	@Override
	public Version getVersion() {

		return version;
	}

	@Override
	public void setVersion(Version version) {

		this.version = version;
	}
}
