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
package org.eclipse.chemclipse.msd.identifier.supplier.nist.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class LinuxWineSupport_DRIVE_1_ITest extends AbstractBackgroundTestCase {

	private IExtendedRuntimeSupport runtimeSupport;
	private String nistApplicationPath;
	private String nistApplication;
	private File testfileNistApplication;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
		nistApplication = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_WINE_DRIVE_NIST_APPLICATION);
		testfileNistApplication = new File(nistApplication);
		nistApplicationPath = testfileNistApplication.getParent();
		runtimeSupport = new LinuxWineSupport(testfileNistApplication.getParentFile(), parameterBackground);
	}

	@Test
	public void testGetAutoimpFile_1() {

		String result = nistApplicationPath + File.separator + INistSupport.AUTOIMP_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getAutoimpFile());
	}

	@Test
	public void testGetFilespecFile_1() {

		String result = TestPathHelper.getStoragePath() + File.separator + INistSupport.FILESPEC_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getFilespecFile());
	}

	@Test
	public void testGetMassSpectraFile_1() {

		String result = TestPathHelper.getStoragePath() + File.separator + INistSupport.MASSSPECTRA_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getMassSpectraFile());
	}

	@Test
	public void testGetNistlogFile_1() {

		String result = nistApplicationPath + File.separator + INistSupport.NISTLOG_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getNistlogFile());
	}

	@Test
	public void testGetScrreadyFile_1() {

		String result = nistApplicationPath + File.separator + INistSupport.SRCREADY_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getSrcreadyFile());
	}

	@Test
	public void testGetSrcresltFile_1() {

		String result = nistApplicationPath + File.separator + INistSupport.SRCRESLT_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getSrcresltFile());
	}

	@Test
	public void testGetNistSettingsFile_1() {

		String result = nistApplicationPath + File.separator + INistSupport.NIST_SETTINGS_FILE;
		assertEquals(result, runtimeSupport.getNistSupport().getNISTSettingsFile());
	}

	@Test
	public void testGetNistApplication_1() {

		String result = nistApplicationPath + File.separator + "nistms.exe";
		assertEquals(result, runtimeSupport.getApplication());
	}

	@Test
	public void testGetNistApplicationPath_1() {

		String result = nistApplicationPath;
		assertEquals(result, runtimeSupport.getApplicationPath());
	}

	@Test
	public void testGetNistExecutable_1() {

		String result = "nistms.exe";
		assertEquals(result, runtimeSupport.getApplicationExecutable());
	}

	@Test
	public void testIsValidNistExecutable_1() {

		assertTrue(runtimeSupport.isValidApplicationExecutable());
	}
}
