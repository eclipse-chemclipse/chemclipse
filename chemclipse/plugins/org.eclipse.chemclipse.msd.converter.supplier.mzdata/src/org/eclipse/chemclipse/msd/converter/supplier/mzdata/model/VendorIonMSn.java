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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzdata.model;

import org.eclipse.chemclipse.msd.model.core.AbstractIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;

public class VendorIonMSn extends AbstractIonMSn implements IVendorIon {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -1963037036476879340L;

	public VendorIonMSn(double ion, float abundance, IIonTransition ionTransition) {

		super(ion, abundance, ionTransition);
	}
}
