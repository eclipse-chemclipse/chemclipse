/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core;

import static org.junit.Assert.assertEquals;

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
import org.junit.Before;
import org.junit.Test;

public class Calculator_3_ITest extends ChromatogramImporterTestCase {

	private Calculator calculator = new Calculator();;
	private IExtractedIonSignals extractedIonSignals;
	private IMarkedIons ionsToPreserve;
	private List<INoiseSegmentMSD> noiseSegments;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		ionsToPreserve = new MarkedIons(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new MarkedIon(103));
		ionsToPreserve.add(new MarkedIon(104));
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		noiseSegments = calculator.getNoiseSegments(extractedIonSignals, ionsToPreserve, 13, new NullProgressMonitor());
	}

	@Test
	public void testGetSize_1() {

		assertEquals("Size", 9, noiseSegments.size());
	}
}
