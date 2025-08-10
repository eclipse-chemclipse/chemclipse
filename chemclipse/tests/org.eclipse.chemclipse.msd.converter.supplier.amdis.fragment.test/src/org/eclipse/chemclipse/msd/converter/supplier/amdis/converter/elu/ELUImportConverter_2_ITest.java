/*******************************************************************************
 * Copyright (c) 2015, 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Janos Binder - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.elu;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.PathResolver;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.ELUReader;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IPeakMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IPeaksMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.junit.Before;
import org.junit.Test;

public class ELUImportConverter_2_ITest {

	private ELUReader reader;
	private File file;

	@Before
	public void setUp() throws Exception {

		reader = new ELUReader();
		String pathname = PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PEAKS_1_ELU);
		file = new File(pathname);
	}

	@Test
	public void testRead_1() throws IOException {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		preferences.putBoolean(PreferenceSupplier.P_EXCLUDE_UNCERTAIN_IONS, true);

		IProcessingInfo<IPeaksMSD> processingInfo = reader.read(file, new NullProgressMonitor());
		List<IPeakMSD> peaks = processingInfo.getProcessingResult().getPeaks();

		IPeakMSD peak1 = peaks.get(0);
		IPeakMassSpectrum peakMassSpectrum1 = peak1.getPeakModel().getPeakMassSpectrum();
		List<IIon> ions1 = peakMassSpectrum1.getIons();

		IPeakMSD peak2 = peaks.get(1);
		IPeakMassSpectrum peakMassSpectrum2 = peak2.getPeakModel().getPeakMassSpectrum();
		List<IIon> ions2 = peakMassSpectrum2.getIons();

		IPeakMSD peak3 = peaks.get(2);
		IPeakMassSpectrum peakMassSpectrum3 = peak3.getPeakModel().getPeakMassSpectrum();
		List<IIon> ions3 = peakMassSpectrum3.getIons();

		IIon ion10 = ions1.get(0);
		assertEquals("Ion 0-0", 28.0, ion10.getIon(), 0);
		assertEquals("Abundance 0-0", 4492.783, ion10.getAbundance(), 1E-3);

		IIon ion11 = ions1.get(1);
		assertEquals("Ion 0-1", 29.0, ion11.getIon(), 0);
		assertEquals("Abundance 0-1", 76.45377, ion11.getAbundance(), 1E-5);

		IIon ion12 = ions1.get(2);
		assertEquals("Ion 0-2", 32.0, ion12.getIon(), 0);
		assertEquals("Abundance 0-2", 1218.7631, ion12.getAbundance(), 1E-4);

		IIon ion20 = ions2.get(0);
		assertEquals("Ion 1-0", 16.0, ion20.getIon(), 0);
		assertEquals("Abundance 1-0", 92493.41, ion20.getAbundance(), 1E-2);

		IIon ion21 = ions2.get(1);
		assertEquals("Ion 1-5", 28.0, ion21.getIon(), 0);
		assertEquals("Abundance 1-5", 7107762.5, ion21.getAbundance(), 1E-6);

		IIon ion22 = ions2.get(2);
		assertEquals("Ion 1-2", 29.0, ion22.getIon(), 0);
		assertEquals("Abundance 1-2", 56919.02, ion22.getAbundance(), 1E-2);

		IIon ion23 = ions2.get(3);
		assertEquals("Ion 1-4", 32.0, ion23.getIon(), 0);
		assertEquals("Abundance 1-4", 2106003.8, ion23.getAbundance(), 1E-1);

		IIon ion24 = ions2.get(4);
		assertEquals("Ion 1-4", 34.0, ion24.getIon(), 0);
		assertEquals("Abundance 1-4", 7114.8774, ion24.getAbundance(), 1E-4);

		IIon ion25 = ions2.get(5);
		assertEquals("Ion 1-5", 40.0, ion25.getIon(), 0);
		assertEquals("Abundance 1-5", 177871.94, ion25.getAbundance(), 1E-2);

		IIon ion30 = ions3.get(0);
		assertEquals("Ion 2-0", 20.0, ion30.getIon(), 0);
		assertEquals("Abundance 2-0", 4583.8853, ion30.getAbundance(), 1E-4);

		IIon ion31 = ions3.get(1);
		assertEquals("Ion 2-1", 28.0, ion31.getIon(), 0);
		assertEquals("Abundance 2-1", 4579301.5, ion31.getAbundance(), 1E-2);

		IIon ion32 = ions3.get(2);
		assertEquals("Ion 2-2", 29.0, ion32.getIon(), 0);
		assertEquals("Abundance 2-2", 45838.85, ion32.getAbundance(), 1E-2);
	}
}
