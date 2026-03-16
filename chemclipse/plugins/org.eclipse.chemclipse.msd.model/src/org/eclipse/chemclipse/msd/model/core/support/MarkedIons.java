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
 * Christoph Läubrich - add support for IonMarkMode
 * Alexander Kerner - add constructor
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core.support;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;

public class MarkedIons extends AbstractMarkedIons {

	private MarkedTraceModus markedTraceModus = MarkedTraceModus.INCLUDE;

	public MarkedIons(int[] ionsList, MarkedTraceModus markedTraceModus) {

		super(ionsList);
		this.markedTraceModus = markedTraceModus;
	}

	public MarkedIons(Collection<? extends Number> ionsList, MarkedTraceModus markedTraceModus) {

		super(ionsList);
		this.markedTraceModus = markedTraceModus;
	}

	public MarkedIons(MarkedTraceModus markedTraceModus) {

		this(new ArrayList<>(), markedTraceModus);
	}

	@Override
	public MarkedTraceModus getMarkedTraceModus() {

		return markedTraceModus;
	}
}