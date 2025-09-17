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
package org.eclipse.chemclipse.msd.identifier.comparison.math;

import org.eclipse.chemclipse.msd.identifier.comparison.spectra.BenzenepropanoicAcid;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ITestMassSpectrum;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.NoMatchA1;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.NoMatchA2;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.PhenolBenzimidazolyl;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemA1;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemA2;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemB1;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemB2;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemC1;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.ProblemC2;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.SinapylAlcohol;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.SinapylAlcoholCis;
import org.eclipse.chemclipse.msd.identifier.comparison.spectra.Syringylacetone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Comparison NIST-DB 12 (MF, RMF):
 * sinapylAclohol vs sinapylAcloholCis: 79.4, 92.6
 * sinapylAclohol vs benzenepropanoicAcid: 61.9, 68.0
 * sinapylAclohol vs syringylAcetone: 59.5, 76.3
 * sinapylAclohol vs phenolBenzimidazolyl: 51.5, 57.6
 * 
 */
@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class MassSpectrumSetTestCase {

	protected ITestMassSpectrum sinapylAclohol;
	protected ITestMassSpectrum sinapylAcloholCis;
	protected ITestMassSpectrum benzenepropanoicAcid;
	protected ITestMassSpectrum syringylAcetone;
	protected ITestMassSpectrum phenolBenzimidazolyl;

	protected ITestMassSpectrum noMatchA1;
	protected ITestMassSpectrum noMatchA2;
	protected ITestMassSpectrum problemA1;
	protected ITestMassSpectrum problemA2;
	protected ITestMassSpectrum problemB1;
	protected ITestMassSpectrum problemB2;
	protected ITestMassSpectrum problemC1;
	protected ITestMassSpectrum problemC2;

	@BeforeAll
	public void setUp() throws Exception {

		sinapylAclohol = new SinapylAlcohol();
		sinapylAcloholCis = new SinapylAlcoholCis();
		benzenepropanoicAcid = new BenzenepropanoicAcid();
		syringylAcetone = new Syringylacetone();
		phenolBenzimidazolyl = new PhenolBenzimidazolyl();

		noMatchA1 = new NoMatchA1();
		noMatchA2 = new NoMatchA2();
		problemA1 = new ProblemA1();
		problemA2 = new ProblemA2();
		problemB1 = new ProblemB1();
		problemB2 = new ProblemB2();
		problemC1 = new ProblemC1();
		problemC2 = new ProblemC2();
	}
}
