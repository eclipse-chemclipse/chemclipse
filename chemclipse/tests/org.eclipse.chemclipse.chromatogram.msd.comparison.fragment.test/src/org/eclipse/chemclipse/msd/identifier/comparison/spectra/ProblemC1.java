/*******************************************************************************
 * Copyright (c) 2014, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.identifier.comparison.spectra;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;

public class ProblemC1 implements ITestMassSpectrum {

	private IScanMSD massSpectrum;

	public ProblemC1() throws Exception {
		massSpectrum = new ScanMSD();
		massSpectrum.addIon(new Ion(183.0d, 32.375866f));
		massSpectrum.addIon(new Ion(181.0d, 35.19116f));
		massSpectrum.addIon(new Ion(124.0d, 156.95256f));
		massSpectrum.addIon(new Ion(167.0d, 600.36115f));
		massSpectrum.addIon(new Ion(182.0d, 703.1193f));
	}

	@Override
	public IScanMSD getMassSpectrum() {

		return massSpectrum;
	}
}
