/*******************************************************************************
 * Copyright (c) 2010, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support.ChromatogramImporterTestCase;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIon;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.msd.model.noise.Calculator;
import org.eclipse.chemclipse.msd.model.noise.INoiseSegmentMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.core.runtime.NullProgressMonitor;

public class Calculator_3_ITest extends ChromatogramImporterTestCase {

	private Calculator calculator;
	private IExtractedIonSignals extractedIonSignals;
	private IMarkedIons ionsToPreserve;
	private List<INoiseSegmentMSD> noiseSegments;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		calculator = new Calculator();
		ionsToPreserve = new MarkedIons(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new MarkedIon(103));
		ionsToPreserve.add(new MarkedIon(104));
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		noiseSegments = calculator.getNoiseSegments(extractedIonSignals, ionsToPreserve, 13, new NullProgressMonitor());
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testGetSize_1() {

		assertEquals("Size", 9, noiseSegments.size());
	}
}
