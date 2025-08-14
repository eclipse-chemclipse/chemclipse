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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.app.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.chemclipse.rcp.app.TestPathHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.junit.Test;

public class Profiles_1_ITest {

	@Test
	public void test1() throws FileNotFoundException, CoreException {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_EXPORT_DIR) + TestPathHelper.TESTFILE_EXPORT_NAME);
		try {
			Profiles.exportProfile(file);
			assertTrue(file.delete());
		} finally {
			/*
			 * Safety delete if something goes wrong.
			 */
			if(file != null) {
				file.delete();
			}
		}
	}

	@Test
	public void test2() throws FileNotFoundException, CoreException {

		File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_PREFS_1));
		IStatus status = Profiles.importProfile(file);
		assertEquals(IStatus.OK, status.getCode());
	}
}
