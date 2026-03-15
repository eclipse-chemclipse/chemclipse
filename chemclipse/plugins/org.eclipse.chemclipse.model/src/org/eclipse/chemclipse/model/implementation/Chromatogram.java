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
 *******************************************************************************/
package org.eclipse.chemclipse.model.implementation;

import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.core.INoiseCalculator;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;

/**
 * Chromatogram shall be used only for testing purposes.
 */
public class Chromatogram extends AbstractChromatogram {

	private static final long serialVersionUID = -8477205385713705933L;

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

	}

	@Override
	protected String getNoiseCalculatorId() {

		return "";
	}

	@Override
	protected INoiseCalculator createNoiseCalculator(String id) {

		return null;
	}
}