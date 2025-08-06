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
package org.eclipse.chemclipse.msd.identifier.supplier.nist;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.runtime.INistSupport;
import org.eclipse.chemclipse.rcp.app.test.TestAssembler;
import org.osgi.framework.FrameworkUtil;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {

		TestAssembler testAssembler = new TestAssembler(FrameworkUtil.getBundle(INistSupport.class).getBundleContext().getBundles());
		TestSuite suite = new TestSuite("Run all tests.");
		String bundleAndPackageName = "org.eclipse.chemclipse.msd.identifier.supplier.nist";
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_Test"); // Unit
		testAssembler.assembleTests(suite, bundleAndPackageName, bundleAndPackageName, "*_ITest"); // Integration
		return suite;
	}
}
