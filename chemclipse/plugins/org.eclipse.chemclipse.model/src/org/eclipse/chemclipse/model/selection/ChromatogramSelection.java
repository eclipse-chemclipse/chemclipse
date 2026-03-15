/*******************************************************************************
 * Copyright (c) 2013, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.model.selection;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;

/**
 * ONLY USE THIS CLASS WHEN NO UPDATES SHALL BE FIRED.
 *
 * This implementation shall be used as a proxy only.
 * It will not fire update changes, hence it is not aware of the
 * contained chromatogram type. It could be a chromatogram detected
 * by a mass selective, a flame ionization or another detector.
 *
 */
public class ChromatogramSelection extends AbstractChromatogramSelection {

	public ChromatogramSelection(IChromatogram chromatogram, boolean fireUpdate) throws ChromatogramIsNullException {

		super(chromatogram);
		/*
		 * Set all members to default values.<br/> This includes also to set a
		 * valid scan and if exists a valid peak.
		 */
		reset(fireUpdate);
	}

	public ChromatogramSelection(IChromatogram chromatogram) throws ChromatogramIsNullException {

		this(chromatogram, true);
	}

	@Override
	public void reset() {

		super.reset(false);
	}
}
