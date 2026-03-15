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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.msd.model.core.AbstractPeakIon;
import org.eclipse.chemclipse.msd.model.core.IIon;

public class PeakIon extends AbstractPeakIon {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -4094514166910811630L;

	public PeakIon(double ion) {

		super(ion);
	}

	public PeakIon(double ion, float abundance) {

		super(ion, abundance);
	}

	public PeakIon(IIon ion) {

		super(ion.getIon(), ion.getAbundance());
	}
}
