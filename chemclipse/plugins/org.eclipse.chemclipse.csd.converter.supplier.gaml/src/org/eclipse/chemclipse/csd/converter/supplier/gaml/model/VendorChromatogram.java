/*******************************************************************************
 * Copyright (c) 2008, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.gaml.model;

import org.eclipse.chemclipse.csd.model.core.AbstractChromatogramCSD;

public class VendorChromatogram extends AbstractChromatogramCSD implements IVendorChromatogram {

	private static final long serialVersionUID = 6325717916001851511L;

	@Override
	public String getName() {

		return extractNameFromFile("GAML chromatogram");
	}
}
