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

import org.eclipse.chemclipse.msd.model.core.AbstractIonMSn;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonTransition;

/**
 * If a new ion type should be implemented, extend the abstract class {@link AbstractIonMSn} and not this class.
 */
public class IonMSn extends AbstractIonMSn {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -2534953352415243410L;

	public IonMSn(double ion, float abundance, IIonTransition ionTransition) {

		super(ion, abundance, ionTransition);
	}

	public IonMSn(IIon ion, IIonTransition ionTransition) {

		super(ion, ionTransition);
	}
}
