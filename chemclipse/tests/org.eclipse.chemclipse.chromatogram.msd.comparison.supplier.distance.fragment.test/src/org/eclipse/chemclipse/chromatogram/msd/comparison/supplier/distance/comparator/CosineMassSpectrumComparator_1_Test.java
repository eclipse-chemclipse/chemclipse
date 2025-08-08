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
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.MatchConstraints;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.junit.Before;
import org.junit.Test;

public class CosineMassSpectrumComparator_1_Test extends MassSpectrumSetTestCase {

	private CosineComparator comparator;
	private IProcessingInfo<IComparisonResult> processingInfo;
	private IComparisonResult result;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();

		IScanMSD unknown = sinapylAclohol.getMassSpectrum();
		IScanMSD reference = sinapylAcloholCis.getMassSpectrum();

		comparator = new CosineComparator();
		processingInfo = comparator.compare(unknown, reference, new MatchConstraints());
		result = processingInfo.getProcessingResult();
	}

	@Test
	public void test1() {

		assertFalse(processingInfo.hasErrorMessages());
	}

	@Test
	public void test2() {

		assertEquals(94.90913f, result.getMatchFactor(), 0);
	}

	@Test
	public void test3() {

		assertEquals(94.9127f, result.getReverseMatchFactor(), 0);
	}
}
