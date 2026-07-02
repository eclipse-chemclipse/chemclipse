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
package org.eclipse.chemclipse.msd.converter.supplier.ocx.model.chromatogram;

import org.eclipse.chemclipse.msd.model.core.AbstractIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;

public class VendorIonMSn extends AbstractIonMSn implements IVendorIonMSn {

	private static final long serialVersionUID = 1363756417066258878L;

	public VendorIonMSn(double ion, float abundance, IIonTransition ionTransition) {

		super(ion, abundance, ionTransition);
	}
}