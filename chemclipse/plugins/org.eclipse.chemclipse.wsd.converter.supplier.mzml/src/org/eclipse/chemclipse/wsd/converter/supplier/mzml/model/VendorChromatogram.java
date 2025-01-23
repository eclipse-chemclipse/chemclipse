/*******************************************************************************
 * Copyright (c) 2021, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.mzml.model;

import org.eclipse.chemclipse.wsd.model.core.AbstractChromatogramWSD;

public class VendorChromatogram extends AbstractChromatogramWSD implements IVendorChromatogram {

	private static final long serialVersionUID = 2874207336794926052L;

	@Override
	public String getName() {

		return extractNameFromFile("mzML HPLC-DAD");
	}
}
