/*******************************************************************************
 * Copyright (c) 2008, 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram;

import org.eclipse.chemclipse.msd.model.core.AbstractIon;

public class VendorIon extends AbstractIon implements IVendorIon {

	private static final long serialVersionUID = -6328005534960551276L;
	/*
	 * The max value for m/z
	 */
	public static final double MIN_ION = 1.0d;
	public static final double MAX_ION = 65535.0d; // TODO: why is this limited to SHORT.MAXVALUE?

	public VendorIon(double ion, float abundance) {

		super(ion, abundance);
	}
}