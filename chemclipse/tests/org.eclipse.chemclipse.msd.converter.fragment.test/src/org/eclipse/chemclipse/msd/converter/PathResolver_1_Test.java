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
package org.eclipse.chemclipse.msd.converter;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;

public class PathResolver_1_Test {

	@Test
	public void testGetAbsolutePath_1() throws IOException, URISyntaxException {

		URL url = getClass().getClassLoader().getResource(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_EMPTY);
		String test = PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_EMPTY);
		String path = new File(FileLocator.toFileURL(url).toURI()).getAbsolutePath();
		// Handle Windows systems in a different way than unix/linux :-).
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("windows")) {
			if(test.startsWith("/")) {
				test = test.substring(1);
			}
			test = test.replace('\\', '/');
			path = path.replace('\\', '/');
		}
		/*
		 * Tests folder, but path resolver is located in plugins.
		 * .../git/chemclipse/chemclipse/tests/org.eclipse.chemclipse.msd.converter.fragment.test/testData/files/import/EMPTY.D/DATA.MS
		 */
		path = path.replace("plugins", "tests");
		assertEquals(path, test);
	}
}
