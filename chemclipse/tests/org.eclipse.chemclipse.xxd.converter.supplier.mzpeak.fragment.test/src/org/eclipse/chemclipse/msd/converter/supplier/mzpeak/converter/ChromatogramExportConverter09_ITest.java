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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzpeak.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.ChromatogramImportConverterIndexed;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.mzpeak.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.io.TempDir;

@TestInstance(Lifecycle.PER_CLASS)
public class ChromatogramExportConverter09_ITest {

	@TempDir
	static Path tempDirectory;

	private boolean importProfileSpectra;
	private IChromatogramMSD original;
	private File exportFile;
	private IChromatogramMSD chromatogram;
	private IChromatogramMSD profileChromatogram;

	@BeforeAll
	public void setUp() {

		importProfileSpectra = PreferenceSupplier.isImportProfileSpectra();
		PreferenceSupplier.INSTANCE().putBoolean(PreferenceSupplier.P_IMPORT_PROFILE_SPECTRA, true);

		File importFile = new File("testData/small.mzML");
		original = new ChromatogramImportConverterIndexed().convert(importFile, new NullProgressMonitor()).getProcessingResult();
		original.enforceLoadScanProxies(new NullProgressMonitor());

		exportFile = tempDirectory.resolve("small.mzpeak").toFile();
		IProcessingInfo<File> processingInfo = new ChromatogramExportConverter().convert(exportFile, original, new NullProgressMonitor());
		assertFalse(processingInfo.hasErrorMessages());

		/*
		 * The centroided spectra are read into the chromatogram, the profile spectra into a
		 * referenced one.
		 */
		chromatogram = new ChromatogramImportConverter().convert(exportFile, new NullProgressMonitor()).getProcessingResult();
		chromatogram.enforceLoadScanProxies(new NullProgressMonitor());
		assertEquals(1, chromatogram.getReferencedChromatograms().size());
		profileChromatogram = (IChromatogramMSD)chromatogram.getReferencedChromatograms().getFirst();
		profileChromatogram.enforceLoadScanProxies(new NullProgressMonitor());
	}

	@AfterAll
	public void tearDown() {

		PreferenceSupplier.INSTANCE().putBoolean(PreferenceSupplier.P_IMPORT_PROFILE_SPECTRA, importProfileSpectra);
	}

	@Test
	public void testMagicNumber() {

		assertTrue(new MagicNumberMatcher().checkFileFormat(exportFile));
	}

	@Test
	public void testArchive() throws IOException {

		try (ZipFile zipFile = new ZipFile(exportFile)) {
			assertNotNull(zipFile.getEntry("mzpeak_index.json"));
			assertNotNull(zipFile.getEntry("spectra_metadata.parquet"));
			assertNotNull(zipFile.getEntry("spectra_peaks.parquet"));
			assertNotNull(zipFile.getEntry("spectra_data.parquet"));
			assertNotNull(zipFile.getEntry("chromatograms_metadata.parquet"));
			assertNotNull(zipFile.getEntry("chromatograms_data.parquet"));
			zipFile.stream().forEach(entry -> assertEquals(ZipEntry.STORED, entry.getMethod(), entry.getName()));
		}
		assertTrue(Files.size(exportFile.toPath()) > 0);
	}

	@Test
	public void testImport() {

		assertNotNull(chromatogram);
	}

	@Test
	public void testInstrument() {

		assertEquals("LTQ FT", chromatogram.getInstrument());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals(original.getNumberOfScans(), chromatogram.getNumberOfScans());
	}

	@Test
	public void testIons() {

		assertEquals(original.getNumberOfScanIons(), chromatogram.getNumberOfScanIons() + profileChromatogram.getNumberOfScanIons());
	}

	@Test
	public void testScans() {

		for(int scanNumber = 1; scanNumber <= original.getNumberOfScans(); scanNumber++) {
			IScanMSD expected = original.getScan(scanNumber);
			IScanMSD actual = getExportedScan(scanNumber);
			String message = "scan " + scanNumber;
			assertEquals(expected.getRetentionTime(), actual.getRetentionTime(), message);
			assertEquals(expected.getNumberOfIons(), actual.getNumberOfIons(), message);
			assertEquals(expected.getTotalSignal(), actual.getTotalSignal(), 0, message);
			assertEquals(expected.getBasePeak(), actual.getBasePeak(), 0, message);
			assertEquals(expected.getBasePeakAbundance(), actual.getBasePeakAbundance(), 0, message);
		}
	}

	@Test
	public void testFirstScanIons() {

		List<IIon> expected = original.getScan(1).getIons();
		List<IIon> actual = getExportedScan(1).getIons();
		assertEquals(expected.size(), actual.size());
		for(int i = 0; i < expected.size(); i++) {
			String message = "ion " + i;
			assertEquals(expected.get(i).getIon(), actual.get(i).getIon(), 0, message);
			assertEquals(expected.get(i).getAbundance(), actual.get(i).getAbundance(), 0, message);
		}
	}

	private IScanMSD getExportedScan(int scanNumber) {

		IScanMSD scan = original.getScan(scanNumber);
		boolean isProfile = scan instanceof IRegularMassSpectrum massSpectrum && massSpectrum.getMassSpectrumType() == MassSpectrumType.PROFILE;
		return isProfile ? profileChromatogram.getScan(scanNumber) : chromatogram.getScan(scanNumber);
	}
}
