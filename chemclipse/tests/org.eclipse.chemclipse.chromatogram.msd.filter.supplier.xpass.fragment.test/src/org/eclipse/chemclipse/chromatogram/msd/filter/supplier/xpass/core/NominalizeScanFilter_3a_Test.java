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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.msd.filter.result.IMassSpectrumFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.xpass.settings.NominalizeFilterSettings;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * PreserveTandemMS - false
 */
public class NominalizeScanFilter_3a_Test {

	private NominalizeScanFilter filter;
	private NominalizeFilterSettings settings;
	private List<IScanMSD> massSpectra;

	@BeforeEach
	public void setUp() {

		filter = new NominalizeScanFilter();
		settings = new NominalizeFilterSettings();
		settings.setPreserveTandemMS(false);
		ScanMSD massSpectrum = new ScanMSD();
		massSpectrum.addIon(new Ion(100.1, 1000f));
		massSpectrum.addIon(new Ion(100.2, 2000f));
		massSpectrum.addIon(new Ion(200.8, 5000f));
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
		assertEquals(2, massSpectra.get(0).getIons().size());
	}

	@Test
	public void test03() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		List<IIon> ions = sortedIons(massSpectra.get(0));
		assertEquals(100.0, ions.get(0).getIon(), 0.001);
	}

	@Test
	public void test04() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		List<IIon> ions = sortedIons(massSpectra.get(0));
		assertEquals(201.0, ions.get(1).getIon(), 0.001);
	}

	@Test
	public void test05() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		List<IIon> ions = sortedIons(massSpectra.get(0));
		assertEquals(3000f, ions.get(0).getAbundance(), 0.001);
	}

	@Test
	public void test06() {

		filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		List<IIon> ions = sortedIons(massSpectra.get(0));
		assertEquals(5000f, ions.get(1).getAbundance(), 0.001);
	}

	@Test
	public void test07() {

		IProcessingInfo<IMassSpectrumFilterResult> processingInfo = filter.applyFilter(massSpectra, settings, new NullProgressMonitor());
		assertEquals(ResultStatus.OK, processingInfo.getProcessingResult().getResultStatus());
	}

	private List<IIon> sortedIons(IScanMSD massSpectrum) {

		List<IIon> ions = new ArrayList<>(massSpectrum.getIons());
		ions.sort((a, b) -> Double.compare(a.getIon(), b.getIon()));
		return ions;
	}
}