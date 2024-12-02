/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.scf.model;

import org.eclipse.chemclipse.wsd.model.core.AbstractChromatogramWSD;

public class VendorChromatogram extends AbstractChromatogramWSD implements IVendorChromatogram {

	private static final long serialVersionUID = 2457872678347226000L;
	//
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
