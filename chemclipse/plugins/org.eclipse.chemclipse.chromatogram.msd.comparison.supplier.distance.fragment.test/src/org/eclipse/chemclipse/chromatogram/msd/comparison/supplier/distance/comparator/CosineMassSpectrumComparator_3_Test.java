/*******************************************************************************
 * Copyright (c) 2014, 2015 Dr. Philip Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.eclipse.chemclipse.chromatogram.msd.comparison.processing.IMassSpectrumComparatorProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.identifier.massspectrum.IMassSpectrumComparisonResult;

public class CosineMassSpectrumComparator_3_Test extends MassSpectrumSetTestCase {

	private CosineMassSpectrumComparator comparator;
	private IMassSpectrumComparatorProcessingInfo processingInfo;
	private IMassSpectrumComparisonResult result;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		//
		IScanMSD unknown = sinapylAclohol.getMassSpectrum();
		IScanMSD reference = syringylAcetone.getMassSpectrum();
		//
		comparator = new CosineMassSpectrumComparator();
		processingInfo = comparator.compare(unknown, reference);
		result = processingInfo.getMassSpectrumComparisonResult();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertFalse(processingInfo.hasErrorMessages());
	}

	public void test2() {

		assertEquals(69.117676f, result.getMatchFactor());
	}

	public void test3() {

		assertEquals(69.120285f, result.getReverseMatchFactor());
	}
}
