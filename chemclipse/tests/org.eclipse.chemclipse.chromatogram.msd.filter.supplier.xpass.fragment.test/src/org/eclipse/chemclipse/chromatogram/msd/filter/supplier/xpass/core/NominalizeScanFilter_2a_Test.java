/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
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
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings.NominalizeFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonMSn;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.IonMSn;
import org.eclipse.chemclipse.msd.model.implementation.IonTransition;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NominalizeScanFilter_2a_Test {

	private NominalizeScanFilter filter;
	private NominalizeFilterSettings settings;
	private List<IScanMSD> massSpectra;

	@BeforeEach
	public void setUp() {

		filter = new NominalizeScanFilter();
		settings = new NominalizeFilterSettings();
		settings.setPreserveTandemMS(true);
		IonTransition ionTransition = new IonTransition(300.0, 150.3, 25.0, 0.7, 0.7, 1);
		ScanMSD massSpectrum = new ScanMSD();
		massSpectrum.addIon(new IonMSn(150.3, 5000f, ionTransition));
		massSpectra = new ArrayList<>();
		massSpectra.add(massSpectrum);
	}

	@Test
	public void test01() {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		assertFalse(processingInfo.hasErrorMessages());
	}

	@Test
	public void test02() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		assertEquals(1, massSpectra.get(0).getIons().size());
	}

	@Test
	public void test03() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		IIon ion = massSpectra.get(0).getIons().iterator().next();
		assertEquals(150.0, ion.getIon(), 0.001);
	}

	@Test
	public void test04() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		IIon ion = massSpectra.get(0).getIons().iterator().next();
		assertInstanceOf(IIonMSn.class, ion);
	}

	@Test
	public void test05() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		IIonMSn ionMSn = (IIonMSn)massSpectra.get(0).getIons().iterator().next();
		assertEquals(150.0, ionMSn.getIonTransition().getQ3Ion(), 0.001);
	}

	@Test
	public void test06() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		IIonMSn ionMSn = (IIonMSn)massSpectra.get(0).getIons().iterator().next();
		assertEquals(300, ionMSn.getIonTransition().getQ1Ion());
	}

	@Test
	public void test07() {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		assertEquals(ResultStatus.OK, processingInfo.getProcessingResult().getResultStatus());
	}
}