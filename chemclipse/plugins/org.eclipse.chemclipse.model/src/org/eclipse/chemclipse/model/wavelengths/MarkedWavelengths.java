/*******************************************************************************
 * Copyright (c) 2016, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Matthias Mailänder - add inclusive/exclusive mode
 * Lorenz Gerber - MarkedWavelenghts based on int array
 *******************************************************************************/
package org.eclipse.chemclipse.model.wavelengths;

import java.util.Collection;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;

public class MarkedWavelengths extends AbstractMarkedWavelengths {

	private MarkedTraceModus markedTraceModus = MarkedTraceModus.INCLUDE;

	public MarkedWavelengths() {

	}

	public MarkedWavelengths(int[] wavelengthList, MarkedTraceModus markedTraceModus) {

		super(wavelengthList);
		this.markedTraceModus = markedTraceModus;
	}

	public MarkedWavelengths(MarkedTraceModus markedTraceModus) {

		this.markedTraceModus = markedTraceModus;
	}

	public MarkedWavelengths(Collection<? extends Number> wavelengths, MarkedTraceModus markedTraceModus) {

		super(wavelengths);
		this.markedTraceModus = markedTraceModus;
	}

	@Override
	public MarkedTraceModus getMarkedTraceModus() {

		return markedTraceModus;
	}
}
