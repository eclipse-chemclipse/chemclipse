/*******************************************************************************
 * Copyright (c) 2010, 2026 Lablicate GmbH.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support.ChromatogramImporterTestCase;
import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.eclipse.chemclipse.msd.model.noise.Calculator;
import org.eclipse.chemclipse.msd.model.noise.INoiseSegmentMSD;
import org.eclipse.chemclipse.msd.model.xic.ExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignalExtractor;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignals;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class Calculator_3_ITest extends ChromatogramImporterTestCase {

	private Calculator calculator = new Calculator();
	private List<INoiseSegmentMSD> noiseSegments;

	@Override
	@BeforeAll
	public void setUp() throws FilterException {

		super.setUp();
		IMarkedTraces<ITrace> ionsToPreserve = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new TraceNominalMSD(103));
		ionsToPreserve.add(new TraceNominalMSD(104));
		IExtractedIonSignalExtractor extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		IExtractedIonSignals extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals(chromatogramSelection);
		noiseSegments = calculator.getNoiseSegments(extractedIonSignals, ionsToPreserve, 13);
	}

	@Test
	public void testGetSize_1() {

		assertEquals(9, noiseSegments.size(), "Size");
	}
}
