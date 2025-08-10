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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.TestPathHelper;
import org.junit.Before;
import org.junit.Test;

public class LinuxWineSupport_DRIVE_2_ITest extends AbstractBackgroundTestCase {

	private IExtendedRuntimeSupport runtimeSupport;

	@Override
	@Before
	public void setUp() throws Exception {

		super.setUp();
	}

	@Test
	public void testConstruct_1() throws FileNotFoundException {

		String nistApp = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_WINE_DRIVE_NIST_APPLICATION);
		runtimeSupport = new LinuxWineSupport(new File(nistApp).getParentFile(), parameterBackground);
		assertNotNull(runtimeSupport);
	}

	@Test
	public void testConstruct_2() {

		String nistApp = "";
		assertThrows(FileNotFoundException.class, () -> {
			runtimeSupport = new LinuxWineSupport(new File(nistApp).getParentFile(), parameterBackground);
			assertNull(runtimeSupport);
		});
	}
}
