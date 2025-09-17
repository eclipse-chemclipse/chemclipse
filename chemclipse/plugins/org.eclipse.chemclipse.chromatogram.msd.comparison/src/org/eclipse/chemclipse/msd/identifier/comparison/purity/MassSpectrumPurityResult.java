/*******************************************************************************
 * Copyright (c) 2012, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.comparison.purity;

import org.eclipse.chemclipse.msd.identifier.comparison.exceptions.ComparisonException;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;

public class MassSpectrumPurityResult extends AbstractMassSpectrumPurityResult {

	/**
	 * The fit value returns how good the extracted mass spectrum matches the
	 * genuine mass spectrum.<br/>
	 * The reverse fit value returns the percentage match of the
	 * genuineMassSpectrum to the extractedMassSpectrum.
	 * 
	 * @param extractedMassSpectrum
	 * @param genuineMassSpectrum
	 * @throws ComparisonException
	 */
	public MassSpectrumPurityResult(IScanMSD extractedMassSpectrum, IScanMSD genuineMassSpectrum) throws ComparisonException {
		super(extractedMassSpectrum, genuineMassSpectrum);
	}
}
