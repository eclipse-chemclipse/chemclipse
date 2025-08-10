/*******************************************************************************
 * Copyright (c) 2008, 2025 Lablicate GmbH.
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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msl;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.database.IDatabaseExportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;

public class MSLExportConverter_1_ITest {

	private IDatabaseExportConverter exportConverter;
	private File exportFile;
	private IScanMSD massSpectrum;
	private IMassSpectra massSpectra;

	@Before
	public void setUp() throws Exception {

		exportConverter = new MSLDatabaseExportConverter();
		exportFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTDIR_EXPORT) + File.separator + TestPathHelper.TESTFILE_EXPORT_DB_1_MSL);
		massSpectrum = new ScanMSD();
		massSpectrum.addIon(new Ion(56.3f, 7382.3f));
		massSpectrum.addIon(new Ion(26.3f, 73382.3f));
		massSpectrum.addIon(new Ion(89.3f, 382.3f));
		massSpectra = new MassSpectra();
		massSpectra.addMassSpectrum(massSpectrum);
	}

	@Test
	public void testExport_1() {

		IProcessingInfo<?> processingInfo = exportConverter.convert(null, massSpectrum, false, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testExport_2() {

		massSpectrum = null;
		IProcessingInfo<?> processingInfo = exportConverter.convert(exportFile, massSpectrum, false, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testExport_3() {

		massSpectra = null;
		IProcessingInfo<?> processingInfo = exportConverter.convert(exportFile, massSpectra, false, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
	}

	@Test
	public void testExport_4() {

		exportFile.setWritable(false);
		IProcessingInfo<?> processingInfo = exportConverter.convert(exportFile, massSpectra, false, new NullProgressMonitor());
		assertTrue(processingInfo.hasErrorMessages());
		exportFile.delete();
	}
}