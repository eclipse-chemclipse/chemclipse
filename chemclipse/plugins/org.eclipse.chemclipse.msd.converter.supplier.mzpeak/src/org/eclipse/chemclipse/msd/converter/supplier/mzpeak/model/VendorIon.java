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
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.model;

import org.eclipse.chemclipse.msd.model.core.AbstractIon;

public class VendorIon extends AbstractIon implements IVendorIon {

	private static final long serialVersionUID = -5451507412681938943L;

	public VendorIon(double ion, float abundance) {

		super(ion, abundance);
	}
}