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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.chemclipse.model.core.IMarkedTraces;
import org.eclipse.chemclipse.model.core.MarkedTraceModus;
import org.eclipse.chemclipse.model.core.MarkedTraces;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.exceptions.FilterException;
import org.eclipse.chemclipse.support.traces.ITrace;
import org.eclipse.chemclipse.support.traces.TraceNominalMSD;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class Denoising_1_ITest extends ChromatogramImporterTestCase {

	private List<ICombinedMassSpectrum> noiseMassSpectra;

	@Override
	@BeforeAll
	public void setUp() throws FilterException {

		super.setUp();
		IMarkedTraces<ITrace> ionsToRemove = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ionsToRemove.add(new TraceNominalMSD(18));
		ionsToRemove.add(new TraceNominalMSD(28));
		ionsToRemove.add(new TraceNominalMSD(32));
		ionsToRemove.add(new TraceNominalMSD(84));
		ionsToRemove.add(new TraceNominalMSD(207));
		IMarkedTraces<ITrace> ionsToPreserve = new MarkedTraces(MarkedTraceModus.INCLUDE);
		ionsToPreserve.add(new TraceNominalMSD(103));
		ionsToPreserve.add(new TraceNominalMSD(103));
		noiseMassSpectra = Denoising.applyDenoisingFilter(chromatogramSelection, ionsToRemove, ionsToPreserve, true, 1, 13, new NullProgressMonitor());
	}

	@Test
	public void testGetSize_1() {

		assertEquals(11, noiseMassSpectra.size(), "Size");
	}
}
