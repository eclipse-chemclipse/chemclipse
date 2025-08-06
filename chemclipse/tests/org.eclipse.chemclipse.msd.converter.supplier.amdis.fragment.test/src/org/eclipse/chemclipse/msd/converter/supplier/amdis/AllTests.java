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
package org.eclipse.chemclipse.msd.converter.supplier.amdis;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.format.MSL;
import org.eclipse.chemclipse.rcp.app.test.TestAssembler;
import org.osgi.framework.FrameworkUtil;

public class AllTests {

	public static Test suite() {

		TestAssembler testAssembler = new TestAssembler(FrameworkUtil.getBundle(MSL.class).getBundleContext().getBundles());
		TestSuite suite = new TestSuite("Run all tests.");
		String bundleAndPackageName = "org.eclipse.chemclipse.msd.converter.supplier.amdis";
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_Test"); // Unit
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_ITest"); // Integration
		return suite;
	}
}
