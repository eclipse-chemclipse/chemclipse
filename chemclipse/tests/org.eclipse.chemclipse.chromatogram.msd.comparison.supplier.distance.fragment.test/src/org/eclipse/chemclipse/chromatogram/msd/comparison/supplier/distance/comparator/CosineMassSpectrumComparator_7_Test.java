/*******************************************************************************
 * Copyright (c) 2014, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.MatchConstraints;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

public class CosineMassSpectrumComparator_7_Test extends MassSpectrumSetTestCase {

	private CosineComparator comparator;
	private IProcessingInfo<IComparisonResult> processingInfo;
	private IComparisonResult result;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		//
		IScanMSD unknown = problemB1.getMassSpectrum();
		IScanMSD reference = problemB2.getMassSpectrum();
		//
		comparator = new CosineComparator();
		processingInfo = comparator.compare(unknown, reference, new MatchConstraints());
		result = processingInfo.getProcessingResult();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertFalse(processingInfo.hasErrorMessages());
	}

	public void test2() {

		assertEquals(97.44086f, result.getMatchFactor());
	}

	public void test3() {

		assertEquals(97.23046f, result.getReverseMatchFactor());
	}
}
